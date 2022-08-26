package MorphologicalAnalysis;

import Dictionary.Word;
import java.util.ArrayList;
import java.util.Collections;

public class FsmParseList {
    private ArrayList<FsmParse> fsmParses;
    private static final String[] longestRootExceptions = {"acağı acak NOUN VERB", "acağım acak NOUN VERB", "acağımı acak NOUN VERB", "acağımız acak NOUN VERB", "acağın acak NOUN VERB", "acağına acak NOUN VERB", "acağını acak NOUN VERB", "acağının acak NOUN VERB", "acağınız acak NOUN VERB", "acağınıza acak NOUN VERB", "acağınızdır acak NOUN VERB", "acağınızı acak NOUN VERB", "acağınızın acak NOUN VERB", "acağız acak NOUN VERB", "acakları acak NOUN VERB", "acaklarını acak NOUN VERB", "acaksa acak NOUN VERB", "acaktır acak NOUN VERB", "ardım ar NOUN VERB", "arız ar NOUN VERB", "arken ar NOUN VERB", "arsa ar NOUN VERB", "arsak ar NOUN VERB", "arsanız ar NOUN VERB", "arsınız ar NOUN VERB", "eceği ecek NOUN VERB", "eceğim ecek NOUN VERB", "eceğimi ecek NOUN VERB", "eceğimiz ecek NOUN VERB", "eceğin ecek NOUN VERB", "eceğine ecek NOUN VERB", "eceğini ecek NOUN VERB", "eceğinin ecek NOUN VERB", "eceğiniz ecek NOUN VERB", "eceğinizdir ecek NOUN VERB", "eceğinize ecek NOUN VERB", "eceğinizi ecek NOUN VERB", "eceğinizin ecek NOUN VERB", "eceğiz ecek NOUN VERB", "ecekleri ecek NOUN VERB", "eceklerini ecek NOUN VERB", "ecekse ecek NOUN VERB", "ecektir ecek NOUN VERB", "erdim er NOUN VERB", "eriz er NOUN VERB", "erken er NOUN VERB", "erse er NOUN VERB", "ersek er NOUN VERB", "erseniz er NOUN VERB", "ersiniz er NOUN VERB", "ilen i VERB VERB", "ilene i VERB VERB", "ilin i VERB VERB", "ilince i VERB VERB", "imiz i ADJ NOUN", "in i ADJ NOUN", "inde i ADJ NOUN", "ine i ADJ NOUN", "ini i ADJ NOUN", "inin i ADJ NOUN", "ılan ı NOUN VERB", "ılana ı NOUN VERB", "ılın ı NOUN VERB", "ılınca ı NOUN VERB", "la la VERB NOUN", "lar la VERB NOUN", "lardan la VERB NOUN", "lardandır la VERB NOUN", "lardır la VERB NOUN", "ları la VERB NOUN", "larıdır la VERB NOUN", "larım la VERB NOUN", "larımdan la VERB NOUN", "larımız la VERB NOUN", "larımıza la VERB NOUN", "larımızda la VERB NOUN", "larımızdan la VERB NOUN", "larımızdaydı la VERB NOUN", "larımızı la VERB NOUN", "larımızın la VERB NOUN", "larımızla la VERB NOUN", "ların la VERB NOUN", "larına la VERB NOUN", "larında la VERB NOUN", "larındaki la VERB NOUN", "larındakiler la VERB NOUN", "larındakilere la VERB NOUN", "larındakileri la VERB NOUN", "larındakilerin la VERB NOUN", "larından la VERB NOUN", "larındandır la VERB NOUN", "larındaysa la VERB NOUN", "larını la VERB NOUN", "larının la VERB NOUN", "larınız la VERB NOUN", "larınıza la VERB NOUN", "larınızda la VERB NOUN", "larınızdaki la VERB NOUN", "larınızdan la VERB NOUN", "larınızı la VERB NOUN", "larınızın la VERB NOUN", "larınızla la VERB NOUN", "larıyla la VERB NOUN", "le le VERB NOUN", "ler le VERB NOUN", "lerden le VERB NOUN", "lerdendir le VERB NOUN", "lerdir le VERB NOUN", "leri le VERB NOUN", "leridir le VERB NOUN", "lerim le VERB NOUN", "lerimden le VERB NOUN", "lerimiz le VERB NOUN", "lerimizde le VERB NOUN", "lerimizden le VERB NOUN", "lerimizdeydi le VERB NOUN", "lerimize le VERB NOUN", "lerimizi le VERB NOUN", "lerimizin le VERB NOUN", "lerimizle le VERB NOUN", "lerin le VERB NOUN", "lerinde le VERB NOUN", "lerindeki le VERB NOUN", "lerindekiler le VERB NOUN", "lerindekilere le VERB NOUN", "lerindekileri le VERB NOUN", "lerindekilerin le VERB NOUN", "lerinden le VERB NOUN", "lerindendir le VERB NOUN", "lerindeyse le VERB NOUN", "lerine le VERB NOUN", "lerini le VERB NOUN", "lerinin le VERB NOUN", "leriniz le VERB NOUN", "lerinizde le VERB NOUN", "lerinizdeki le VERB NOUN", "lerinizden le VERB NOUN", "lerinize le VERB NOUN", "lerinizi le VERB NOUN", "lerinizin le VERB NOUN", "lerinizle le VERB NOUN", "leriyle le VERB NOUN", "m m NOUN NOUN", "madan ma NOUN VERB", "malı ma NOUN VERB", "malıdır ma NOUN VERB", "malıdırlar ma NOUN VERB", "malılar ma NOUN VERB", "malısınız ma NOUN VERB", "malıyım ma NOUN VERB", "malıyız ma NOUN VERB", "mam ma NOUN VERB", "mama ma NOUN VERB", "mamız ma NOUN VERB", "mamıza ma NOUN VERB", "mamızı ma NOUN VERB", "manız ma NOUN VERB", "manızda ma NOUN VERB", "manızdır ma NOUN VERB", "manızı ma NOUN VERB", "manızla ma NOUN VERB", "ması ma NOUN VERB", "masıdır ma NOUN VERB", "masın ma NOUN VERB", "masına ma NOUN VERB", "masında ma NOUN VERB", "masındaki ma NOUN VERB", "masını ma NOUN VERB", "masıyla ma NOUN VERB", "mdan m NOUN NOUN", "meden me NOUN VERB", "meli me NOUN VERB", "melidir me NOUN VERB", "melidirler me NOUN VERB", "meliler me NOUN VERB", "melisiniz me NOUN VERB", "meliyim me NOUN VERB", "meliyiz me NOUN VERB", "mem me NOUN VERB", "meme me NOUN VERB", "memiz me NOUN VERB", "memize me NOUN VERB", "memizi me NOUN VERB", "meniz me NOUN VERB", "menizde me NOUN VERB", "menizdir me NOUN VERB", "menizi me NOUN VERB", "menizle me NOUN VERB", "mesi me NOUN VERB", "mesidir me NOUN VERB", "mesin me NOUN VERB", "mesinde me NOUN VERB", "mesindeki me NOUN VERB", "mesine me NOUN VERB", "mesini me NOUN VERB", "mesiyle me NOUN VERB", "mişse miş NOUN VERB", "mını m NOUN NOUN", "mışsa mış NOUN VERB", "mız m NOUN NOUN", "n n NOUN NOUN", "na n NOUN NOUN", "ne n NOUN NOUN", "nin n NOUN NOUN", "niz n NOUN NOUN", "nın n NOUN NOUN", "nız n NOUN NOUN", "rdim r NOUN VERB", "rdım r NOUN VERB", "riz r NOUN VERB", "rız r NOUN VERB", "rken r NOUN VERB", "rken r NOUN VERB", "rsa r NOUN VERB", "rsak r NOUN VERB", "rsanız r NOUN VERB", "rse r NOUN VERB", "rsek r NOUN VERB", "rseniz r NOUN VERB", "rsiniz r NOUN VERB", "rsınız r NOUN VERB", "sa sa VERB ADJ", "se se VERB ADJ", "ulan u NOUN VERB", "un un VERB NOUN", "üne ün VERB NOUN", "unun un VERB NOUN"};

    /**
     * A constructor of {@link FsmParseList} class which takes an {@link ArrayList} fsmParses as an input. First it sorts
     * the items of the {@link ArrayList} then loops through it, if the current item's transitions equal to the next item's
     * transitions, it removes the latter item. At the end, it assigns this {@link ArrayList} to the fsmParses variable.
     *
     * @param fsmParses {@link FsmParse} type{@link ArrayList} input.
     */
    public FsmParseList(ArrayList<FsmParse> fsmParses) {
        Collections.sort(fsmParses);
        for (int i = 0; i < fsmParses.size() - 1; i++) {
            if (fsmParses.get(i).transitionList().equals(fsmParses.get(i + 1).transitionList())) {
                fsmParses.remove(i + 1);
                i--;
            }
        }
        this.fsmParses = fsmParses;
    }

    /**
     * The size method returns the size of fsmParses {@link ArrayList}.
     *
     * @return the size of fsmParses {@link ArrayList}.
     */
    public int size() {
        return fsmParses.size();
    }

    /**
     * The getFsmParse method takes an integer index as an input and returns the item of fsmParses {@link ArrayList} at given index.
     *
     * @param index Integer input.
     * @return the item of fsmParses {@link ArrayList} at given index.
     */
    public FsmParse getFsmParse(int index) {
        return fsmParses.get(index);
    }

    /**
     * The rootWords method gets the first item's root of fsmParses {@link ArrayList} and uses it as currentRoot. Then loops through
     * the fsmParses, if the current item's root does not equal to the currentRoot, it then assigns it as the currentRoot and
     * accumulates root words in a {@link String} result.
     *
     * @return String result that has root words.
     */
    public String rootWords() {
        String result = fsmParses.get(0).getWord().getName(), currentRoot = result;
        for (int i = 1; i < fsmParses.size(); i++) {
            if (!fsmParses.get(i).getWord().getName().equals(currentRoot)) {
                currentRoot = fsmParses.get(i).getWord().getName();
                result = result + "$" + currentRoot;
            }
        }
        return result;
    }

    /**
     * The reduceToParsesWithSameRootAndPos method takes a {@link Word} currentWithPos as an input and loops i times till
     * i equals to the size of the fsmParses {@link ArrayList}. If the given currentWithPos does not equal to the ith item's
     * root and the MorphologicalTag of the first inflectional of fsmParses, it removes the ith item from the {@link ArrayList}.
     *
     * @param currentWithPos {@link Word} input.
     */
    public void reduceToParsesWithSameRootAndPos(Word currentWithPos) {
        int i = 0;
        while (i < fsmParses.size()) {
            if (!fsmParses.get(i).getWordWithPos().equals(currentWithPos)) {
                fsmParses.remove(i);
            } else {
                i++;
            }
        }
    }

    /**
     * The getParseWithLongestRootWord method returns the parse with the longest root word. If more than one parse has the
     * longest root word, the first parse with that root is returned.
     *
     * @return FsmParse Parse with the longest root word.
     */
    public FsmParse getParseWithLongestRootWord() {
        int maxLength = -1;
        FsmParse bestParse = null;
        for (FsmParse currentParse : fsmParses) {
            if (currentParse.getWord().getName().length() > maxLength && !isLongestRootException(currentParse)) {
                maxLength = currentParse.getWord().getName().length();
                bestParse = currentParse;
            }
        }
        return bestParse;
    }

    private boolean isLongestRootException(FsmParse fsmParse) {
        String surfaceForm = fsmParse.getSurfaceForm();
        String root = fsmParse.getWord().getName();
        String parse = fsmParse.toString();

        for (int i = 0; i < longestRootExceptions.length; i++) {
            if(surfaceForm.endsWith(longestRootExceptions[i].split(" ")[0]) && root.endsWith(longestRootExceptions[i].split(" ")[1])
                    && (parse.split("\\+",2)[1].startsWith(longestRootExceptions[i].split(" ")[2]))){
                for (FsmParse currentParse: fsmParses) {
                    if(currentParse.toString().split("\\+",2)[0].equals(surfaceForm.replaceFirst(longestRootExceptions[i].split(" ")[0],""))
                            && currentParse.toString().split("\\+",2)[1].startsWith(longestRootExceptions[i].split(" ")[3])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * The reduceToParsesWithSameRoot method takes a {@link String} currentWithPos as an input and loops i times till
     * i equals to the size of the fsmParses {@link ArrayList}. If the given currentRoot does not equal to the root of ith item of
     * fsmParses, it removes the ith item from the {@link ArrayList}.
     *
     * @param currentRoot {@link String} input.
     */
    public void reduceToParsesWithSameRoot(String currentRoot) {
        int i = 0;
        while (i < fsmParses.size()) {
            if (!fsmParses.get(i).getWord().getName().equals(currentRoot)) {
                fsmParses.remove(i);
            } else {
                i++;
            }
        }
    }

    /**
     * The constructParseListForDifferentRootWithPos method initially creates a result {@link ArrayList} then loops through the
     * fsmParses {@link ArrayList}. For the first iteration, it creates new {@link ArrayList} as initial, then adds the
     * first item od fsmParses to initial and also add this initial {@link ArrayList} to the result {@link ArrayList}.
     * For the following iterations, it checks whether the current item's root with the MorphologicalTag of the first inflectional
     * equal to the previous item's  root with the MorphologicalTag of the first inflectional. If so, it adds that item
     * to the result {@link ArrayList}, if not it creates new {@link ArrayList} as initial and adds the first item od fsmParses
     * to initial and also add this initial {@link ArrayList} to the result {@link ArrayList}.
     *
     * @return result {@link ArrayList} type of {@link FsmParseList}.
     */
    public ArrayList<FsmParseList> constructParseListForDifferentRootWithPos() {
        ArrayList<FsmParseList> result = new ArrayList<>();
        int i = 0;
        while (i < fsmParses.size()) {
            if (i == 0) {
                ArrayList<FsmParse> initial = new ArrayList<>();
                initial.add(fsmParses.get(i));
                result.add(new FsmParseList(initial));
            } else {
                if (fsmParses.get(i).getWordWithPos().equals(fsmParses.get(i - 1).getWordWithPos())) {
                    result.get(result.size() - 1).fsmParses.add(fsmParses.get(i));
                } else {
                    ArrayList<FsmParse> initial = new ArrayList<>();
                    initial.add(fsmParses.get(i));
                    result.add(new FsmParseList(initial));
                }
            }
            i++;
        }
        return result;
    }

    /**
     * The parsesWithoutPrefixAndSuffix method first creates a {@link String} array named analyses with the size of fsmParses {@link ArrayList}'s size.
     * <p>
     * If the size is just 1, it then returns the first item's transitionList, if it is greater than 1, loops through the fsmParses and
     * puts the transitionList of each item to the analyses array.
     * <p>
     * If the removePrefix condition holds, it loops through the analyses array and takes each item's substring after the first + sign and updates that
     * item of analyses array with that substring.
     * <p>
     * If the removeSuffix condition holds, it loops through the analyses array and takes each item's substring till the last + sign and updates that
     * item of analyses array with that substring.
     * <p>
     * It then removes the duplicate items of analyses array and returns a result {@link String} that has the accumulated items of analyses array.
     *
     * @return result {@link String} that has the accumulated items of analyses array.
     */
    public String parsesWithoutPrefixAndSuffix() {
        String[] analyses = new String[fsmParses.size()];
        boolean removePrefix = true, removeSuffix = true;
        if (fsmParses.size() == 1) {
            return fsmParses.get(0).transitionList().substring(fsmParses.get(0).transitionList().indexOf("+") + 1);
        }
        for (int i = 0; i < fsmParses.size(); i++) {
            analyses[i] = fsmParses.get(i).transitionList();
        }
        while (removePrefix) {
            removePrefix = true;
            for (int i = 0; i < fsmParses.size() - 1; i++) {
                if (!analyses[i].contains("+") || !analyses[i + 1].contains("+") ||
                        !analyses[i].substring(0, analyses[i].indexOf("+") + 1).equals(analyses[i + 1].substring(0, analyses[i + 1].indexOf("+") + 1))) {
                    removePrefix = false;
                    break;
                }
            }
            if (removePrefix) {
                for (int i = 0; i < fsmParses.size(); i++) {
                    analyses[i] = analyses[i].substring(analyses[i].indexOf("+") + 1);
                }
            }
        }
        while (removeSuffix) {
            removeSuffix = true;
            for (int i = 0; i < fsmParses.size() - 1; i++) {
                if (!analyses[i].contains("+") || !analyses[i + 1].contains("+") ||
                        !analyses[i].substring(analyses[i].lastIndexOf("+")).equals(analyses[i + 1].substring(analyses[i + 1].lastIndexOf("+")))) {
                    removeSuffix = false;
                    break;
                }
            }
            if (removeSuffix) {
                for (int i = 0; i < fsmParses.size(); i++) {
                    analyses[i] = analyses[i].substring(0, analyses[i].lastIndexOf("+"));
                }
            }
        }
        for (int i = 0; i < analyses.length; i++) {
            for (int j = i + 1; j < analyses.length; j++) {
                if (analyses[i].compareTo(analyses[j]) > 0) {
                    String tmp = analyses[i];
                    analyses[i] = analyses[j];
                    analyses[j] = tmp;
                }
            }
        }
        String result = analyses[0];
        for (int i = 1; i < analyses.length; i++) {
            result = result + "$" + analyses[i];
        }
        return result;
    }

    /**
     * The overridden toString method loops through the fsmParses {@link ArrayList} and accumulates the items to a result {@link String}.
     *
     * @return result {@link String} that has the items of fsmParses {@link ArrayList}.
     */
    public String toString() {
        String result = "";
        for (int i = 0; i < fsmParses.size(); i++) {
            result += fsmParses.get(i) + "\n";
        }
        return result;
    }

    /**
     * The toJson method adds [\n to the beginning of the result {@link String} that has the items of fsmParses {@link ArrayList}
     * between double quotes and adds /n] to the ending.
     *
     * @return String output.
     */
    public String toJson() {
        String json = "[\n";
        for (int i = 0; i < fsmParses.size(); i++) {
            if (i == 0) {
                json = json + "\"" + fsmParses.get(i).toString() + "\"";
            } else {
                json = json + ",\n\"" + fsmParses.get(i).toString() + "\"";
            }
        }
        return json + "\n]";
    }

}
