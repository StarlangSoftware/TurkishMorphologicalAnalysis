package MorphologicalAnalysis;

import Dictionary.Word;

import java.io.Serializable;
import java.util.ArrayList;

public class MorphologicalParse implements Serializable{
    protected ArrayList<InflectionalGroup> inflectionalGroups;
    protected Word root;

    public MorphologicalParse(){
    }

    public Word getWord(){
        return root;
    }

    public MorphologicalParse(String parse){
        int i;
        ArrayList<String> iGs;
        iGs = new ArrayList<String>();
        String st = parse;
        while (st.contains("^DB+")){
            iGs.add(st.substring(0, st.indexOf("^DB+")));
            st = st.substring(st.indexOf("^DB+") + 4);
        }
        iGs.add(st);
        inflectionalGroups = new ArrayList<InflectionalGroup>();
        if (iGs.get(0).equals("++Punc")){
            root = new Word("+");
            inflectionalGroups.add(new InflectionalGroup("Punc"));
        } else {
            if (iGs.get(0).indexOf('+') != -1){
                root = new Word(iGs.get(0).substring(0, iGs.get(0).indexOf('+')));
                inflectionalGroups.add(new InflectionalGroup(iGs.get(0).substring(iGs.get(0).indexOf('+') + 1)));
            } else {
                root = new Word(iGs.get(0));
            }
            for (i = 1; i < iGs.size(); i++){
                inflectionalGroups.add(new InflectionalGroup(iGs.get(i)));
            }
        }
    }

    public MorphologicalParse(ArrayList<String> inflectionalGroups){
        int i;
        this.inflectionalGroups = new ArrayList<InflectionalGroup>();
        if (inflectionalGroups.get(0).indexOf('+') != -1){
            root = new Word(inflectionalGroups.get(0).substring(0, inflectionalGroups.get(0).indexOf('+')));
            this.inflectionalGroups.add(new InflectionalGroup(inflectionalGroups.get(0).substring(inflectionalGroups.get(0).indexOf('+') + 1)));
        } else {
            System.out.println("Root word for " + inflectionalGroups.get(0) + " does not exist\n");
        }
        for (i = 1; i < inflectionalGroups.size(); i++){
            this.inflectionalGroups.add(new InflectionalGroup(inflectionalGroups.get(i)));
        }
    }

    public String getTransitionList(){
        String result = inflectionalGroups.get(0).toString();
        for (int i = 1; i < inflectionalGroups.size(); i++){
            result = result + "+" + inflectionalGroups.get(i).toString();
        }
        return result;
    }

    public String getInflectionalGroupString(int index){
        if (index == 0){
            return root.getName() + "+" + inflectionalGroups.get(0).toString();
        } else {
            return inflectionalGroups.get(index).toString();
        }
    }

    public InflectionalGroup getInflectionalGroup(int index){
        return inflectionalGroups.get(index);
    }

    public InflectionalGroup getLastInflectionalGroup(){
        return getInflectionalGroup(inflectionalGroups.size() - 1);
    }

    public String getTag(int index){
        int size = 1;
        if (index == 0)
            return root.getName();
        for (InflectionalGroup group:inflectionalGroups){
            if (index < size + group.size()){
                return InflectionalGroup.getTag(group.getTag(index - size));
            }
            size += group.size();
        }
        return null;
    }

    public int tagSize(){
        int size = 1;
        for (InflectionalGroup group:inflectionalGroups){
            size += group.size();
        }
        return size;
    }

    public int size(){
        return inflectionalGroups.size();
    }

    public InflectionalGroup firstInflectionalGroup(){
        return inflectionalGroups.get(0);
    }

    public InflectionalGroup lastInflectionalGroup(){
        return inflectionalGroups.get(inflectionalGroups.size() - 1);
    }

    public Word getWordWithPos(){
        return new Word(root.getName() + "+" + InflectionalGroup.getTag(firstInflectionalGroup().getTag(0)));
    }

    public String getPos(){
        return InflectionalGroup.getTag(lastInflectionalGroup().getTag(0));
    }

    public String getRootPos(){
        return InflectionalGroup.getTag(firstInflectionalGroup().getTag(0));
    }

    public String lastIGContainsCase(){
        MorphologicalTag caseTag = lastInflectionalGroup().containsCase();
        if (caseTag != null)
            return InflectionalGroup.getTag(caseTag);
        else
            return "NULL";
    }

    public boolean lastIGContainsTag(MorphologicalTag tag){
        return lastInflectionalGroup().containsTag(tag);
    }

    public boolean lastIGContainsPossessive(){
        return lastInflectionalGroup().containsPossessive();
    }

    public boolean isCapitalWord(){
        return Character.isUpperCase(root.getName().charAt(0));
    }

    public boolean isNoun(){
        return (getPos().equals("NOUN"));
    }

    public boolean isVerb(){
        return (getPos().equals("VERB"));
    }

    public boolean isRootVerb(){
        return (getRootPos().equals("VERB"));
    }

    public boolean isAdjective(){
        return (getPos().equals("ADJ"));
    }

    public boolean isProperNoun(){
        return getInflectionalGroup(0).containsTag(MorphologicalTag.PROPERNOUN);
    }

    public boolean isPunctuation(){
        return getInflectionalGroup(0).containsTag(MorphologicalTag.PUNCTUATION);
    }

    public boolean isCardinal(){
        return getInflectionalGroup(0).containsTag(MorphologicalTag.CARDINAL);
    }

    public boolean isOrdinal(){
        return getInflectionalGroup(0).containsTag(MorphologicalTag.ORDINAL);
    }

    public boolean isReal(){
        return getInflectionalGroup(0).containsTag(MorphologicalTag.REAL);
    }

    public boolean isNumber(){
        return isReal() || isCardinal();
    }

    public boolean isTime(){
        return getInflectionalGroup(0).containsTag(MorphologicalTag.TIME);
    }

    public boolean isDate(){
        return getInflectionalGroup(0).containsTag(MorphologicalTag.DATE);
    }

    public boolean isHashTag(){
        return getInflectionalGroup(0).containsTag(MorphologicalTag.HASHTAG);
    }

    public boolean isEmail(){
        return getInflectionalGroup(0).containsTag(MorphologicalTag.EMAIL);
    }

    public boolean isPercent(){
        return getInflectionalGroup(0).containsTag(MorphologicalTag.PERCENT);
    }

    public boolean isFraction(){
        return getInflectionalGroup(0).containsTag(MorphologicalTag.FRACTION);
    }

    public boolean isRange(){
        return getInflectionalGroup(0).containsTag(MorphologicalTag.RANGE);
    }

    public boolean isPlural(){
        for (InflectionalGroup inflectionalGroup : inflectionalGroups)
            if (inflectionalGroup.containsPlural()) {
                return true;
            }
        return false;
    }

    public boolean isAuxiliary(){
        return root.getName().equals("et") || root.getName().equals("ol") || root.getName().equals("yap");
    }

    public boolean containsTag(MorphologicalTag tag){
        for (InflectionalGroup inflectionalGroup:inflectionalGroups){
            if (inflectionalGroup.containsTag(tag)){
                return true;
            }
        }
        return false;
    }

    public String toString(){
        String result = root.getName() + "+" + inflectionalGroups.get(0).toString();
        for (int i = 1; i < inflectionalGroups.size(); i++)
            result = result + "^DB+" + inflectionalGroups.get(i).toString();
        return result;
    }

}
