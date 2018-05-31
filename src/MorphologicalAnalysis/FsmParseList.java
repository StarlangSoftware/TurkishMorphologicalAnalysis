package MorphologicalAnalysis;

import Dictionary.Word;

import java.util.ArrayList;
import java.util.Collections;

public class FsmParseList {
    private ArrayList<FsmParse> fsmParses;

    public FsmParseList(ArrayList<FsmParse> fsmParses){
        Collections.sort(fsmParses);
        for (int i = 0; i < fsmParses.size() - 1; i++){
            if (fsmParses.get(i).transitionList().equals(fsmParses.get(i + 1).transitionList())){
                fsmParses.remove(i + 1);
                i--;
            }
        }
        this.fsmParses = fsmParses;
    }

    public int size(){
        return fsmParses.size();
    }

    public FsmParse getFsmParse(int index){
        return fsmParses.get(index);
    }

    public String rootWords(){
        String result = fsmParses.get(0).getWord().getName(), currentRoot = result;
        for (int i = 1; i < fsmParses.size(); i++){
            if (!fsmParses.get(i).getWord().getName().equals(currentRoot)){
                currentRoot = fsmParses.get(i).getWord().getName();
                result = result + "$" + currentRoot;
            }
        }
        return result;
    }

    public void reduceToParsesWithSameRootAndPos(Word currentWithPos){
        int i = 0;
        while (i < fsmParses.size()){
            if (!fsmParses.get(i).getWordWithPos().equals(currentWithPos)){
                fsmParses.remove(i);
            } else {
                i++;
            }
        }
    }

    public void reduceToParsesWithSameRoot(String currentRoot){
        int i = 0;
        while (i < fsmParses.size()){
            if (!fsmParses.get(i).getWord().getName().equals(currentRoot)){
                fsmParses.remove(i);
            } else {
                i++;
            }
        }
    }

    private String defaultCaseForParseString(String rootForm, String parseString, String partOfSpeech){
        String defaultCase = null;
        switch (parseString){
            case "A3PL+P3PL+NOM$A3PL+P3SG+NOM$A3PL+PNON+ACC$A3SG+P3PL+NOM":
                defaultCase = "A3PL+P3SG+NOM";
                break;
            case "P3SG+NOM$PNON+ACC":
                if (partOfSpeech.equals("PROP")){
                    defaultCase = "PNON+ACC";
                } else {
                    defaultCase = "P3SG+NOM";
                }
                break;
            case "A3SG+P2SG$A3SG+PNON":
                defaultCase = "A3SG+PNON";
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
                if (rootForm.equalsIgnoreCase("yıl") || rootForm.equalsIgnoreCase("süre") || rootForm.equalsIgnoreCase("zaman") || rootForm.equalsIgnoreCase("ay")){
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

    public FsmParse caseDisambiguator(){
        String defaultCase;
        String parseString = parsesWithoutPrefixAndSuffix();
        if (fsmParses.size() == 1){
            return fsmParses.get(0);
        }
        if (fsmParses.size() == 0){
            return null;
        }
        defaultCase = defaultCaseForParseString(fsmParses.get(0).getWord().getName(), parseString, fsmParses.get(0).getFinalPos());
        if (defaultCase != null){
            for (int i = 0; i < fsmParses.size(); i++){
                FsmParse fsmParse = fsmParses.get(i);
                if (fsmParse.transitionList().contains(defaultCase)){
                    return fsmParse;
                }
            }
        }
        return null;
    }


    public ArrayList<FsmParseList> constructParseListForDifferentRootWithPos(){
        ArrayList<FsmParseList> result = new ArrayList<>();
        int i = 0;
        while (i < fsmParses.size()){
            if (i == 0){
                ArrayList<FsmParse> initial = new ArrayList<>();
                initial.add(fsmParses.get(i));
                result.add(new FsmParseList(initial));
            } else {
                if (fsmParses.get(i).getWordWithPos().equals(fsmParses.get(i - 1).getWordWithPos())){
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

    public String parsesWithoutPrefixAndSuffix(){
        String[] analyses = new String[fsmParses.size()];
        boolean removePrefix = true, removeSuffix = true;
        if (fsmParses.size() == 1){
            return fsmParses.get(0).transitionList().substring(fsmParses.get(0).transitionList().indexOf("+") + 1);
        }
        for (int i = 0; i < fsmParses.size(); i++){
            analyses[i] = fsmParses.get(i).transitionList();
        }
        while (removePrefix){
            removePrefix = true;
            for (int i = 0; i < fsmParses.size() - 1; i++){
                if (!analyses[i].contains("+") || !analyses[i + 1].contains("+") ||
                        !analyses[i].substring(0, analyses[i].indexOf("+") + 1).equals(analyses[i + 1].substring(0, analyses[i + 1].indexOf("+") + 1))){
                    removePrefix = false;
                    break;
                }
            }
            if (removePrefix){
                for (int i = 0; i < fsmParses.size(); i++){
                    analyses[i] = analyses[i].substring(analyses[i].indexOf("+") + 1);
                }
            }
        }
        while (removeSuffix){
            removeSuffix = true;
            for (int i = 0; i < fsmParses.size() - 1; i++){
                if (!analyses[i].contains("+") || !analyses[i + 1].contains("+") ||
                        !analyses[i].substring(analyses[i].lastIndexOf("+")).equals(analyses[i + 1].substring(analyses[i + 1].lastIndexOf("+")))) {
                    removeSuffix = false;
                    break;
                }
            }
            if (removeSuffix){
                for (int i = 0; i < fsmParses.size(); i++){
                    analyses[i] = analyses[i].substring(0, analyses[i].lastIndexOf("+"));
                }
            }
        }
        for (int i = 0; i < analyses.length; i++){
            for (int j = i + 1; j < analyses.length; j++){
                if (analyses[i].compareTo(analyses[j]) > 0){
                    String tmp = analyses[i];
                    analyses[i] = analyses[j];
                    analyses[j] = tmp;
                }
            }
        }
        String result = analyses[0];
        for (int i = 1; i < analyses.length; i++){
            result = result + "$" + analyses[i];
        }
        return result;
    }

    public String toString(){
        String result = "";
        for (int i = 0; i < fsmParses.size(); i++){
            result += fsmParses.get(i) + "\n";
        }
        return result;
    }

    public String toJson(){
        String json = "[\n";
        for (int i = 0; i < fsmParses.size(); i++){
            if (i == 0){
                json = json + "\"" + fsmParses.get(i).toString() + "\"";
            } else {
                json = json + ",\n\"" + fsmParses.get(i).toString() + "\"";
            }
        }
        return json + "\n]";
    }

}
