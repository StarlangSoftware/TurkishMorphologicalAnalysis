package MorphologicalAnalysis;

import Corpus.Sentence;
import DataStructure.Cache.LRUCache;
import Dictionary.Trie.Trie;
import Dictionary.*;
import Util.FileUtils;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class FsmMorphologicalAnalyzer {

    private final Trie dictionaryTrie;
    private Trie suffixTrie;
    private HashMap<String, String> parsedSurfaceForms = null;
    private HashMap<String, String> pronunciations;
    private final FiniteStateMachine finiteStateMachine;
    private static final int MAX_DISTANCE = 2;
    private final TxtDictionary dictionary;
    private final LRUCache<String, FsmParseList> cache;
    private final HashMap<String, Pattern> mostUsedPatterns = new HashMap<>();

    /**
     * First no-arg constructor of FsmMorphologicalAnalyzer class. It generates a new TxtDictionary type dictionary from
     * turkish_dictionary.txt with fixed cache size 10000000 and by using turkish_finite_state_machine.xml file.
     */
    public FsmMorphologicalAnalyzer() {
        this("turkish_finite_state_machine.xml", new TxtDictionary(), 10000000);
    }

    /**
     * Another constructor of FsmMorphologicalAnalyzer class. It generates a new TxtDictionary type dictionary from
     * turkish_dictionary.txt with given input cacheSize and by using turkish_finite_state_machine.xml file.
     *
     * @param cacheSize the size of the LRUCache.
     */
    public FsmMorphologicalAnalyzer(int cacheSize) {
        this("turkish_finite_state_machine.xml", new TxtDictionary(), cacheSize);
    }

    /**
     * Another constructor of FsmMorphologicalAnalyzer class. It generates a new TxtDictionary type dictionary from
     * given input dictionary file name and by using turkish_finite_state_machine.xml file.
     *
     * @param dictionaryFileName the size of the LRUCache.
     */
    public FsmMorphologicalAnalyzer(String dictionaryFileName) {
        this("turkish_finite_state_machine.xml", new TxtDictionary(dictionaryFileName, new TurkishWordComparator()), 10000000);
    }

    /**
     * Another constructor of FsmMorphologicalAnalyzer class. It generates a new TxtDictionary type dictionary from
     * given input dictionary file name and by using turkish_finite_state_machine.xml file.
     *
     * @param fileName           the file to read the finite state machine.
     * @param dictionaryFileName the file to read the dictionary.
     */
    public FsmMorphologicalAnalyzer(String fileName, String dictionaryFileName) {
        this(fileName, new TxtDictionary(dictionaryFileName, new TurkishWordComparator()), 10000000);
    }

    /**
     * Another constructor of FsmMorphologicalAnalyzer class. It generates a new TxtDictionary type dictionary from
     * given input dictionary, with given inputs fileName and cacheSize.
     *
     * @param fileName   the file to read the finite state machine.
     * @param dictionary the dictionary file that will be used to generate dictionaryTrie.
     * @param cacheSize  the size of the LRUCache.
     */
    public FsmMorphologicalAnalyzer(String fileName, TxtDictionary dictionary, int cacheSize) {
        this.dictionary = dictionary;
        finiteStateMachine = new FiniteStateMachine(fileName);
        prepareSuffixTrie();
        dictionaryTrie = dictionary.prepareTrie();
        if (cacheSize > 0){
            cache = new LRUCache<>(cacheSize);
        } else {
            cache = null;
        }
        addPronunciations("pronunciations.txt");
    }

    /**
     * Another constructor of FsmMorphologicalAnalyzer class. It generates a new TxtDictionary type dictionary from
     * given input dictionary, with given input fileName and fixed size cacheSize = 10000000.
     *
     * @param fileName   the file to read the finite state machine.
     * @param dictionary the dictionary file that will be used to generate dictionaryTrie.
     */
    public FsmMorphologicalAnalyzer(String fileName, TxtDictionary dictionary) {
        this(fileName, dictionary, 10000000);
    }

    /**
     * Another constructor of FsmMorphologicalAnalyzer class. It generates a new TxtDictionary type dictionary from
     * given input dictionary, with given input fileName and fixed size cacheSize = 10000000.
     *
     * @param dictionary the dictionary file that will be used to generate dictionaryTrie.
     */
    public FsmMorphologicalAnalyzer(TxtDictionary dictionary) {
        this("turkish_finite_state_machine.xml", dictionary, 10000000);
    }

    /**
     * Constructs and returns the reverse string of a given string.
     * @param s String to be reversed.
     * @return Reverse of a given string.
     */
    private String reverseString(String s){
        StringBuilder result = new StringBuilder();
        for (int i = s.length() - 1; i >= 0; i--){
            result.append(s.charAt(i));
        }
        return result.toString();
    }

    /**
     * Constructs the suffix trie from the input file suffixes.txt. suffixes.txt contains the most frequent 6000
     * suffixes that a verb or a noun can take. The suffix trie is a trie that stores these suffixes in reverse form,
     * which can be then used to match a given word for its possible suffix content.
     */
    private void prepareSuffixTrie(){
        suffixTrie = new Trie();
        Scanner inputFile = new Scanner(FileUtils.getInputStream("suffixes.txt"));
        while (inputFile.hasNext()){
            String suffix = inputFile.next();
            String reverseSuffix = reverseString(suffix);
            suffixTrie.addWord(reverseSuffix, new Word(reverseSuffix));
        }
        inputFile.close();
    }

    /**
     * Reads the file for foreign words and their pronunciations.
     * @param fileName Input file containing foreign words and their pronunciations.
     */
    public void addPronunciations(String fileName){
        pronunciations = FileUtils.readHashMap(fileName);
    }

    /**
     * Reads the file for correct surface forms and their most frequent root forms, in other words, the surface forms
     * which have at least one morphological analysis in  Turkish.
     * @param fileName Input file containing analyzable surface forms and their root forms.
     */
    public void addParsedSurfaceForms(String fileName){
        parsedSurfaceForms = FileUtils.readHashMap(fileName);
    }

    /**
     * The getPossibleWords method takes {@link MorphologicalParse} and {@link MetamorphicParse} as input.
     * First it determines whether the given morphologicalParse is the root verb and whether it contains a verb tag.
     * Then it creates new transition with -mak and creates a new {@link HashSet} result.
     * <p>
     * It takes the given {@link MetamorphicParse} input as currentWord and if there is a compound word starting with the
     * currentWord, it gets this compoundWord from dictionaryTrie. If there is a compoundWord and the difference of the
     * currentWord and compundWords is less than 3 than compoundWord is added to the result, otherwise currentWord is added.
     * <p>
     * Then it gets the root from parse input as a currentRoot. If it is not null, and morphologicalParse input is verb,
     * it directly adds the verb to result after making transition to currentRoot with currentWord String. Else, it creates a new
     * transition with -lar and make this transition then adds to the result.
     *
     * @param morphologicalParse {@link MorphologicalParse} type input.
     * @param metamorphicParse              {@link MetamorphicParse} type input.
     * @return {@link HashSet} result.
     */
    public HashSet<String> getPossibleWords(MorphologicalParse morphologicalParse, MetamorphicParse metamorphicParse) {
        boolean isRootVerb = morphologicalParse.getRootPos().equals("VERB");
        boolean containsVerb = morphologicalParse.containsTag(MorphologicalTag.VERB);
        Transition transition, verbTransition = new Transition("mAk");
        TxtWord compoundWord, currentRoot;
        HashSet<String> result = new HashSet<>();
        if (metamorphicParse == null || metamorphicParse.getWord() == null) {
            return result;
        }
        String verbWord, pluralWord, currentWord = metamorphicParse.getWord().getName();
        int pluralIndex = -1;
        compoundWord = dictionaryTrie.getCompoundWordStartingWith(currentWord);
        if (!isRootVerb) {
            if (compoundWord != null && compoundWord.getName().length() - currentWord.length() < 3) {
                result.add(compoundWord.getName());
            }
            result.add(currentWord);
        }
        currentRoot = (TxtWord) dictionary.getWord(metamorphicParse.getWord().getName());
        if (currentRoot == null && compoundWord != null) {
            currentRoot = compoundWord;
        }
        if (currentRoot != null) {
            if (isRootVerb) {
                verbWord = verbTransition.makeTransition(currentRoot, currentWord);
                result.add(verbWord);
            }
            pluralWord = null;
            for (int i = 1; i < metamorphicParse.size(); i++) {
                transition = new Transition(null, metamorphicParse.getMetaMorpheme(i), null);
                if (metamorphicParse.getMetaMorpheme(i).equals("lAr")) {
                    pluralWord = currentWord;
                    pluralIndex = i + 1;
                }
                currentWord = transition.makeTransition(currentRoot, currentWord);
                result.add(currentWord);
                if (containsVerb) {
                    verbWord = verbTransition.makeTransition(currentRoot, currentWord);
                    result.add(verbWord);
                }
            }
            if (pluralWord != null) {
                currentWord = pluralWord;
                for (int i = pluralIndex; i < metamorphicParse.size(); i++) {
                    transition = new Transition(null, metamorphicParse.getMetaMorpheme(i), null);
                    currentWord = transition.makeTransition(currentRoot, currentWord);
                    result.add(currentWord);
                    if (containsVerb) {
                        verbWord = verbTransition.makeTransition(currentRoot, currentWord);
                        result.add(verbWord);
                    }
                }
            }
        }
        return result;
    }

    /**
     * The getDictionary method is used to get TxtDictionary.
     *
     * @return TxtDictionary type dictionary.
     */
    public TxtDictionary getDictionary() {
        return dictionary;
    }

    /**
     * The getFiniteStateMachine method is used to get FiniteStateMachine.
     *
     * @return FiniteStateMachine type finiteStateMachine.
     */
    public FiniteStateMachine getFiniteStateMachine() {
        return finiteStateMachine;
    }

    /**
     * The isPossibleSubstring method first checks whether given short and long strings are equal to root word.
     * Then, compares both short and long strings' chars till the last two chars of short string. In the presence of mismatch,
     * false is returned. On the other hand, it counts the distance between two strings until it becomes greater than 2,
     * which is the MAX_DISTANCE also finds the index of the last char.
     * <p>
     * If the substring is a rootWord and equals to 'ben', which is a special case or root holds the lastIdropsDuringSuffixation or
     * lastIdropsDuringPassiveSuffixation conditions, then it returns true if distance is not greater than MAX_DISTANCE.
     * <p>
     * On the other hand, if the shortStrong ends with one of these chars 'e, a, p, ç, t, k' and 't 's a rootWord with
     * the conditions of rootSoftenDuringSuffixation, vowelEChangesToIDuringYSuffixation, vowelAChangesToIDuringYSuffixation
     * or endingKChangesIntoG then it returns true if the last index is not equal to 2 and distance is not greater than
     * MAX_DISTANCE and false otherwise.
     *
     * @param shortString the possible substring.
     * @param longString  the long string to compare with substring.
     * @param root        the root of the long string.
     * @return true if given substring is the actual substring of the longString, false otherwise.
     */
    private boolean isPossibleSubstring(String shortString, String longString, TxtWord root) {
        boolean rootWord = ((shortString == root.getName()) || longString == root.getName());
        int distance = 0, j, last = 1;
        for (j = 0; j < shortString.length(); j++) {
            if (shortString.charAt(j) != longString.charAt(j)) {
                if (j < shortString.length() - 2) {
                    return false;
                }
                last = shortString.length() - j;
                distance++;
                if (distance > MAX_DISTANCE) {
                    break;
                }
            }
        }
        if (rootWord && (root.getName().equals("ben") || root.getName().equals("sen") || root.lastIdropsDuringSuffixation() || root.lastIdropsDuringPassiveSuffixation())) {
            return (distance <= MAX_DISTANCE);
        } else {
            if (shortString.endsWith("e") || shortString.endsWith("a") || shortString.endsWith("p") || shortString.endsWith("ç") || shortString.endsWith("t") || shortString.endsWith("k") || (rootWord && (root.rootSoftenDuringSuffixation() || root.vowelEChangesToIDuringYSuffixation() || root.vowelAChangesToIDuringYSuffixation() || root.endingKChangesIntoG()))) {
                return (last != 2 && distance <= MAX_DISTANCE - 1);
            } else {
                return (distance <= MAX_DISTANCE - 2);
            }
        }
    }

    /**
     * The initializeParseList method initializes the given fsm ArrayList with given root words by parsing them.
     * <p>
     * It checks many conditions;
     * isPlural; if root holds the condition then it gets the state with the name of NominalRootPlural, then
     * creates a new parsing and adds this to the input fsmParse Arraylist.
     * Ex : Açıktohumlular
     * <p>
     * !isPlural and isPortmanteauEndingWithSI, if root holds the conditions then it gets the state with the
     * name of NominalRootNoPossesive.
     * Ex : Balarısı
     * <p>
     * !isPlural and isPortmanteau, if root holds the conditions then it gets the state with the name of
     * CompoundNounRoot.
     * Ex : Aslanağızı
     * <p>
     * !isPlural, !isPortmanteau and isHeader, if root holds the conditions then it gets the state with the
     * name of HeaderRoot.
     * Ex :  </title>
     * <p>
     * !isPlural, !isPortmanteau and isInterjection, if root holds the conditions then it gets the state
     * with the name of InterjectionRoot.
     * Ex : Hey, Aa
     * <p>
     * !isPlural, !isPortmanteau and isDuplicate, if root holds the conditions then it gets the state
     * with the name of DuplicateRoot.
     * Ex : Allak,
     * <p>
     * !isPlural, !isPortmanteau and isCode, if root holds the conditions then it gets the state
     * with the name of CodeRoot.
     * Ex : 9400f,
     * <p>
     * !isPlural, !isPortmanteau and isMetric, if root holds the conditions then it gets the state
     * with the name of MetricRoot.
     * Ex : 11x8x12,
     * <p>
     * !isPlural, !isPortmanteau and isNumeral, if root holds the conditions then it gets the state
     * with the name of CardinalRoot.
     * Ex : Yüz, bin
     * <p>
     * !isPlural, !isPortmanteau and isReal, if root holds the conditions then it gets the state
     * with the name of RealRoot.
     * Ex : 1.2
     * <p>
     * !isPlural, !isPortmanteau and isFraction, if root holds the conditions then it gets the state
     * with the name of FractionRoot.
     * Ex : 1/2
     * <p>
     * !isPlural, !isPortmanteau and isDate, if root holds the conditions then it gets the state
     * with the name of DateRoot.
     * Ex : 11/06/2018
     * <p>
     * !isPlural, !isPortmanteau and isPercent, if root holds the conditions then it gets the state
     * with the name of PercentRoot.
     * Ex : %12.5
     * <p>
     * !isPlural, !isPortmanteau and isRange, if root holds the conditions then it gets the state
     * with the name of RangeRoot.
     * Ex : 3-5
     * <p>
     * !isPlural, !isPortmanteau and isTime, if root holds the conditions then it gets the state
     * with the name of TimeRoot.
     * Ex : 13:16:08
     * <p>
     * !isPlural, !isPortmanteau and isOrdinal, if root holds the conditions then it gets the state
     * with the name of OrdinalRoot.
     * Ex : Altıncı
     * <p>
     * !isPlural, !isPortmanteau, and isVerb if root holds the conditions then it gets the state
     * with the name of VerbalRoot. Or isPassive, then it gets the state with the name of PassiveHn.
     * Ex : Anla (!isPAssive)
     * Ex : Çağrıl (isPassive)
     * <p>
     * !isPlural, !isPortmanteau and isPronoun, if root holds the conditions then it gets the state
     * with the name of PronounRoot. There are 6 different Pronoun state names, REFLEX, QUANT, QUANTPLURAL, DEMONS, PERS, QUES.
     * REFLEX = Reflexive Pronouns Ex : kendi
     * QUANT = Quantitative Pronouns Ex : öbür, hep, kimse, hiçbiri, bazı, kimi, biri
     * QUANTPLURAL = Quantitative Plural Pronouns Ex : tümü, çoğu, hepsi
     * DEMONS = Demonstrative Pronouns Ex : o, bu, şu
     * PERS = Personal Pronouns Ex : ben, sen, o, biz, siz, onlar
     * QUES = Interrogatıve Pronouns Ex : nere, ne, kim, hangi
     * <p>
     * !isPlural, !isPortmanteau and isAdjective, if root holds the conditions then it gets the state
     * with the name of AdjectiveRoot.
     * Ex : Absürt, Abes
     * <p>
     * !isPlural, !isPortmanteau and isPureAdjective, if root holds the conditions then it gets the state
     * with the name of Adjective.
     * Ex : Geçmiş, Cam
     * <p>
     * !isPlural, !isPortmanteau and isNominal, if root holds the conditions then it gets the state
     * with the name of NominalRoot.
     * Ex : Görüş
     * <p>
     * !isPlural, !isPortmanteau and isProper, if root holds the conditions then it gets the state
     * with the name of ProperRoot.
     * Ex : Abdi
     * <p>
     * !isPlural, !isPortmanteau and isQuestion, if root holds the conditions then it gets the state
     * with the name of QuestionRoot.
     * Ex : Mi, mü
     * <p>
     * !isPlural, !isPortmanteau and isDeterminer, if root holds the conditions then it gets the state
     * with the name of DeterminerRoot.
     * Ex : Çok, bir
     * <p>
     * !isPlural, !isPortmanteau and isConjunction, if root holds the conditions then it gets the state
     * with the name of ConjunctionRoot.
     * Ex : Ama , ancak
     * <p>
     * !isPlural, !isPortmanteau and isPostP, if root holds the conditions then it gets the state
     * with the name of PostP.
     * Ex : Ait, dair
     * <p>
     * !isPlural, !isPortmanteau and isAdverb, if root holds the conditions then it gets the state
     * with the name of AdverbRoot.
     * Ex : Acilen
     *
     * @param fsmParse ArrayList to initialize.
     * @param root     word to check properties and add to fsmParse according to them.
     * @param isProper is used to check a word is proper or not.
     */
    private void initializeParseList(ArrayList<FsmParse> fsmParse, TxtWord root, boolean isProper) {
        FsmParse currentFsmParse;
        if (root.isPlural()) {
            currentFsmParse = new FsmParse(root, finiteStateMachine.getState("NominalRootPlural"));
            fsmParse.add(currentFsmParse);
        } else {
            if (root.isPortmanteauEndingWithSI()) {
                currentFsmParse = new FsmParse(root.getName().substring(0, root.getName().length() - 2), finiteStateMachine.getState("CompoundNounRoot"));
                fsmParse.add(currentFsmParse);
                currentFsmParse = new FsmParse(root, finiteStateMachine.getState("NominalRootNoPossesive"));
                fsmParse.add(currentFsmParse);
            } else {
                if (root.isPortmanteau()) {
                    if (root.isPortmanteauFacedVowelEllipsis()){
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("NominalRootNoPossesive"));
                        fsmParse.add(currentFsmParse);
                        currentFsmParse = new FsmParse(root.getName().substring(0, root.getName().length() - 2) + root.getName().charAt(root.getName().length() - 1) + root.getName().charAt(root.getName().length() - 2), finiteStateMachine.getState("CompoundNounRoot"));
                    } else {
                        if (root.isPortmanteauFacedSoftening()){
                            currentFsmParse = new FsmParse(root, finiteStateMachine.getState("NominalRootNoPossesive"));
                            fsmParse.add(currentFsmParse);
                            switch (root.getName().charAt(root.getName().length() - 2)){
                                case 'b':
                                    currentFsmParse = new FsmParse(root.getName().substring(0, root.getName().length() - 2) + 'p', finiteStateMachine.getState("CompoundNounRoot"));
                                    break;
                                case 'c':
                                    currentFsmParse = new FsmParse(root.getName().substring(0, root.getName().length() - 2) + 'ç', finiteStateMachine.getState("CompoundNounRoot"));
                                    break;
                                case 'd':
                                    currentFsmParse = new FsmParse(root.getName().substring(0, root.getName().length() - 2) + 't', finiteStateMachine.getState("CompoundNounRoot"));
                                    break;
                                case 'ğ':
                                    currentFsmParse = new FsmParse(root.getName().substring(0, root.getName().length() - 2) + 'k', finiteStateMachine.getState("CompoundNounRoot"));
                                    break;
                                default:
                                    currentFsmParse = new FsmParse(root.getName().substring(0, root.getName().length() - 1), finiteStateMachine.getState("CompoundNounRoot"));
                            }
                        } else {
                            currentFsmParse = new FsmParse(root.getName().substring(0, root.getName().length() - 1), finiteStateMachine.getState("CompoundNounRoot"));
                        }
                    }
                    fsmParse.add(currentFsmParse);
                } else {
                    if (root.isHeader()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("HeaderRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isInterjection()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("InterjectionRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isDuplicate()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("DuplicateRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isCode()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("CodeRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isMetric()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("MetricRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isNumeral()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("CardinalRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isReal()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("RealRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isFraction()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("FractionRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isDate()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("DateRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isPercent()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("PercentRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isRange()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("RangeRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isTime()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("TimeRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isOrdinal()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("OrdinalRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isVerb() || root.isPassive()) {
                        if (!root.verbType().equalsIgnoreCase("")) {
                            currentFsmParse = new FsmParse(root, finiteStateMachine.getState("VerbalRoot(" + root.verbType() + ")"));
                        } else {
                            if (!root.isPassive()) {
                                currentFsmParse = new FsmParse(root, finiteStateMachine.getState("VerbalRoot"));
                            } else {
                                currentFsmParse = new FsmParse(root, finiteStateMachine.getState("PassiveHn"));
                            }
                        }
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isPronoun()) {
                        if (root.getName().equalsIgnoreCase("kendi")) {
                            currentFsmParse = new FsmParse(root, finiteStateMachine.getState("PronounRoot(REFLEX)"));
                            fsmParse.add(currentFsmParse);
                        }
                        if (root.getName().equalsIgnoreCase("öbür") || root.getName().equalsIgnoreCase("öteki") || root.getName().equalsIgnoreCase("hep") || root.getName().equalsIgnoreCase("kimse") || root.getName().equalsIgnoreCase("diğeri") || root.getName().equalsIgnoreCase("hiçbiri") || root.getName().equalsIgnoreCase("böylesi") || root.getName().equalsIgnoreCase("birbiri") || root.getName().equalsIgnoreCase("birbirleri") || root.getName().equalsIgnoreCase("biri") || root.getName().equalsIgnoreCase("başkası") || root.getName().equalsIgnoreCase("bazı") || root.getName().equalsIgnoreCase("kimi")) {
                            currentFsmParse = new FsmParse(root, finiteStateMachine.getState("PronounRoot(QUANT)"));
                            fsmParse.add(currentFsmParse);
                        }
                        if (root.getName().equalsIgnoreCase("tümü") || root.getName().equalsIgnoreCase("topu") || root.getName().equalsIgnoreCase("herkes") || root.getName().equalsIgnoreCase("cümlesi") || root.getName().equalsIgnoreCase("çoğu") || root.getName().equalsIgnoreCase("birçoğu") || root.getName().equalsIgnoreCase("birkaçı") || root.getName().equalsIgnoreCase("birçokları") || root.getName().equalsIgnoreCase("hepsi")) {
                            currentFsmParse = new FsmParse(root, finiteStateMachine.getState("PronounRoot(QUANTPLURAL)"));
                            fsmParse.add(currentFsmParse);
                        }
                        if (root.getName().equalsIgnoreCase("o") || root.getName().equalsIgnoreCase("bu") || root.getName().equalsIgnoreCase("şu")) {
                            currentFsmParse = new FsmParse(root, finiteStateMachine.getState("PronounRoot(DEMONS)"));
                            fsmParse.add(currentFsmParse);
                        }
                        if (root.getName().equalsIgnoreCase("ben") || root.getName().equalsIgnoreCase("sen") || root.getName().equalsIgnoreCase("o") || root.getName().equalsIgnoreCase("biz") || root.getName().equalsIgnoreCase("siz") || root.getName().equalsIgnoreCase("onlar")) {
                            currentFsmParse = new FsmParse(root, finiteStateMachine.getState("PronounRoot(PERS)"));
                            fsmParse.add(currentFsmParse);
                        }
                        if (root.getName().equalsIgnoreCase("nere") || root.getName().equalsIgnoreCase("ne") || root.getName().equalsIgnoreCase("kaçı") || root.getName().equalsIgnoreCase("kim") || root.getName().equalsIgnoreCase("hangi")) {
                            currentFsmParse = new FsmParse(root, finiteStateMachine.getState("PronounRoot(QUES)"));
                            fsmParse.add(currentFsmParse);
                        }
                    }
                    if (root.isAdjective()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("AdjectiveRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isPureAdjective()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("Adjective"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isNominal()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("NominalRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isAbbreviation()){
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("NominalRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isProperNoun() && isProper) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("ProperRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isQuestion()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("QuestionRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isDeterminer()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("DeterminerRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isConjunction()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("ConjunctionRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isPostP()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("PostP"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isAdverb()) {
                        currentFsmParse = new FsmParse(root, finiteStateMachine.getState("AdverbRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                }
            }
        }
    }

    /**
     * The initializeParseListFromRoot method is used to create an {@link ArrayList} which consists of initial fsm parsings. First, traverses
     * this HashSet and uses each word as a root and calls initializeParseList method with this root and ArrayList.
     * <p>
     *
     * @param parseList ArrayList to initialize.
     * @param root the root form to generate initial parse list.
     * @param isProper    is used to check a word is proper or not.
     */
    private void initializeParseListFromRoot(ArrayList<FsmParse> parseList, TxtWord root, boolean isProper) {
        initializeParseList(parseList, root, isProper);
        if (root.obeysAndNotObeysVowelHarmonyDuringAgglutination()){
            TxtWord newRoot = root.clone();
            newRoot.removeFlag("IS_UU");
            newRoot.removeFlag("IS_UUU");
            initializeParseList(parseList, newRoot, isProper);
        }
        if (root.rootSoftenAndNotSoftenDuringSuffixation()){
            TxtWord newRoot = root.clone();
            newRoot.removeFlag("IS_SD");
            newRoot.removeFlag("IS_SDD");
            initializeParseList(parseList, newRoot, isProper);
        }
        if (root.lastIDropsAndNotDropDuringSuffixation()){
            TxtWord newRoot = root.clone();
            newRoot.removeFlag("IS_UD");
            newRoot.removeFlag("IS_UDD");
            initializeParseList(parseList, newRoot, isProper);
        }
        if (root.duplicatesAndNotDuplicatesDuringSuffixation()){
            TxtWord newRoot = root.clone();
            newRoot.removeFlag("IS_ST");
            newRoot.removeFlag("IS_STT");
            initializeParseList(parseList, newRoot, isProper);
        }
        if (root.endingKChangesIntoG() && root.containsFlag("IS_OA")){
            TxtWord newRoot = root.clone();
            newRoot.removeFlag("IS_OA");
            initializeParseList(parseList, newRoot, isProper);
        }
    }

    /**
     * The initializeParseListFromSurfaceForm method is used to create an {@link ArrayList} which consists of initial fsm parsings. First,
     * it calls getWordsWithPrefix methods by using input String surfaceForm and generates a {@link HashSet}. Then, traverses
     * this HashSet and uses each word as a root and calls initializeParseListFromRoot method with this root and ArrayList.
     * <p>
     *
     * @param surfaceForm the String used to generate a HashSet of words.
     * @param isProper    is used to check a word is proper or not.
     * @return initialFsmParse ArrayList.
     */
    private ArrayList<FsmParse> initializeParseListFromSurfaceForm(String surfaceForm, boolean isProper) {
        TxtWord root;
        ArrayList<FsmParse> initialFsmParse;
        initialFsmParse = new ArrayList<>();
        if (surfaceForm.isEmpty()) {
            return initialFsmParse;
        }
        HashSet<Word> words = dictionaryTrie.getWordsWithPrefix(surfaceForm);
        for (Word word : words) {
            root = (TxtWord) word;
            initializeParseListFromRoot(initialFsmParse, root, isProper);
        }
        return initialFsmParse;
    }

    /**
     * The addNewParsesFromCurrentParse method initially gets the final suffixes from input currentFsmParse called as currentState,
     * and by using the currentState information it gets the new analysis. Then loops through each currentState's transition.
     * If the currentTransition is possible, it makes the transition.
     *
     * @param currentFsmParse FsmParse type input.
     * @param fsmParse        an ArrayList of FsmParse.
     * @param maxLength     Maximum length of the parse.
     * @param root            TxtWord used to make transition.
     */
    private void addNewParsesFromCurrentParse(FsmParse currentFsmParse, Queue<FsmParse> fsmParse, int maxLength, TxtWord root) {
        State currentState = currentFsmParse.getFinalSuffix();
        String currentSurfaceForm = currentFsmParse.getSurfaceForm();
        for (Transition currentTransition : finiteStateMachine.getTransitions(currentState)) {
            if (currentTransition.transitionPossible(currentFsmParse) && (currentSurfaceForm.compareTo(root.getName()) != 0 || (currentSurfaceForm.compareTo(root.getName()) == 0 && currentTransition.transitionPossible(root, currentState)))) {
                String tmp = currentTransition.makeTransition(root, currentSurfaceForm, currentFsmParse.getStartState());
                if (tmp.length() <= maxLength) {
                    FsmParse newFsmParse = currentFsmParse.clone();
                    newFsmParse.addSuffix(currentTransition.toState(), tmp, currentTransition.with(), currentTransition.toString(), currentTransition.toPos());
                    newFsmParse.setAgreement(currentTransition.with());
                    fsmParse.add(newFsmParse);
                }
            }
        }
    }

    /**
     * The addNewParsesFromCurrentParse method initially gets the final suffixes from input currentFsmParse called as currentState,
     * and by using the currentState information it gets the currentSurfaceForm. Then loops through each currentState's transition.
     * If the currentTransition is possible, it makes the transition
     *
     * @param currentFsmParse FsmParse type input.
     * @param fsmParse        an ArrayList of FsmParse.
     * @param surfaceForm     String to use during transition.
     * @param root            TxtWord used to make transition.
     */
    private void addNewParsesFromCurrentParse(FsmParse currentFsmParse, Queue<FsmParse> fsmParse, String surfaceForm, TxtWord root) {
        State currentState = currentFsmParse.getFinalSuffix();
        String currentSurfaceForm = currentFsmParse.getSurfaceForm();
        for (Transition currentTransition : finiteStateMachine.getTransitions(currentState)) {
            if (currentTransition.transitionPossible(currentFsmParse.getSurfaceForm(), surfaceForm) && currentTransition.transitionPossible(currentFsmParse) && (currentSurfaceForm.compareTo(root.getName()) != 0 || (currentSurfaceForm.compareTo(root.getName()) == 0 && currentTransition.transitionPossible(root, currentState)))) {
                String tmp = currentTransition.makeTransition(root, currentSurfaceForm, currentFsmParse.getStartState());
                if ((tmp.length() < surfaceForm.length() && isPossibleSubstring(tmp, surfaceForm, root)) || (tmp.length() == surfaceForm.length() && (root.lastIdropsDuringSuffixation() || (tmp.equalsIgnoreCase(surfaceForm))))) {
                    FsmParse newFsmParse = currentFsmParse.clone();
                    newFsmParse.addSuffix(currentTransition.toState(), tmp, currentTransition.with(), currentTransition.toString(), currentTransition.toPos());
                    newFsmParse.setAgreement(currentTransition.with());
                    fsmParse.add(newFsmParse);
                }
            }
        }
    }

    /**
     * The parseExists method is used to check the existence of the parse.
     *
     * @param fsmParse    an ArrayList of FsmParse
     * @param surfaceForm String to use during transition.
     * @return true when the currentState is end state and input surfaceForm id equal to currentSurfaceForm, otherwise false.
     */
    private boolean parseExists(ArrayList<FsmParse> fsmParse, String surfaceForm) {
        FsmParse currentFsmParse;
        TxtWord root;
        State currentState;
        String currentSurfaceForm;
        LinkedList<FsmParse> parseQueue = new LinkedList<>(fsmParse);
        while (!parseQueue.isEmpty()) {
            currentFsmParse = parseQueue.remove();
            root = (TxtWord) currentFsmParse.getWord();
            currentState = currentFsmParse.getFinalSuffix();
            currentSurfaceForm = currentFsmParse.getSurfaceForm();
            if (currentState.isEndState() && currentSurfaceForm.compareTo(surfaceForm) == 0) {
                return true;
            }
            addNewParsesFromCurrentParse(currentFsmParse, parseQueue, surfaceForm, root);
        }
        return false;
    }

    /**
     * The parseWord method is used to parse a given fsmParse. It simply adds new parses to the current parse by
     * using addNewParsesFromCurrentParse method.
     *
     * @param fsmParse    an ArrayList of FsmParse
     * @param maxLength maximum length of the surfaceform.
     * @return result {@link ArrayList} which has the currentFsmParse.
     */
    private ArrayList<FsmParse> parseWord(ArrayList<FsmParse> fsmParse, int maxLength) {
        ArrayList<FsmParse> result;
        ArrayList<String> resultTransitionList = new ArrayList<>();
        FsmParse currentFsmParse;
        TxtWord root;
        State currentState;
        String currentSurfaceForm;
        String currentTransitionList;
        LinkedList<FsmParse> parseQueue = new LinkedList<>(fsmParse);
        result = new ArrayList<>();
        while (!parseQueue.isEmpty()) {
            currentFsmParse = parseQueue.remove();
            root = (TxtWord) currentFsmParse.getWord();
            currentState = currentFsmParse.getFinalSuffix();
            currentSurfaceForm = currentFsmParse.getSurfaceForm();
            if (currentState.isEndState() && currentSurfaceForm.length() <= maxLength) {
                currentTransitionList = currentSurfaceForm + " " + currentFsmParse.transitionList();
                if (!resultTransitionList.contains(currentTransitionList)) {
                    result.add(currentFsmParse);
                    currentFsmParse.constructInflectionalGroups();
                    resultTransitionList.add(currentTransitionList);
                }
            }
            addNewParsesFromCurrentParse(currentFsmParse, parseQueue, maxLength, root);
        }
        return result;
    }

    /**
     * The parseWord method is used to parse a given fsmParse. It simply adds new parses to the current parse by
     * using addNewParsesFromCurrentParse method.
     *
     * @param fsmParse    an ArrayList of FsmParse
     * @param surfaceForm String to use during transition.
     * @return result {@link ArrayList} which has the currentFsmParse.
     */
    private ArrayList<FsmParse> parseWord(ArrayList<FsmParse> fsmParse, String surfaceForm) {
        ArrayList<FsmParse> result;
        ArrayList<String> resultTransitionList = new ArrayList<>();
        FsmParse currentFsmParse;
        TxtWord root;
        State currentState;
        String currentSurfaceForm;
        String currentTransitionList;
        LinkedList<FsmParse> parseQueue = new LinkedList<>(fsmParse);
        result = new ArrayList<>();
        while (!parseQueue.isEmpty()) {
            currentFsmParse = parseQueue.remove();
            root = (TxtWord) currentFsmParse.getWord();
            currentState = currentFsmParse.getFinalSuffix();
            currentSurfaceForm = currentFsmParse.getSurfaceForm();
            if (currentState.isEndState() && currentSurfaceForm.compareTo(surfaceForm) == 0) {
                currentTransitionList = currentFsmParse.transitionList();
                if (!resultTransitionList.contains(currentTransitionList)) {
                    result.add(currentFsmParse);
                    currentFsmParse.constructInflectionalGroups();
                    resultTransitionList.add(currentTransitionList);
                }
            }
            addNewParsesFromCurrentParse(currentFsmParse, parseQueue, surfaceForm, root);
        }
        return result;
    }

    /**
     * The morphologicalAnalysis with 3 inputs is used to initialize an {@link ArrayList} and add a new FsmParse
     * with given root and state.
     *
     * @param root        TxtWord input.
     * @param surfaceForm String input to use for parsing.
     * @param state       String input.
     * @return parseWord method with newly populated FsmParse ArrayList and input surfaceForm.
     */
    public ArrayList<FsmParse> morphologicalAnalysis(TxtWord root, String surfaceForm, String state) {
        ArrayList<FsmParse> initialFsmParse;
        initialFsmParse = new ArrayList<>();
        initialFsmParse.add(new FsmParse(root, finiteStateMachine.getState(state)));
        return parseWord(initialFsmParse, surfaceForm);
    }

    /**
     * Given a set of morphological parses, this method returns all surface forms of those parses.
     * @param parseList Morphological parse list.
     * @return All distinct surface forms for a given set of morphological parses.
     */
    private HashSet<String> distinctSurfaceFormList(ArrayList<FsmParse> parseList){
        HashSet<String> items = new HashSet<>();
        for (FsmParse parse : parseList){
            items.add(parse.getSurfaceForm());
        }
        return items;
    }

    /**
     * This method generates all possible surface forms that can be generated by the morphological analyzer with the
     * current root forms from the dictionary. Since the  number of all possible surface forms is infinity in Turkish,
     * this method bounds the length of the possible surface forms. It includes only those surface forms, that can be
     * obtained by adding at most 4 characters to the root form. The method prints all distinct surface forms to an
     * output file.
     * @param outputFile Output file that will contain distinct possible surface forms.
     */
    public void outputAllParses(String outputFile){
        try {
            PrintWriter pw = new PrintWriter(outputFile);
            for (int i = 0; i < getDictionary().size(); i++){
                TxtWord word = (TxtWord) getDictionary().getWord(i);
                ArrayList<FsmParse> parseList = generateAllParses(word, word.getName().length() + 4);
                HashSet<String> possibilities = distinctSurfaceFormList(parseList);
                for (String possibility : possibilities){
                    pw.println(possibility);
                }
            }
            pw.close();
        } catch (FileNotFoundException ignored) {
        }
    }

    /**
     * The generateAllParses with 2 inputs is used to generate all parses with given root. Then it calls initializeParseListFromRoot method to initialize list with newly created ArrayList, input root,
     * and maximum length.
     *
     * @param root        TxtWord input.
     * @param maxLength Maximum length of the surface form.
     * @return parseWord method with newly populated FsmParse ArrayList and maximum length.
     */
    public ArrayList<FsmParse> generateAllParses(TxtWord root, int maxLength) {
        ArrayList<FsmParse> initialFsmParse;
        initialFsmParse = new ArrayList<>();
        if (root.isProperNoun()){
            initializeParseListFromRoot(initialFsmParse, root, true);
        }
        initializeParseListFromRoot(initialFsmParse, root, false);
        return parseWord(initialFsmParse, maxLength);
    }

    /**
     * The morphologicalAnalysis with 2 inputs is used to initialize an {@link ArrayList} and add a new FsmParse
     * with given root. Then it calls initializeParseListFromRoot method to initialize list with newly created ArrayList, input root,
     * and input surfaceForm.
     *
     * @param root        TxtWord input.
     * @param surfaceForm String input to use for parsing.
     * @return parseWord method with newly populated FsmParse ArrayList and input surfaceForm.
     */
    public ArrayList<FsmParse> morphologicalAnalysis(TxtWord root, String surfaceForm) {
        ArrayList<FsmParse> initialFsmParse;
        initialFsmParse = new ArrayList<>();
        initializeParseListFromRoot(initialFsmParse, root, isProperNoun(surfaceForm));
        return parseWord(initialFsmParse, surfaceForm);
    }

    /**
     * Replaces previous lemma in the sentence with the new lemma. Both lemma can contain multiple words.
     * @param original Original sentence to be replaced with.
     * @param previousWord Root word in the original sentence
     * @param newWord New word to be replaced.
     * @return Newly generated sentence by replacing the previous word in the original sentence with the new word.
     */
    public Sentence replaceWord(Sentence original, String previousWord, String newWord){
        int i;
        String[] previousWordSplitted = null, newWordSplitted = null;
        Sentence result = new Sentence();
        String replacedWord = null, lastWord, newRootWord;
        boolean previousWordMultiple = previousWord.contains(" ");
        boolean newWordMultiple = newWord.contains(" ");
        if (previousWordMultiple){
            previousWordSplitted = previousWord.split(" ");
            lastWord = previousWordSplitted[previousWordSplitted.length - 1];
        } else {
            lastWord = previousWord;
        }
        if (newWordMultiple){
            newWordSplitted = newWord.split(" ");
            newRootWord = newWordSplitted[newWordSplitted.length - 1];
        } else {
            newRootWord = newWord;
        }
        TxtWord newRootTxtWord = (TxtWord) dictionary.getWord(newRootWord);
        FsmParseList[] parseList = morphologicalAnalysis(original);
        for (i = 0; i < parseList.length; i++){
            boolean replaced = false;
            for (int j = 0; j < parseList[i].size(); j++){
                if (parseList[i].getFsmParse(j).root.getName().equals(lastWord) && newRootTxtWord != null){
                    replaced = true;
                    replacedWord = parseList[i].getFsmParse(j).replaceRootWord(newRootTxtWord);
                }
            }
            if (replaced && replacedWord != null){
                if (previousWordMultiple){
                    for (int k = 0; k < i - previousWordSplitted.length + 1; k++){
                        result.addWord(original.getWord(k));
                    }
                }
                if (newWordMultiple){
                    for (int k = 0; k < newWordSplitted.length - 1; k++){
                        if (result.wordCount() == 0){
                            result.addWord(new Word((newWordSplitted[k].charAt(0) + "").toUpperCase(new Locale("tr")) + newWordSplitted[k].substring(1)));
                        } else {
                            result.addWord(new Word(newWordSplitted[k]));
                        }
                    }
                }
                if (result.wordCount() == 0){
                    replacedWord = (replacedWord.charAt(0) + "").toUpperCase(new Locale("tr")) + replacedWord.substring(1);
                }
                result.addWord(new Word(replacedWord));
                if (previousWordMultiple){
                    i++;
                    break;
                }
            } else {
                if (!previousWordMultiple){
                    result.addWord(original.getWord(i));
                }
            }
        }
        if (previousWordMultiple){
            for (; i < parseList.length; i++){
                result.addWord(original.getWord(i));
            }
        }
        return result;
    }

    /**
     * The analysisExists method checks several cases. If the given surfaceForm is a punctuation or double then it
     * returns true. If it is not a root word, then it initializes the parse list and returns the parseExists method with
     * this newly initialized list and surfaceForm.
     *
     * @param rootWord    TxtWord root.
     * @param surfaceForm String input.
     * @param isProper    boolean variable indicates a word is proper or not.
     * @return true if surfaceForm is punctuation or double, otherwise returns parseExist method with given surfaceForm.
     */
    private boolean analysisExists(TxtWord rootWord, String surfaceForm, boolean isProper) {
        ArrayList<FsmParse> initialFsmParse;
        if (Word.isPunctuation(surfaceForm)) {
            return true;
        }
        if (isDouble(surfaceForm)) {
            return true;
        }
        if (rootWord != null) {
            initialFsmParse = new ArrayList<>();
            initializeParseListFromRoot(initialFsmParse, rootWord, isProper);
        } else {
            initialFsmParse = initializeParseListFromSurfaceForm(surfaceForm, isProper);
        }
        return parseExists(initialFsmParse, surfaceForm);
    }

    /**
     * The analysis method is used by the morphologicalAnalysis method. It gets String surfaceForm as an input and checks
     * its type such as punctuation, number or compares with the regex for date, fraction, percent, time, range, hashtag,
     * and mail or checks its variable type as integer or double. After finding the right case for given surfaceForm, it calls
     * constructInflectionalGroups method which creates sub-word units.
     *
     * @param surfaceForm String to analyse.
     * @param isProper    is used to indicate the proper words.
     * @return ArrayList type initialFsmParse which holds the analyses.
     */
    private ArrayList<FsmParse> analysis(String surfaceForm, boolean isProper) {
        ArrayList<FsmParse> initialFsmParse;
        FsmParse fsmParse;
        if (Word.isPunctuation(surfaceForm) && !surfaceForm.equals("%")) {
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("Punctuation"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (isNumber(surfaceForm)) {
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("CardinalRoot"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (patternMatches("\\d+/\\d+", surfaceForm)) {
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("FractionRoot"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            fsmParse = new FsmParse(surfaceForm, new State(("DateRoot"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (isDate(surfaceForm)) {
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("DateRoot"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (patternMatches("\\d+\\\\/\\d+", surfaceForm)) {
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("FractionRoot"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (surfaceForm.equals("%") || isPercent(surfaceForm)) {
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("PercentRoot"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (isTime(surfaceForm)) {
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("TimeRoot"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (isRange(surfaceForm)) {
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("RangeRoot"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (surfaceForm.startsWith("#")) {
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("Hashtag"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (surfaceForm.contains("@")) {
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("Email"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (surfaceForm.endsWith(".") && isInteger(surfaceForm.substring(0, surfaceForm.length() - 1))) {
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(Integer.parseInt(surfaceForm.substring(0, surfaceForm.length() - 1)), finiteStateMachine.getState("OrdinalRoot"));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (isInteger(surfaceForm)) {
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(Integer.parseInt(surfaceForm), finiteStateMachine.getState("CardinalRoot"));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (isDouble(surfaceForm)) {
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(Double.parseDouble(surfaceForm), finiteStateMachine.getState("RealRoot"));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        initialFsmParse = initializeParseListFromSurfaceForm(surfaceForm, isProper);
        return parseWord(initialFsmParse, surfaceForm);
    }

    /**
     * This method uses cache idea to speed up pattern matching in Fsm. mostUsedPatterns stores the compiled forms of
     * the previously used patterns. When Fsm tries to match a string to a pattern, first we check if it exists in
     * mostUsedPatterns. If it exists, we directly use the compiled pattern to match the string. Otherwise, new pattern
     * is compiled and put in the mostUsedPatterns.
     * @param expr Pattern to check
     * @param value String to match the pattern
     * @return True if the string matches the pattern, false otherwise.
     */
    private boolean patternMatches(String expr, String value){
        Pattern p = mostUsedPatterns.get(expr);
        if (p == null){
            p = Pattern.compile(expr);
            mostUsedPatterns.put(expr, p);
        }
        return p.matcher(value).matches();
    }

    /**
     * The isProperNoun method takes surfaceForm String as input and checks its each char whether they are in the range
     * of letters between A to Z or one of the Turkish letters such as İ, Ü, Ğ, Ş, Ç, and Ö.
     *
     * @param surfaceForm String to check for proper noun.
     * @return false if surfaceForm is null or length of 0, return true if it is a letter.
     */
    public boolean isProperNoun(String surfaceForm) {
        if (surfaceForm == null || surfaceForm.isEmpty()) {
            return false;
        }
        return (surfaceForm.charAt(0) >= 'A' && surfaceForm.charAt(0) <= 'Z') || (surfaceForm.charAt(0) == 'İ') || (surfaceForm.charAt(0) == 'Ü') || (surfaceForm.charAt(0) == 'Ğ') || (surfaceForm.charAt(0) == 'Ş') || (surfaceForm.charAt(0) == 'Ç') || (surfaceForm.charAt(0) == 'Ö'); // İ, Ü, Ğ, Ş, Ç, Ö
    }

    /**
     * The isCode method takes surfaceForm String as input and checks if it consists of both letters and numbers
     *
     * @param surfaceForm String to check for code-like word.
     * @return True if it is a code-like word, return false otherwise.
     */
    private boolean isCode(String surfaceForm) {
        if (surfaceForm == null || surfaceForm.isEmpty()) {
            return false;
        }
        return patternMatches(".*[0-9].*", surfaceForm) && patternMatches(".*[a-zA-ZçöğüşıÇÖĞÜŞİ].*", surfaceForm);
    }

    /**
     * Identifies a possible new root word for a given surface form. It also adds the new root form to the dictionary
     * for further usage. The method first searches the suffix trie for the reverse string of the surface form. This
     * way, it can identify if the word has a suffix that is in the most frequently used suffix list. Since a word can
     * have multiple possible suffixes, the method identifies the longest suffix and returns the substring of the
     * surface form tht does not contain the suffix. Let say the word is 'googlelaştırdık', it will identify 'tık' as
     * a suffix and will return 'googlelaştır' as a possible root form. Another example will be 'homelesslerimizle', it
     * will identify 'lerimizle' as suffix and will return 'homeless' as a possible root form. If the root word ends
     * with 'ğ', it is replacesd with 'k'. 'morfolojikliğini' will return 'morfolojikliğ' then which will be replaced
     * with 'morfolojiklik'.
     * @param surfaceForm Surface form for which we will identify a possible new root form.
     * @return Possible new root form.
     */
    private ArrayList<TxtWord> rootOfPossiblyNewWord(String surfaceForm){
        HashSet<Word> words = suffixTrie.getWordsWithPrefix(reverseString(surfaceForm));
        ArrayList<TxtWord> candidateList = new ArrayList<>();
        for (Word word : words){
            String candidateWord = surfaceForm.substring(0, surfaceForm.length() - word.getName().length());
            TxtWord newWord;
            if (candidateWord.endsWith("ğ")){
                candidateWord = candidateWord.substring(0, candidateWord.length() - 1) + "k";
                newWord = new TxtWord(candidateWord, "CL_ISIM");
                newWord.addFlag("IS_SD");
            } else {
                newWord = new TxtWord(candidateWord, "CL_ISIM");
                newWord.addFlag("CL_FIIL");
            }
            candidateList.add(newWord);
            dictionaryTrie.addWord(candidateWord, newWord);
        }
        return candidateList;
    }

    /**
     * The robustMorphologicalAnalysis is used to analyse surfaceForm String. First it gets the currentParse of the surfaceForm
     * then, if the size of the currentParse is 0, and given surfaceForm is a proper noun, it adds the surfaceForm
     * whose state name is ProperRoot to an {@link ArrayList}, if it is a code-like word, it adds the surfaceForm
     * whose state name is CodeRoot to the {@link ArrayList} and if it is neither, it adds the surfaceForm
     * whose state name is NominalRoot to the {@link ArrayList}.
     *
     * @param surfaceForm String to analyse.
     * @return FsmParseList type currentParse which holds morphological analysis of the surfaceForm.
     */
    public FsmParseList robustMorphologicalAnalysis(String surfaceForm) {
        ArrayList<FsmParse> fsmParse;
        FsmParseList currentParse;
        if (surfaceForm == null || surfaceForm.isEmpty()) {
            return new FsmParseList(new ArrayList<>());
        }
        currentParse = morphologicalAnalysis(surfaceForm);
        if (currentParse.size() == 0) {
            fsmParse = new ArrayList<>(1);
            if (isProperNoun(surfaceForm)) {
                fsmParse.add(new FsmParse(surfaceForm, finiteStateMachine.getState("ProperRoot")));
            }
            if (isCode(surfaceForm)) {
                fsmParse.add(new FsmParse(surfaceForm, finiteStateMachine.getState("CodeRoot")));
            }
            ArrayList<TxtWord> newCandidateList = rootOfPossiblyNewWord(surfaceForm);
            if (!newCandidateList.isEmpty()){
                for (TxtWord word : newCandidateList){
                    fsmParse.add(new FsmParse(word, finiteStateMachine.getState("VerbalRoot")));
                    fsmParse.add(new FsmParse(word, finiteStateMachine.getState("NominalRoot")));
                }
            }
            fsmParse.add(new FsmParse(surfaceForm, finiteStateMachine.getState("NominalRoot")));
            return new FsmParseList(parseWord(fsmParse, surfaceForm));
        } else {
            return currentParse;
        }
    }

    /**
     * The morphologicalAnalysis is used for debug purposes.
     *
     * @param sentence  to get word from.
     * @return FsmParseList type result.
     */
    public FsmParseList[] morphologicalAnalysis(Sentence sentence) {
        FsmParseList wordFsmParseList;
        FsmParseList[] result = new FsmParseList[sentence.wordCount()];
        for (int i = 0; i < sentence.wordCount(); i++) {
            String originalForm = sentence.getWord(i).getName();
            String spellCorrectedForm = dictionary.getCorrectForm(originalForm);
            if (spellCorrectedForm == null){
                spellCorrectedForm = originalForm;
            }
            wordFsmParseList = morphologicalAnalysis(spellCorrectedForm);
            result[i] = wordFsmParseList;
        }
        return result;
    }


    /**
     * The robustMorphologicalAnalysis method takes just one argument as an input. It gets the name of the words from
     * input sentence then calls robustMorphologicalAnalysis with surfaceForm.
     *
     * @param sentence Sentence type input used to get surfaceForm.
     * @return FsmParseList array which holds the result of the analysis.
     */
    public FsmParseList[] robustMorphologicalAnalysis(Sentence sentence) {
        FsmParseList fsmParseList;
        FsmParseList[] result = new FsmParseList[sentence.wordCount()];
        for (int i = 0; i < sentence.wordCount(); i++) {
            String originalForm = sentence.getWord(i).getName();
            String spellCorrectedForm = dictionary.getCorrectForm(originalForm);
            if (spellCorrectedForm == null){
                spellCorrectedForm = originalForm;
            }
            fsmParseList = robustMorphologicalAnalysis(spellCorrectedForm);
            result[i] = fsmParseList;
        }
        return result;
    }

    /**
     * The isInteger method compares input surfaceForm with regex [-+]?\d+ and returns the result.
     * Supports positive integer checks only.
     *
     * @param surfaceForm String to check.
     * @return true if surfaceForm matches with the regex.
     */
    private boolean isInteger(String surfaceForm) {
        if (!patternMatches("[-+]?\\d+", surfaceForm))
            return false;
        int len = surfaceForm.length();
        if (len < 10) {
            return true;
        } else {
            if (len > 10) {
                return false;
            } else {
                return surfaceForm.compareTo("2147483647") <= 0;
            }
        }
    }

    /**
     * The isDouble method compares input surfaceForm with the regex ([-+]?\d+\.\d+)|(\d*\.\d+) and returns the result.
     *
     * @param surfaceForm String to check.
     * @return true if surfaceForm matches with the regex.
     */
    private boolean isDouble(String surfaceForm) {
        return patternMatches("([-+]?\\d+\\.\\d+)|(\\d*\\.\\d+)", surfaceForm);
    }

    /**
     * The isNumber method compares input surfaceForm with the array of written numbers and returns the result.
     *
     * @param surfaceForm String to check.
     * @return true if surfaceForm matches with the regex.
     */
    private boolean isNumber(String surfaceForm) {
        boolean found;
        int count = 0;
        String[] numbers = {"bir", "iki", "üç", "dört", "beş", "altı", "yedi", "sekiz", "dokuz",
                "on", "yirmi", "otuz", "kırk", "elli", "altmış", "yetmiş", "seksen", "doksan",
                "yüz", "bin", "milyon", "milyar", "trilyon", "katrilyon"};
        String word = surfaceForm;
        while (!word.isEmpty()) {
            found = false;
            for (String number : numbers) {
                if (word.startsWith(number)) {
                    found = true;
                    count++;
                    word = word.substring(number.length());
                    break;
                }
            }
            if (!found) {
                break;
            }
        }
        return word.isEmpty() && count > 1;
    }

    /**
     * Checks if a given surface form matches to a percent value. It should be something like %4, %45, %4.3 or %56.786
     * @param surfaceForm Surface form to be checked.
     * @return True if the surface form is in percent form
     */
    private boolean isPercent(String surfaceForm){
        return patternMatches("%(\\d\\d|\\d)", surfaceForm) || patternMatches("%(\\d\\d|\\d)\\.\\d+", surfaceForm);
    }

    /**
     * Checks if a given surface form matches to a time form. It should be something like 3:34, 12:56 etc.
     * @param surfaceForm Surface form to be checked.
     * @return True if the surface form is in time form
     */
    private boolean isTime(String surfaceForm) {
        return patternMatches("(\\d\\d|\\d):(\\d\\d|\\d):(\\d\\d|\\d)", surfaceForm) || patternMatches("(\\d\\d|\\d):(\\d\\d|\\d)", surfaceForm);
    }

    /**
     * Checks if a given surface form matches to a range form. It should be something like 123-1400 or 12:34-15:78 or
     * 3.45-4.67.
     * @param surfaceForm Surface form to be checked.
     * @return True if the surface form is in range form
     */
    private boolean isRange(String surfaceForm) {
        return patternMatches("\\d+-\\d+", surfaceForm) || patternMatches("(\\d\\d|\\d):(\\d\\d|\\d)-(\\d\\d|\\d):(\\d\\d|\\d)", surfaceForm) || patternMatches("(\\d\\d|\\d)\\.(\\d\\d|\\d)-(\\d\\d|\\d)\\.(\\d\\d|\\d)", surfaceForm);
    }

    /**
     * Checks if a given surface form matches to a date form. It should be something like 3/10/2023 or 2.3.2012
     * @param surfaceForm Surface form to be checked.
     * @return True if the surface form is in date form
     */
    private boolean isDate(String surfaceForm) {
        return patternMatches("(\\d\\d|\\d)/(\\d\\d|\\d)/\\d+", surfaceForm) || patternMatches("(\\d\\d|\\d)\\.(\\d\\d|\\d)\\.\\d+", surfaceForm);
    }

    /**
     * The morphologicalAnalysis method is used to analyse a FsmParseList by comparing with the regex.
     * It creates an {@link ArrayList} fsmParse to hold the result of the analysis method. For each surfaceForm input,
     * it gets a substring and considers it as a possibleRoot. Then compares with the regex.
     * <p>
     * If the surfaceForm input string matches with Turkish chars like Ç, Ş, İ, Ü, Ö, it adds the surfaceForm to Trie with IS_OA tag.
     * If the possibleRoot contains /, then it is added to the Trie with IS_KESIR tag.
     * If the possibleRoot contains \d\d|\d)/(\d\d|\d)/\d+, then it is added to the Trie with IS_DATE tag.
     * If the possibleRoot contains \\d\d|\d, then it is added to the Trie with IS_PERCENT tag.
     * If the possibleRoot contains \d\d|\d):(\d\d|\d):(\d\d|\d), then it is added to the Trie with IS_ZAMAN tag.
     * If the possibleRoot contains \d+-\d+, then it is added to the Trie with IS_RANGE tag.
     * If the possibleRoot is an Integer, then it is added to the Trie with IS_SAYI tag.
     * If the possibleRoot is a Double, then it is added to the Trie with IS_REELSAYI tag.
     *
     * @param surfaceForm String to analyse.
     * @return fsmParseList which holds the analysis.
     */
    public FsmParseList morphologicalAnalysis(String surfaceForm) {
        FsmParseList fsmParseList;
        TxtWord newWord;
        String lowerCased = surfaceForm.toLowerCase(new Locale("tr"));
        String possibleRootLowerCased = "", pronunciation = "";
        boolean isRootReplaced = false;
        if (parsedSurfaceForms != null && parsedSurfaceForms.containsKey(lowerCased) && !isInteger(surfaceForm) && !isDouble(surfaceForm) && !isPercent(surfaceForm) && !isTime(surfaceForm) && !isRange(surfaceForm) && !isDate(surfaceForm)){
            ArrayList<FsmParse> parses = new ArrayList<>();
            parses.add(new FsmParse(new Word(parsedSurfaceForms.get(lowerCased))));
            return new FsmParseList(parses);
        }
        if (cache != null && cache.contains(surfaceForm)) {
            return cache.get(surfaceForm);
        }
        if (patternMatches("(\\w|Ç|Ş|İ|Ü|Ö)\\.",surfaceForm)) {
            dictionaryTrie.addWord(lowerCased, new TxtWord(lowerCased, "IS_OA"));
        }
        ArrayList<FsmParse> defaultFsmParse = analysis(lowerCased, isProperNoun(surfaceForm));
        if (!defaultFsmParse.isEmpty()) {
            fsmParseList = new FsmParseList(defaultFsmParse);
            if (cache != null) {
                cache.add(surfaceForm, fsmParseList);
            }
            return fsmParseList;
        }
        ArrayList<FsmParse> fsmParse = new ArrayList<>();
        if (surfaceForm.contains("'")) {
            String possibleRoot = surfaceForm.substring(0, surfaceForm.indexOf('\''));
            if (!possibleRoot.isEmpty()) {
                if (possibleRoot.contains("/") || possibleRoot.contains("\\/")) {
                    dictionaryTrie.addWord(possibleRoot, new TxtWord(possibleRoot, "IS_KESIR"));
                    fsmParse = analysis(lowerCased, isProperNoun(surfaceForm));
                } else {
                    if (isDate(possibleRoot)) {
                        dictionaryTrie.addWord(possibleRoot, new TxtWord(possibleRoot, "IS_DATE"));
                        fsmParse = analysis(lowerCased, isProperNoun(surfaceForm));
                    } else {
                        if (patternMatches("\\d+/\\d+", possibleRoot)) {
                            dictionaryTrie.addWord(possibleRoot, new TxtWord(possibleRoot, "IS_KESIR"));
                            fsmParse = analysis(lowerCased, isProperNoun(surfaceForm));
                        } else {
                            if (isPercent(possibleRoot)) {
                                dictionaryTrie.addWord(possibleRoot, new TxtWord(possibleRoot, "IS_PERCENT"));
                                fsmParse = analysis(lowerCased, isProperNoun(surfaceForm));
                            } else {
                                if (isTime(surfaceForm)) {
                                    dictionaryTrie.addWord(possibleRoot, new TxtWord(possibleRoot, "IS_ZAMAN"));
                                    fsmParse = analysis(lowerCased, isProperNoun(surfaceForm));
                                } else {
                                    if (isRange(surfaceForm)) {
                                        dictionaryTrie.addWord(possibleRoot, new TxtWord(possibleRoot, "IS_RANGE"));
                                        fsmParse = analysis(lowerCased, isProperNoun(surfaceForm));
                                    } else {
                                        if (isInteger(possibleRoot)) {
                                            dictionaryTrie.addWord(possibleRoot, new TxtWord(possibleRoot, "IS_SAYI"));
                                            fsmParse = analysis(lowerCased, isProperNoun(surfaceForm));
                                        } else {
                                            if (isDouble(possibleRoot)) {
                                                dictionaryTrie.addWord(possibleRoot, new TxtWord(possibleRoot, "IS_REELSAYI"));
                                                fsmParse = analysis(lowerCased, isProperNoun(surfaceForm));
                                            } else {
                                                if (Word.isCapital(possibleRoot) || "QXW".contains(possibleRoot.substring(0, 1))) {
                                                    possibleRootLowerCased = possibleRoot.toLowerCase(new Locale("tr"));
                                                    if (pronunciations.containsKey(possibleRootLowerCased)){
                                                        isRootReplaced = true;
                                                        pronunciation = pronunciations.get(possibleRootLowerCased);
                                                        if (dictionary.getWord(pronunciation) != null) {
                                                            ((TxtWord) dictionary.getWord(pronunciation)).addFlag("IS_OA");
                                                        } else {
                                                            newWord = new TxtWord(pronunciation, "IS_OA");
                                                            dictionaryTrie.addWord(pronunciation, newWord);
                                                        }
                                                        String replacedWord = pronunciation + lowerCased.substring(possibleRootLowerCased.length());
                                                        fsmParse = analysis(replacedWord, isProperNoun(surfaceForm));
                                                    } else {
                                                        if (dictionary.getWord(possibleRootLowerCased) != null) {
                                                            ((TxtWord) dictionary.getWord(possibleRootLowerCased)).addFlag("IS_OA");
                                                        } else {
                                                            newWord = new TxtWord(possibleRootLowerCased, "IS_OA");
                                                            dictionaryTrie.addWord(possibleRootLowerCased, newWord);
                                                        }
                                                        fsmParse = analysis(lowerCased, isProperNoun(surfaceForm));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (isRootReplaced){
            for (FsmParse parse: fsmParse){
                parse.restoreOriginalForm(possibleRootLowerCased, pronunciation);
            }
        }
        fsmParseList = new FsmParseList(fsmParse);
        if (cache != null && fsmParseList.size() > 0) {
            cache.add(surfaceForm, fsmParseList);
        }
        return fsmParseList;
    }

    /**
     * The morphologicalAnalysisExists method calls analysisExists to check the existence of the analysis with given
     * root and surfaceForm.
     *
     * @param surfaceForm String to check.
     * @param rootWord    TxtWord input root.
     * @return true an analysis exists, otherwise return false.
     */
    public boolean morphologicalAnalysisExists(TxtWord rootWord, String surfaceForm) {
        return analysisExists(rootWord, surfaceForm.toLowerCase(new Locale("tr")), true);
    }

}