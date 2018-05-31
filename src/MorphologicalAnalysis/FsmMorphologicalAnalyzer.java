package MorphologicalAnalysis;

import Corpus.Sentence;
import DataStructure.Cache.LRUCache;
import Dictionary.Trie.Trie;
import Dictionary.*;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.*;

public class FsmMorphologicalAnalyzer{

    private Trie dictionaryTrie;
    private ArrayList<State> states;
    private static final int MAX_DISTANCE = 2;
    private TxtDictionary dictionary;
    private LRUCache<String, FsmParseList> cache;

    public FsmMorphologicalAnalyzer(){
        this("turkish_finite_state_machine.xml", new TxtDictionary("Data/Dictionary/turkish_dictionary.txt", new TurkishWordComparator()), 100000);
    }

    public FsmMorphologicalAnalyzer(int cacheSize){
        this("turkish_finite_state_machine.xml", new TxtDictionary("Data/Dictionary/turkish_dictionary.txt", new TurkishWordComparator()), cacheSize);
    }

    public FsmMorphologicalAnalyzer(String fileName, TxtDictionary dictionary, int cacheSize){
        this.dictionary = dictionary;
        readFsm(fileName);
        dictionaryTrie = prepareTrie(dictionary);
        cache = new LRUCache<>(cacheSize);
    }

    public FsmMorphologicalAnalyzer(String fileName, TxtDictionary dictionary){
        this(fileName, dictionary, 100000);
    }

    private void readFsm(String fileName){
        int i;
        boolean startState, endState;
        NodeList stateList;
        Node stateNode, rootNode, transitionNode, withNode;
        State state, toState;
        String stateName, withName, originalPos, rootToPos, toPos;
        NamedNodeMap attributes;
        DOMParser parser = new DOMParser();
        Document doc;
        try {
            parser.parse(fileName);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        doc = parser.getDocument();
        stateList = doc.getElementsByTagName("state");
        states = new ArrayList<>();
        for (i = 0; i < stateList.getLength(); i++){
            stateNode = stateList.item(i);
            attributes = stateNode.getAttributes();
            stateName = attributes.getNamedItem("name").getNodeValue();
            startState = attributes.getNamedItem("start").getNodeValue().equalsIgnoreCase("yes");
            endState = attributes.getNamedItem("end").getNodeValue().equalsIgnoreCase("yes");
            if (startState){
                originalPos = attributes.getNamedItem("originalpos").getNodeValue();
                states.add(new State(stateName, true, endState, originalPos));
            } else {
                states.add(new State(stateName, false, endState));
            }
        }
        rootNode = doc.getFirstChild();
        stateNode = rootNode.getFirstChild();
        while (stateNode != null){
            if (stateNode.hasAttributes()){
                attributes = stateNode.getAttributes();
                stateName = attributes.getNamedItem("name").getNodeValue();
                state = getState(stateName);
                transitionNode = stateNode.getFirstChild();
                while (transitionNode != null){
                    if (transitionNode.hasAttributes()){
                        attributes = transitionNode.getAttributes();
                        stateName = attributes.getNamedItem("name").getNodeValue();
                        if (attributes.getNamedItem("transitionname") != null){
                            withName = attributes.getNamedItem("transitionname").getNodeValue();
                        } else {
                            withName = null;
                        }
                        if (attributes.getNamedItem("topos") != null){
                            rootToPos = attributes.getNamedItem("topos").getNodeValue();
                        } else {
                            rootToPos = null;
                        }
                        toState = getState(stateName);
                        if (toState != null){
                            withNode = transitionNode.getFirstChild();
                            while (withNode != null){
                                if (withNode.getFirstChild() != null){
                                    if (withNode.hasAttributes()){
                                        attributes = withNode.getAttributes();
                                        withName = attributes.getNamedItem("name").getNodeValue();
                                        if (attributes.getNamedItem("toPos") != null){
                                            toPos = attributes.getNamedItem("topos").getNodeValue();
                                        } else {
                                            toPos = null;
                                        }
                                    } else {
                                        toPos = null;
                                    }
                                    if (toPos == null){
                                        if (rootToPos == null){
                                            state.addTransition(toState, withNode.getFirstChild().getNodeValue(), withName);
                                        } else {
                                            state.addTransition(toState, withNode.getFirstChild().getNodeValue(), withName, rootToPos);
                                        }
                                    } else {
                                        state.addTransition(toState, withNode.getFirstChild().getNodeValue(), withName, toPos);
                                    }
                                }
                                withNode = withNode.getNextSibling();
                            }
                        } else {
                            System.out.println("From state " + state.getName() + " to state " + stateName + " does not exist");
                        }
                    }
                    transitionNode = transitionNode.getNextSibling();
                }
            }
            stateNode = stateNode.getNextSibling();
        }
    }

    private void addWordWhenRootSoften(Trie trie, char last, String root, TxtWord word){
        switch (last){
            case 'p':
                trie.addWord(root + 'b', word);
                break;
            case 'ç':
                trie.addWord(root + 'c', word);
                break;
            case 't':
                trie.addWord(root + 'd', word);
                break;
            case 'k':
            case 'g':
                trie.addWord(root + 'ğ', word);
                break;
        }
    }

    private Trie prepareTrie(TxtDictionary currentDictionary){
        Trie result = new Trie();
        String root, rootWithoutLast, rootWithoutLastTwo;
        char last;
        for (int i = 0; i < currentDictionary.size(); i++){
            TxtWord word = (TxtWord) currentDictionary.getWord(i);
            root = word.getName();
            if (root.equals("ben")){
                result.addWord("bana", word);
            }
            rootWithoutLast = root.substring(0, root.length() - 1);
            if (root.length() > 1){
                rootWithoutLastTwo = root.substring(0, root.length() - 2);
            } else {
                rootWithoutLastTwo = "";
            }
            last = root.charAt(root.length() - 1);
            result.addWord(root, word);
            if (word.lastIdropsDuringSuffixation() || word.lastIdropsDuringPassiveSuffixation()){
                if (word.rootSoftenDuringSuffixation()){
                    addWordWhenRootSoften(result, last, rootWithoutLastTwo, word);
                } else {
                    result.addWord(rootWithoutLastTwo + last, word);
                }
            }
            if (word.isPortmanteauEndingWithSI()){
                result.addWord(rootWithoutLastTwo, word);
            }
            if (word.rootSoftenDuringSuffixation()){
                addWordWhenRootSoften(result, last, rootWithoutLast, word);
            }
            if (word.isPortmanteau()){
                result.addWord(rootWithoutLast, word);
            }
            if (word.vowelEChangesToIDuringYSuffixation() || word.vowelAChangesToIDuringYSuffixation()){
                switch (last){
                    case 'e':
                        result.addWord(rootWithoutLast, word);
                        break;
                    case 'a':
                        result.addWord(rootWithoutLast, word);
                        break;
                }
            }
            if (word.endingKChangesIntoG()){
                result.addWord(rootWithoutLast + 'g', word);
            }
        }
        return result;
    }

    public HashSet<String> getPossibleWords(MorphologicalParse morphologicalParse, MetamorphicParse parse){
        boolean isRootVerb = morphologicalParse.getRootPos().equals("VERB");
        boolean containsVerb = morphologicalParse.containsTag(MorphologicalTag.VERB);
        Transition transition, verbTransition = new Transition("mAk");
        TxtWord compoundWord, currentRoot;
        HashSet<String> result = new HashSet<>();
        if (parse == null || parse.getWord() == null){
            return result;
        }
        String verbWord, pluralWord, currentWord = parse.getWord().getName();
        int pluralIndex = -1;
        compoundWord = dictionaryTrie.getCompoundWordStartingWith(currentWord);
        if (!isRootVerb){
            if (compoundWord != null && compoundWord.getName().length() - currentWord.length() < 3){
                result.add(compoundWord.getName());
            }
            result.add(currentWord);
        }
        currentRoot = (TxtWord) dictionary.getWord(parse.getWord().getName());
        if (currentRoot == null && compoundWord != null){
            currentRoot = compoundWord;
        }
        if (currentRoot != null){
            if (isRootVerb){
                verbWord = verbTransition.makeTransition(currentRoot, currentWord);
                result.add(verbWord);
            }
            pluralWord = null;
            for (int i = 1; i < parse.size(); i++){
                transition = new Transition(null, parse.getMetaMorpheme(i), null);
                if (parse.getMetaMorpheme(i).equals("lAr")){
                    pluralWord = currentWord;
                    pluralIndex = i + 1;
                }
                currentWord = transition.makeTransition(currentRoot, currentWord);
                result.add(currentWord);
                if (containsVerb){
                    verbWord = verbTransition.makeTransition(currentRoot, currentWord);
                    result.add(verbWord);
                }
            }
            if (pluralWord != null){
                currentWord = pluralWord;
                for (int i = pluralIndex; i < parse.size(); i++){
                    transition = new Transition(null, parse.getMetaMorpheme(i), null);
                    currentWord = transition.makeTransition(currentRoot, currentWord);
                    result.add(currentWord);
                    if (containsVerb){
                        verbWord = verbTransition.makeTransition(currentRoot, currentWord);
                        result.add(verbWord);
                    }
                }
            }
        }
        return result;
    }

    public boolean isValidTransition(String transition){
        for (State state:states){
            for (int i = 0; i < state.transitionCount(); i++){
                if (state.getTransition(i).toString() != null && state.getTransition(i).toString().equals(transition)){
                    return true;
                }
            }
        }
        return false;
    }

    public TxtDictionary getDictionary(){
        return dictionary;
    }

    private State getState(String name){
        for (State state:states){
            if (state.getName().equalsIgnoreCase(name)){
                return state;
            }
        }
        return null;
    }

    private boolean isPossibleSubstring(String shortString, String longString, TxtWord root){
        boolean rootWord = ((shortString == root.getName()) || longString == root.getName());
        int distance = 0, j, last = 1;
        for (j = 0; j < shortString.length(); j++){
            if (shortString.charAt(j) != longString.charAt(j)){
                if (j < shortString.length() - 2){
                    return false;
                }
                last = shortString.length() - j;
                distance++;
                if (distance > MAX_DISTANCE){
                    break;
                }
            }
        }
        if (rootWord && (root.getName().equals("ben") || root.lastIdropsDuringSuffixation() || root.lastIdropsDuringPassiveSuffixation())){
            return (distance <= MAX_DISTANCE);
        } else {
            if (shortString.endsWith("e") || shortString.endsWith("a") || shortString.endsWith("p") || shortString.endsWith("ç") || shortString.endsWith("t") || shortString.endsWith("k") || (rootWord && (root.rootSoftenDuringSuffixation() || root.vowelEChangesToIDuringYSuffixation() || root.vowelAChangesToIDuringYSuffixation() || root.endingKChangesIntoG()))){
                return (last != 2 && distance <= MAX_DISTANCE - 1);
            } else {
                return (distance <= MAX_DISTANCE - 2);
            }
        }
    }

    private void initializeParseList(ArrayList<FsmParse> fsmParse, TxtWord root, boolean isProper){
        FsmParse currentFsmParse;
        if (root.isPlural()){
            currentFsmParse = new FsmParse(root, getState("NominalRootPlural"));
            fsmParse.add(currentFsmParse);
        } else {
            if (root.isPortmanteauEndingWithSI()){
                currentFsmParse = new FsmParse(root, getState("NominalRootNoPossesive"));
                fsmParse.add(currentFsmParse);
            } else {
                if (root.isPortmanteau()){
                    currentFsmParse = new FsmParse(root.getName().substring(0, root.getName().length() - 1), getState("CompoundNounRoot"));
                    fsmParse.add(currentFsmParse);
                } else {
                    if (root.isHeader()){
                        currentFsmParse = new FsmParse(root, getState("HeaderRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isInterjection()){
                        currentFsmParse = new FsmParse(root, getState("InterjectionRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isDuplicate()){
                        currentFsmParse = new FsmParse(root, getState("DuplicateRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isNumeral()){
                        currentFsmParse = new FsmParse(root, getState("CardinalRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isReal()){
                        currentFsmParse = new FsmParse(root, getState("RealRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isFraction()){
                        currentFsmParse = new FsmParse(root, getState("FractionRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isDate()){
                        currentFsmParse = new FsmParse(root, getState("DateRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isPercent()){
                        currentFsmParse = new FsmParse(root, getState("PercentRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isRange()){
                        currentFsmParse = new FsmParse(root, getState("RangeRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isTime()){
                        currentFsmParse = new FsmParse(root, getState("TimeRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isOrdinal()){
                        currentFsmParse = new FsmParse(root, getState("OrdinalRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isVerb() || root.isPassive()){
                        if (!root.verbType().equalsIgnoreCase("")){
                            currentFsmParse = new FsmParse(root, getState("VerbalRoot(" + root.verbType() + ")"));
                        } else {
                            if (!root.isPassive()){
                                currentFsmParse = new FsmParse(root, getState("VerbalRoot"));
                            } else {
                                currentFsmParse = new FsmParse(root, getState("PassiveHn"));
                            }
                        }
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isPronoun()){
                        if (root.getName().equalsIgnoreCase("kendi")){
                            currentFsmParse = new FsmParse(root, getState("PronounRoot(REFLEX)"));
                            fsmParse.add(currentFsmParse);
                        }
                        if (root.getName().equalsIgnoreCase("öbür") || root.getName().equalsIgnoreCase("hep") || root.getName().equalsIgnoreCase("kimse") || root.getName().equalsIgnoreCase("hiçbiri") || root.getName().equalsIgnoreCase("birbiri") || root.getName().equalsIgnoreCase("birbirleri") || root.getName().equalsIgnoreCase("biri") || root.getName().equalsIgnoreCase("bazı")  || root.getName().equalsIgnoreCase("kimi")){
                            currentFsmParse = new FsmParse(root, getState("PronounRoot(QUANT)"));
                            fsmParse.add(currentFsmParse);
                        }
                        if (root.getName().equalsIgnoreCase("tümü") || root.getName().equalsIgnoreCase("çoğu") || root.getName().equalsIgnoreCase("hepsi")){
                            currentFsmParse = new FsmParse(root, getState("PronounRoot(QUANTPLURAL)"));
                            fsmParse.add(currentFsmParse);
                        }
                        if (root.getName().equalsIgnoreCase("o") || root.getName().equalsIgnoreCase("bu") || root.getName().equalsIgnoreCase("şu")){
                            currentFsmParse = new FsmParse(root, getState("PronounRoot(DEMONS)"));
                            fsmParse.add(currentFsmParse);
                        }
                        if (root.getName().equalsIgnoreCase("ben") || root.getName().equalsIgnoreCase("sen") || root.getName().equalsIgnoreCase("o") || root.getName().equalsIgnoreCase("biz") || root.getName().equalsIgnoreCase("siz") || root.getName().equalsIgnoreCase("onlar")){
                            currentFsmParse = new FsmParse(root, getState("PronounRoot(PERS)"));
                            fsmParse.add(currentFsmParse);
                        }
                        if (root.getName().equalsIgnoreCase("nere") || root.getName().equalsIgnoreCase("ne") || root.getName().equalsIgnoreCase("kim") || root.getName().equalsIgnoreCase("hangi")){
                            currentFsmParse = new FsmParse(root, getState("PronounRoot(QUES)"));
                            fsmParse.add(currentFsmParse);
                        }
                    }
                    if (root.isAdjective()){
                        currentFsmParse = new FsmParse(root, getState("AdjectiveRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isPureAdjective()){
                        currentFsmParse = new FsmParse(root, getState("Adjective"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isNominal()){
                        currentFsmParse = new FsmParse(root, getState("NominalRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isProperNoun() && isProper){
                        currentFsmParse = new FsmParse(root, getState("ProperRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isQuestion()){
                        currentFsmParse = new FsmParse(root, getState("QuestionRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isDeterminer()){
                        currentFsmParse = new FsmParse(root, getState("DeterminerRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isConjunction()){
                        currentFsmParse = new FsmParse(root, getState("ConjunctionRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isPostP()){
                        currentFsmParse = new FsmParse(root, getState("PostP"));
                        fsmParse.add(currentFsmParse);
                    }
                    if (root.isAdverb()){
                        currentFsmParse = new FsmParse(root, getState("AdverbRoot"));
                        fsmParse.add(currentFsmParse);
                    }
                }
            }
        }
    }

    private ArrayList<FsmParse> initializeRootList(String surfaceForm, boolean isProper){
        TxtWord root;
        ArrayList<FsmParse> initialFsmParse;
        initialFsmParse = new ArrayList<>();
        if (surfaceForm.length() == 0){
            return initialFsmParse;
        }
        HashSet<Word> words = dictionaryTrie.getWordsWithPrefix(surfaceForm);
        for (Word word : words){
            root = (TxtWord) word;
            initializeParseList(initialFsmParse, root, isProper);
        }
        return initialFsmParse;
    }

    private void addNewParsesFromCurrentParse(FsmParse currentFsmParse, ArrayList<FsmParse> fsmParse, String surfaceForm, TxtWord root){
        State currentState = currentFsmParse.getFinalSuffix();
        String currentSurfaceForm = currentFsmParse.getSurfaceForm();
        for (int i = 0; i < currentState.transitionCount(); i++){
            Transition currentTransition = currentState.getTransition(i);
            if (currentTransition.transitionPossible(currentFsmParse.getSurfaceForm(), surfaceForm) && currentTransition.transitionPossible(currentFsmParse) && (currentSurfaceForm.compareTo(root.getName()) != 0 || (currentSurfaceForm.compareTo(root.getName()) == 0 && currentTransition.transitionPossible(root, currentState)))){
                String tmp = currentTransition.makeTransition(root, currentSurfaceForm, currentFsmParse.getStartState());
                if ((tmp.length() < surfaceForm.length() && isPossibleSubstring(tmp, surfaceForm, root)) || (tmp.length() == surfaceForm.length() && (root.lastIdropsDuringSuffixation() || (tmp.equalsIgnoreCase(surfaceForm))))){
                    FsmParse newFsmParse = currentFsmParse.clone();
                    newFsmParse.addSuffix(currentTransition.toState(), tmp, currentTransition.with(), currentTransition.toString(), currentTransition.toPos());
                    newFsmParse.setAgreement(currentTransition.with());
                    fsmParse.add(newFsmParse);
                }
            }
        }
    }

    private boolean parseExists(ArrayList<FsmParse> fsmParse, String surfaceForm){
        FsmParse currentFsmParse;
        TxtWord root;
        State currentState;
        String currentSurfaceForm;
        while (fsmParse.size() > 0){
            currentFsmParse = fsmParse.remove(0);
            root = (TxtWord) currentFsmParse.getWord();
            currentState = currentFsmParse.getFinalSuffix();
            currentSurfaceForm = currentFsmParse.getSurfaceForm();
            if (currentState.isEndState() && currentSurfaceForm.compareTo(surfaceForm) == 0){
                return true;
            }
            addNewParsesFromCurrentParse(currentFsmParse, fsmParse, surfaceForm, root);
        }
        return false;
    }

    private ArrayList<FsmParse> parseWord(ArrayList<FsmParse> fsmParse, String surfaceForm){
        ArrayList<FsmParse> result;
        FsmParse currentFsmParse;
        TxtWord root;
        State currentState;
        String currentSurfaceForm;
        int i;
        boolean exists;
        result = new ArrayList<>();
        while (fsmParse.size() > 0){
            currentFsmParse = fsmParse.remove(0);
            root = (TxtWord) currentFsmParse.getWord();
            currentState = currentFsmParse.getFinalSuffix();
            currentSurfaceForm = currentFsmParse.getSurfaceForm();
            if (currentState.isEndState() && currentSurfaceForm.compareTo(surfaceForm) == 0){
                exists = false;
                for (i = 0; i < result.size(); i++){
                    if (currentFsmParse.suffixList().equalsIgnoreCase(result.get(i).suffixList())){
                        exists = true;
                        break;
                    }
                }
                if (!exists){
                    result.add(currentFsmParse);
                    currentFsmParse.constructInflectionalGroups();
                }
            }
            addNewParsesFromCurrentParse(currentFsmParse, fsmParse, surfaceForm, root);
        }
        return result;
    }

    public ArrayList<FsmParse> morphologicalAnalysis(TxtWord root, String surfaceForm, String state){
        ArrayList<FsmParse> initialFsmParse;
        initialFsmParse = new ArrayList<>();
        initialFsmParse.add(new FsmParse(root, getState(state)));
        return parseWord(initialFsmParse, surfaceForm);
    }

    public ArrayList<FsmParse> morphologicalAnalysis(TxtWord root, String surfaceForm){
        ArrayList<FsmParse> initialFsmParse;
        initialFsmParse = new ArrayList<>();
        initializeParseList(initialFsmParse, root, isProperNoun(surfaceForm));
        return parseWord(initialFsmParse, surfaceForm);
    }

    private boolean analysisExists(TxtWord rootWord, String surfaceForm, boolean isProper){
        ArrayList<FsmParse> initialFsmParse;
        if (Word.isPunctuation(surfaceForm)){
            return true;
        }
        if (isDouble(surfaceForm)){
            return true;
        }
        if (rootWord != null){
            initialFsmParse = new ArrayList<>();
            initializeParseList(initialFsmParse, rootWord, isProper);
        } else {
            initialFsmParse = initializeRootList(surfaceForm, isProper);
        }
        return parseExists(initialFsmParse, surfaceForm);
    }

    private ArrayList<FsmParse> analysis(String surfaceForm, boolean isProper){
        ArrayList<FsmParse> initialFsmParse;
        FsmParse fsmParse;
        if (Word.isPunctuation(surfaceForm) && !surfaceForm.equals("%")){
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("Punctuation"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (isNumber(surfaceForm)){
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("CardinalRoot"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (surfaceForm.matches("(\\d\\d|\\d)/(\\d\\d|\\d)/\\d+") || surfaceForm.matches("(\\d\\d|\\d)\\.(\\d\\d|\\d)\\.\\d+")){
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("DateRoot"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (surfaceForm.matches("\\d+/\\d+")){
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("FractionRoot"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            fsmParse = new FsmParse(surfaceForm, new State(("DateRoot"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (surfaceForm.matches("\\d+\\\\/\\d+")){
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("FractionRoot"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (surfaceForm.equalsIgnoreCase("%") || surfaceForm.matches("%(\\d\\d|\\d)") || surfaceForm.matches("%(\\d\\d|\\d)\\.\\d+")){
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("PercentRoot"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (surfaceForm.matches("(\\d\\d|\\d):(\\d\\d|\\d):(\\d\\d|\\d)") || surfaceForm.matches("(\\d\\d|\\d):(\\d\\d|\\d)")){
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("TimeRoot"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (surfaceForm.matches("\\d+-\\d+") || surfaceForm.matches("(\\d\\d|\\d):(\\d\\d|\\d)-(\\d\\d|\\d):(\\d\\d|\\d)") || surfaceForm.matches("(\\d\\d|\\d)\\.(\\d\\d|\\d)-(\\d\\d|\\d)\\.(\\d\\d|\\d)")){
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("RangeRoot"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (surfaceForm.startsWith("#")){
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("Hashtag"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (surfaceForm.contains("@")){
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(surfaceForm, new State(("Email"), true, true));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (surfaceForm.endsWith(".") && isInteger(surfaceForm.substring(0, surfaceForm.length() - 1))){
            Integer.parseInt(surfaceForm.substring(0, surfaceForm.length() - 1));
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(Integer.parseInt(surfaceForm.substring(0, surfaceForm.length() - 1)), getState("OrdinalRoot"));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (isInteger(surfaceForm)){
            Integer.parseInt(surfaceForm);
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(Integer.parseInt(surfaceForm), getState("CardinalRoot"));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        if (isDouble(surfaceForm)){
            Double.parseDouble(surfaceForm);
            initialFsmParse = new ArrayList<>(1);
            fsmParse = new FsmParse(Double.parseDouble(surfaceForm), getState("RealRoot"));
            fsmParse.constructInflectionalGroups();
            initialFsmParse.add(fsmParse);
            return initialFsmParse;
        }
        initialFsmParse = initializeRootList(surfaceForm, isProper);
        return parseWord(initialFsmParse, surfaceForm);
    }

    public boolean isProperNoun(String surfaceForm){
        if (surfaceForm == null || surfaceForm.length() == 0){
            return false;
        }
        return (surfaceForm.charAt(0) >= 'A' && surfaceForm.charAt(0) <= 'Z') || (surfaceForm.charAt(0) == 'İ') || (surfaceForm.charAt(0) == 'Ü') || (surfaceForm.charAt(0) == 'Ğ') || (surfaceForm.charAt(0) == 'Ş') || (surfaceForm.charAt(0) == 'Ç') || (surfaceForm.charAt(0) == 'Ö');
    }

    public FsmParseList robustMorphologicalAnalysis(String surfaceForm){
        ArrayList<FsmParse> fsmParse;
        FsmParseList currentParse;
        if (surfaceForm == null || surfaceForm.isEmpty()){
            return new FsmParseList(new ArrayList<>());
        }
        currentParse = morphologicalAnalysis(surfaceForm);
        if (currentParse.size() == 0){
            fsmParse = new ArrayList<>(1);
            if (isProperNoun(surfaceForm)){
                fsmParse.add(new FsmParse(surfaceForm, getState("ProperRoot")));
                return new FsmParseList(parseWord(fsmParse, surfaceForm));
            } else {
                fsmParse.add(new FsmParse(surfaceForm, getState("NominalRoot")));
                return new FsmParseList(parseWord(fsmParse, surfaceForm));
            }
        } else {
            return currentParse;
        }
    }

    public FsmParseList[] morphologicalAnalysis(Sentence sentence, boolean debugMode){
        FsmParseList wordFsmParseList;
        FsmParseList[] result = new FsmParseList[sentence.wordCount()];
        for (int i = 0; i < sentence.wordCount(); i++){
            wordFsmParseList = morphologicalAnalysis(sentence.getWord(i).getName());
            if (wordFsmParseList.size() == 0 && debugMode){
                System.out.println("Word " + sentence.getWord(i).getName() + " can not be parsed\n");
            }
            result[i] = wordFsmParseList;
        }
        return result;
    }

    public FsmParseList[] robustMorphologicalAnalysis(Sentence sentence){
        FsmParseList fsmParseList;
        FsmParseList[] result = new FsmParseList[sentence.wordCount()];
        for (int i = 0; i < sentence.wordCount(); i++){
            fsmParseList = robustMorphologicalAnalysis(sentence.getWord(i).getName());
            result[i] = fsmParseList;
        }
        return result;
    }

    private boolean isInteger(String surfaceForm){
        return surfaceForm.matches("\\+?\\d+") && surfaceForm.length() < 11;
    }

    private boolean isDouble(String surfaceForm){
        return surfaceForm.matches("\\+?(\\d+)?\\.\\d*");
    }

    private boolean isNumber(String surfaceForm){
        boolean found;
        int count = 0;
        String[] numbers = {"bir", "iki", "üç", "dört", "beş", "altı", "yedi", "sekiz", "dokuz",
                "on", "yirmi", "otuz", "kırk", "elli", "altmış", "yetmiş", "seksen", "doksan",
                "yüz", "bin", "milyon", "milyar", "trilyon", "katrilyon"};
        String word = surfaceForm;
        while (!word.isEmpty()){
            found = false;
            for (String number : numbers){
                if (word.startsWith(number)){
                    found = true;
                    count++;
                    word = word.substring(number.length());
                    break;
                }
            }
            if (!found){
                break;
            }
        }
        if (word.isEmpty() && count > 1){
            return true;
        } else {
            return false;
        }
    }

    public FsmParseList morphologicalAnalysis(String surfaceForm){
        FsmParseList fsmParseList;
        if (cache != null && cache.contains(surfaceForm)){
            return cache.get(surfaceForm);
        }
        if (surfaceForm.matches("(\\w|Ç|Ş|İ|Ü|Ö)\\.")) {
            dictionaryTrie.addWord(surfaceForm.toLowerCase(new Locale("tr")), new TxtWord(surfaceForm.toLowerCase(new Locale("tr")), "IS_OA"));
        }
        ArrayList<FsmParse> defaultFsmParse = analysis(surfaceForm.toLowerCase(new Locale("tr")), isProperNoun(surfaceForm));
        if (defaultFsmParse.size() > 0){
            fsmParseList = new FsmParseList(defaultFsmParse);
            if (cache != null){
                cache.add(surfaceForm, fsmParseList);
            }
            return fsmParseList;
        }
        ArrayList<FsmParse> fsmParse = new ArrayList<>();
        if (surfaceForm.contains("'")){
            String possibleRoot = surfaceForm.substring(0, surfaceForm.indexOf('\''));
            if (!possibleRoot.isEmpty()){
                if (possibleRoot.contains("/") || possibleRoot.contains("\\/")){
                    dictionaryTrie.addWord(possibleRoot, new TxtWord(possibleRoot, "IS_KESIR"));
                    fsmParse = analysis(surfaceForm.toLowerCase(new Locale("tr")), isProperNoun(surfaceForm));
                } else {
                    if (possibleRoot.matches("(\\d\\d|\\d)/(\\d\\d|\\d)/\\d+") || possibleRoot.matches("(\\d\\d|\\d)\\.(\\d\\d|\\d)\\.\\d+")){
                        dictionaryTrie.addWord(possibleRoot, new TxtWord(possibleRoot, "IS_DATE"));
                        fsmParse = analysis(surfaceForm.toLowerCase(new Locale("tr")), isProperNoun(surfaceForm));
                    } else {
                        if (possibleRoot.matches("\\d+/\\d+")){
                            dictionaryTrie.addWord(possibleRoot, new TxtWord(possibleRoot, "IS_KESIR"));
                            fsmParse = analysis(surfaceForm.toLowerCase(new Locale("tr")), isProperNoun(surfaceForm));
                        } else {
                            if (possibleRoot.matches("%(\\d\\d|\\d)") || possibleRoot.matches("%(\\d\\d|\\d)\\.\\d+")){
                                dictionaryTrie.addWord(possibleRoot, new TxtWord(possibleRoot, "IS_PERCENT"));
                                fsmParse = analysis(surfaceForm.toLowerCase(new Locale("tr")), isProperNoun(surfaceForm));
                            } else {
                                if (possibleRoot.matches("(\\d\\d|\\d):(\\d\\d|\\d):(\\d\\d|\\d)") || possibleRoot.matches("(\\d\\d|\\d):(\\d\\d|\\d)")){
                                    dictionaryTrie.addWord(possibleRoot, new TxtWord(possibleRoot, "IS_ZAMAN"));
                                    fsmParse = analysis(surfaceForm.toLowerCase(new Locale("tr")), isProperNoun(surfaceForm));
                                } else {
                                    if (possibleRoot.matches("\\d+-\\d+") || possibleRoot.matches("(\\d\\d|\\d):(\\d\\d|\\d)-(\\d\\d|\\d):(\\d\\d|\\d)") || possibleRoot.matches("(\\d\\d|\\d)\\.(\\d\\d|\\d)-(\\d\\d|\\d)\\.(\\d\\d|\\d)")){
                                        dictionaryTrie.addWord(possibleRoot, new TxtWord(possibleRoot, "IS_RANGE"));
                                        fsmParse = analysis(surfaceForm.toLowerCase(new Locale("tr")), isProperNoun(surfaceForm));
                                    } else {
                                        if (isInteger(possibleRoot)){
                                            Integer.parseInt(possibleRoot);
                                            dictionaryTrie.addWord(possibleRoot, new TxtWord(possibleRoot, "IS_SAYI"));
                                            fsmParse = analysis(surfaceForm.toLowerCase(new Locale("tr")), isProperNoun(surfaceForm));
                                        } else {
                                            if (isDouble(possibleRoot)){
                                                Double.parseDouble(possibleRoot);
                                                dictionaryTrie.addWord(possibleRoot, new TxtWord(possibleRoot, "IS_REELSAYI"));
                                                fsmParse = analysis(surfaceForm.toLowerCase(new Locale("tr")), isProperNoun(surfaceForm));
                                            } else {
                                                if (Word.isCapital(possibleRoot)) {
                                                    TxtWord newWord = null;
                                                    if (dictionary.getWord(possibleRoot.toLowerCase(new Locale("tr"))) != null){
                                                        ((TxtWord) dictionary.getWord(possibleRoot.toLowerCase(new Locale("tr")))).addFlag("IS_OA");
                                                    } else {
                                                        newWord = new TxtWord(possibleRoot.toLowerCase(new Locale("tr")), "IS_OA");
                                                        dictionaryTrie.addWord(possibleRoot.toLowerCase(new Locale("tr")), newWord);
                                                    }
                                                    fsmParse = analysis(surfaceForm.toLowerCase(new Locale("tr")), isProperNoun(surfaceForm));
                                                    if (fsmParse.size() == 0 && newWord != null) {
                                                        newWord.addFlag("IS_KIS");
                                                        fsmParse = analysis(surfaceForm.toLowerCase(new Locale("tr")), isProperNoun(surfaceForm));
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
        fsmParseList = new FsmParseList(fsmParse);
        if (cache != null){
            cache.add(surfaceForm, fsmParseList);
        }
        return fsmParseList;
    }

    public boolean morphologicalAnalysisExists(TxtWord rootWord, String surfaceForm){
        return analysisExists(rootWord, surfaceForm.toLowerCase(new Locale("tr")), true);
    }

}
