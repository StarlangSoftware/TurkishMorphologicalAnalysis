package MorphologicalAnalysis;

import Dictionary.Word;

import java.util.ArrayList;
import java.util.Collections;

public class FsmParseList {
    private ArrayList<FsmParse> fsmParses;

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
     * The defaultCaseForParseString method takes String rootForm, parseString and partOfSpeech as inputs. And checks defined cases
     * for parseString and returns the strings till the $ sign. For example, if the given parseString is
     * "A3PL+P3PL+NOM$A3PL+P3SG+NOM$A3PL+PNON+ACC$A3SG+P3PL+NOM" it returns "A3PL+P3SG+NOM".
     *
     * @param rootForm     String input.
     * @param parseString  String input.
     * @param partOfSpeech String input.
     * @return String defaultCase.
     */
    private String defaultCaseForParseString(String rootForm, String parseString, String partOfSpeech) {
        String defaultCase = null;
        switch (parseString) {
            case "P3SG+NOM$PNON+ACC":
                if (partOfSpeech.equals("PROP")) {
                    defaultCase = "PNON+ACC";
                }
                break;
            case "A2SG+P2SG$A3SG+P3SG":
                defaultCase = "A3SG+P3SG";
                break;
            case "P2SG$P3SG":
                defaultCase = "P3SG";
                break;
            case "A3SG+PNON+NOM^DB+VERB+ZERO+PRES+A3PL$A3PL+PNON+NOM":
                defaultCase = "A3PL+PNON+NOM";
                break;
            case "P2SG+NOM$PNON+GEN":
                defaultCase = "PNON+GEN";
                break;
            case "AOR^DB+ADJ+ZERO$AOR+A3SG":
                defaultCase = "AOR+A3SG";
                break;
            case "P2SG$PNON":
                defaultCase = "PNON";
                break;
            case "ADV+SINCE$VERB+ZERO+PRES+COP+A3SG":
                if (rootForm.equalsIgnoreCase("yıl") || rootForm.equalsIgnoreCase("süre") || rootForm.equalsIgnoreCase("zaman") || rootForm.equalsIgnoreCase("ay")) {
                    defaultCase = "ADV+SINCE";
                } else {
                    defaultCase = "VERB+ZERO+PRES+COP+A3SG";
                }
                break;
            case "CONJ$VERB+POS+IMP+A2SG":
                defaultCase = "CONJ";
                break;
            case "NEG+IMP+A2SG$POS^DB+NOUN+INF2+A3SG+PNON+NOM":
                defaultCase = "POS^DB+NOUN+INF2+A3SG+PNON+NOM";
                break;
            case "NEG+OPT+A3SG$POS^DB+NOUN+INF2+A3SG+PNON+DAT":
                defaultCase = "POS^DB+NOUN+INF2+A3SG+PNON+DAT";
                break;
            case "NOUN+A3SG+P3SG+NOM$NOUN^DB+ADJ+ALMOST":
                defaultCase = "NOUN+A3SG+P3SG+NOM";
                break;
            case "ADJ$VERB+POS+IMP+A2SG":
                defaultCase = "ADJ";
                break;
            case "NOUN+A3SG+PNON+NOM$VERB+POS+IMP+A2SG":
                defaultCase = "NOUN+A3SG+PNON+NOM";
                break;
            case "INF2+A3SG+P3SG+NOM$INF2^DB+ADJ+ALMOST$":
                defaultCase = "INF2+A3SG+P3SG+NOM";
                break;
        }
        return defaultCase;
    }

    /**
     * The caseDisambiguator method first calls the parsesWithoutPrefixAndSuffix method and gets the words without prefixes and suffixes.
     * If the size of fsmParses {@link ArrayList} is 1, it directly returns the first item of that {@link ArrayList} and null if
     * the size is 0.
     * <p>
     * Then, it calls defaultCaseForParseString method with the root of first item of fsmParses, result of parsesWithoutPrefixAndSuffix method,
     * and the pos of the first item and assigns it result to the defaultCase {@link String}. If defaultCase is not null,
     * it then loops through the fsmParses and checks whether the current transitionList of {@link FsmParse} contains the defaultCase, if so it returns
     * current {@link FsmParse}, null otherwise.
     *
     * @return FsmParse if it contains defaultCase, null otherwise.
     */
    public FsmParse caseDisambiguator() {
        String defaultCase;
        String parseString = parsesWithoutPrefixAndSuffix();
        if (fsmParses.size() == 1) {
            return fsmParses.get(0);
        }
        if (fsmParses.size() == 0) {
            return null;
        }
        defaultCase = defaultCaseForParseString(fsmParses.get(0).getWord().getName(), parseString, fsmParses.get(0).getFinalPos());
        if (defaultCase != null) {
            for (int i = 0; i < fsmParses.size(); i++) {
                FsmParse fsmParse = fsmParses.get(i);
                if (fsmParse.transitionList().contains(defaultCase)) {
                    return fsmParse;
                }
            }
        }
        return null;
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
