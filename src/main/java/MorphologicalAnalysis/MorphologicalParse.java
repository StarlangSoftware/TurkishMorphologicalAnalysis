package MorphologicalAnalysis;

import Dictionary.Word;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class MorphologicalParse implements Serializable {
    protected ArrayList<InflectionalGroup> inflectionalGroups;
    protected Word root;

    /**
     * An empty constructor of {@link MorphologicalParse} class.
     */
    public MorphologicalParse() {
    }

    /**
     * The no-arg getWord method returns root {@link Word}.
     *
     * @return root {@link Word}.
     */
    public Word getWord() {
        return root;
    }

    /**
     * Another constructor of {@link MorphologicalParse} class which takes a {@link String} parse as an input. First it creates
     * an {@link ArrayList} as iGs for inflectional groups, and while given String contains derivational boundary (^DB+), it
     * adds the substring to the iGs {@link ArrayList} and continue to use given String from 4th index. If it does not contain ^DB+,
     * it directly adds the given String to the iGs {@link ArrayList}. Then, it creates a new {@link ArrayList} as
     * inflectionalGroups and checks for some cases.
     * <p>
     * If the first item of iGs {@link ArrayList} is ++Punc, it creates a new root as +, and by calling
     * {@link InflectionalGroup} method with Punc it initializes the IG {@link ArrayList} by parsing given input
     * String IG by + and calling the getMorphologicalTag method with these substrings. If getMorphologicalTag method returns
     * a tag, it adds this tag to the IG {@link ArrayList} and also to the inflectionalGroups {@link ArrayList}.
     * <p>
     * If the first item of iGs {@link ArrayList} has +, it creates a new word of first item's substring from index 0 to +,
     * and assigns it to root. Then, by calling {@link InflectionalGroup} method with substring from index 0 to +,
     * it initializes the IG {@link ArrayList} by parsing given input String IG by + and calling the getMorphologicalTag
     * method with these substrings. If getMorphologicalTag method returns a tag, it adds this tag to the IG {@link ArrayList}
     * and also to the inflectionalGroups {@link ArrayList}.
     * <p>
     * If the first item of iGs {@link ArrayList} does not contain +, it creates a new word with first item and assigns it as root.
     * <p>
     * At the end, it loops through the items of iGs and by calling {@link InflectionalGroup} method with these items
     * it initializes the IG {@link ArrayList} by parsing given input String IG by + and calling the getMorphologicalTag
     * method with these substrings. If getMorphologicalTag method returns a tag, it adds this tag to the IG {@link ArrayList}
     * and also to the inflectionalGroups {@link ArrayList}.
     *
     * @param parse String input.
     */
    public MorphologicalParse(String parse) {
        int i;
        ArrayList<String> iGs;
        iGs = new ArrayList<String>();
        String st = parse;
        while (st.contains("^DB+")) {
            iGs.add(st.substring(0, st.indexOf("^DB+")));
            st = st.substring(st.indexOf("^DB+") + 4);
        }
        iGs.add(st);
        inflectionalGroups = new ArrayList<InflectionalGroup>();
        if (iGs.get(0).equals("++Punc")) {
            root = new Word("+");
            inflectionalGroups.add(new InflectionalGroup("Punc"));
        } else {
            if (iGs.get(0).indexOf('+') != -1) {
                root = new Word(iGs.get(0).substring(0, iGs.get(0).indexOf('+')));
                inflectionalGroups.add(new InflectionalGroup(iGs.get(0).substring(iGs.get(0).indexOf('+') + 1)));
            } else {
                root = new Word(iGs.get(0));
            }
            for (i = 1; i < iGs.size(); i++) {
                inflectionalGroups.add(new InflectionalGroup(iGs.get(i)));
            }
        }
    }

    /**
     * Another constructor of {@link MorphologicalParse} class which takes an {@link ArrayList} inflectionalGroups as an input.
     * First, it initializes inflectionalGroups {@link ArrayList} and if the first item of the {@link ArrayList} has +, it gets
     * the substring from index 0 to + and assigns it as root, and by calling {@link InflectionalGroup} method with substring from index 0 to +,
     * it initializes the IG {@link ArrayList} by parsing given input String IG by + and calling the getMorphologicalTag
     * method with these substrings. If getMorphologicalTag method returns a tag, it adds this tag to the IG {@link ArrayList}
     * and also to the inflectionalGroups {@link ArrayList}. However, if the first item does not contain +, it directly prints out
     * indicating that there is no root for that item of this Inflectional Group.
     * <p>
     * At the end, it loops through the items of inflectionalGroups and by calling {@link InflectionalGroup} method with these items
     * it initializes the IG {@link ArrayList} by parsing given input String IG by + and calling the getMorphologicalTag
     * method with these substrings. If getMorphologicalTag method returns a tag, it adds this tag to the IG {@link ArrayList}
     * and also to the inflectionalGroups {@link ArrayList}.
     *
     * @param inflectionalGroups {@link ArrayList} input.
     */
    public MorphologicalParse(ArrayList<String> inflectionalGroups) {
        int i;
        this.inflectionalGroups = new ArrayList<InflectionalGroup>();
        if (inflectionalGroups.get(0).indexOf('+') != -1) {
            root = new Word(inflectionalGroups.get(0).substring(0, inflectionalGroups.get(0).indexOf('+')));
            this.inflectionalGroups.add(new InflectionalGroup(inflectionalGroups.get(0).substring(inflectionalGroups.get(0).indexOf('+') + 1)));
        } else {
            System.out.println("Root word for " + inflectionalGroups.get(0) + " does not exist\n");
        }
        for (i = 1; i < inflectionalGroups.size(); i++) {
            this.inflectionalGroups.add(new InflectionalGroup(inflectionalGroups.get(i)));
        }
    }

    /**
     * The getTransitionList method gets the first item of inflectionalGroups {@link ArrayList} as a {@link String}, then loops
     * through the items of inflectionalGroups and concatenates them by using +.
     *
     * @return String that contains transition list.
     */
    public String getTransitionList() {
        String result = inflectionalGroups.get(0).toString();
        for (int i = 1; i < inflectionalGroups.size(); i++) {
            result = result + "+" + inflectionalGroups.get(i).toString();
        }
        return result;
    }

    /**
     * The getInflectionalGroupString method takes an {@link Integer} index as an input and if index is 0, it directly returns the
     * root and the first item of inflectionalGroups {@link ArrayList}. If the index is not 0, it then returns the corresponding
     * item of inflectionalGroups {@link ArrayList} as a {@link String}.
     *
     * @param index Integer input.
     * @return corresponding item of inflectionalGroups at given index as a {@link String}.
     */
    public String getInflectionalGroupString(int index) {
        if (index == 0) {
            return root.getName() + "+" + inflectionalGroups.get(0).toString();
        } else {
            return inflectionalGroups.get(index).toString();
        }
    }

    /**
     * The getInflectionalGroup method takes an {@link Integer} index as an input and it directly returns the {@link InflectionalGroup}
     * at given index.
     *
     * @param index Integer input.
     * @return InflectionalGroup at given index.
     */
    public InflectionalGroup getInflectionalGroup(int index) {
        return inflectionalGroups.get(index);
    }

    /**
     * The getLastInflectionalGroup method directly returns the last {@link InflectionalGroup} of inflectionalGroups {@link ArrayList}.
     *
     * @return the last {@link InflectionalGroup} of inflectionalGroups {@link ArrayList}.
     */
    public InflectionalGroup getLastInflectionalGroup() {
        return getInflectionalGroup(inflectionalGroups.size() - 1);
    }

    /**
     * The getTag method takes an {@link Integer} index as an input and and if the given index is 0, it directly return the root.
     * then, it loops through the inflectionalGroups {@link ArrayList} it returns the MorphologicalTag of the corresponding inflectional group.
     *
     * @param index Integer input.
     * @return the MorphologicalTag of the corresponding inflectional group, or null of invalid index inputs.
     */
    public String getTag(int index) {
        int size = 1;
        if (index == 0)
            return root.getName();
        for (InflectionalGroup group : inflectionalGroups) {
            if (index < size + group.size()) {
                return InflectionalGroup.getTag(group.getTag(index - size));
            }
            size += group.size();
        }
        return null;
    }

    /**
     * The tagSize method loops through the inflectionalGroups {@link ArrayList} and accumulates the sizes of each inflectional group
     * in the inflectionalGroups.
     *
     * @return total size of the inflectionalGroups {@link ArrayList}.
     */
    public int tagSize() {
        int size = 1;
        for (InflectionalGroup group : inflectionalGroups) {
            size += group.size();
        }
        return size;
    }

    /**
     * The size method returns the size of the inflectionalGroups {@link ArrayList}.
     *
     * @return the size of the inflectionalGroups {@link ArrayList}.
     */
    public int size() {
        return inflectionalGroups.size();
    }

    /**
     * The firstInflectionalGroup method returns the first inflectional group of inflectionalGroups {@link ArrayList}.
     *
     * @return the first inflectional group of inflectionalGroups {@link ArrayList}.
     */
    public InflectionalGroup firstInflectionalGroup() {
        return inflectionalGroups.get(0);
    }

    /**
     * The lastInflectionalGroup method returns the last inflectional group of inflectionalGroups {@link ArrayList}.
     *
     * @return the last inflectional group of inflectionalGroups {@link ArrayList}.
     */
    public InflectionalGroup lastInflectionalGroup() {
        return inflectionalGroups.get(inflectionalGroups.size() - 1);
    }

    /**
     * The getWordWithPos method returns root with the MorphologicalTag of the first inflectional as a new word.
     *
     * @return root with the MorphologicalTag of the first inflectional as a new word.
     */
    public Word getWordWithPos() {
        return new Word(root.getName() + "+" + InflectionalGroup.getTag(firstInflectionalGroup().getTag(0)));
    }

    /**
     * The getPos method returns the MorphologicalTag of the last inflectional group.
     *
     * @return the MorphologicalTag of the last inflectional group.
     */
    public String getPos() {
        return InflectionalGroup.getTag(lastInflectionalGroup().getTag(0));
    }

    /**
     * The getRootPos method returns the MorphologicalTag of the first inflectional group.
     *
     * @return the MorphologicalTag of the first inflectional group.
     */
    public String getRootPos() {
        return InflectionalGroup.getTag(firstInflectionalGroup().getTag(0));
    }

    /**
     * The lastIGContainsCase method returns the MorphologicalTag of last inflectional group if it is one of the NOMINATIVE,
     * ACCUSATIVE, DATIVE, LOCATIVE or ABLATIVE cases, null otherwise.
     *
     * @return the MorphologicalTag of last inflectional group if it is one of the NOMINATIVE,
     * ACCUSATIVE, DATIVE, LOCATIVE or ABLATIVE cases, null otherwise.
     */
    public String lastIGContainsCase() {
        MorphologicalTag caseTag = lastInflectionalGroup().containsCase();
        if (caseTag != null)
            return InflectionalGroup.getTag(caseTag);
        else
            return "NULL";
    }

    /**
     * The lastIGContainsTag method takes a MorphologicalTag as an input and returns true if the last inflectional group's
     * MorphologicalTag matches with one of the tags in the IG {@link ArrayList}, falze otherwise.
     *
     * @param tag {@link MorphologicalTag} type input.
     * @return true if the last inflectional group's MorphologicalTag matches with one of the tags in the IG {@link ArrayList}, false otherwise.
     */
    public boolean lastIGContainsTag(MorphologicalTag tag) {
        return lastInflectionalGroup().containsTag(tag);
    }

    /**
     * lastIGContainsPossessive method returns true if the last inflectional group contains one of the
     * possessives: P1PL, P1SG, P2PL, P2SG, P3PL AND P3SG, false otherwise.
     *
     * @return true if the last inflectional group contains one of the possessives: P1PL, P1SG, P2PL, P2SG, P3PL AND P3SG, false otherwise.
     */
    public boolean lastIGContainsPossessive() {
        return lastInflectionalGroup().containsPossessive();
    }

    /**
     * The isCapitalWord method returns true if the character at first index o f root is an uppercase letter, false otherwise.
     *
     * @return true if the character at first index o f root is an uppercase letter, false otherwise.
     */
    public boolean isCapitalWord() {
        return Character.isUpperCase(root.getName().charAt(0));
    }

    /**
     * The isNoun method returns true if the past of speech is NOUN, false otherwise.
     *
     * @return true if the past of speech is NOUN, false otherwise.
     */
    public boolean isNoun() {
        return (getPos().equals("NOUN"));
    }

    /**
     * The isVerb method returns true if the past of speech is VERB, false otherwise.
     *
     * @return true if the past of speech is VERB, false otherwise.
     */
    public boolean isVerb() {
        return (getPos().equals("VERB"));
    }

    /**
     * The isRootVerb method returns true if the past of speech of root is BERV, false otherwise.
     *
     * @return true if the past of speech of root is VERB, false otherwise.
     */
    public boolean isRootVerb() {
        return (getRootPos().equals("VERB"));
    }

    /**
     * The isAdjective method returns true if the past of speech is ADJ, false otherwise.
     *
     * @return true if the past of speech is ADJ, false otherwise.
     */
    public boolean isAdjective() {
        return (getPos().equals("ADJ"));
    }

    /**
     * The isProperNoun method returns true if the first inflectional group's MorphologicalTag is a PROPERNOUN, false otherwise.
     *
     * @return true if the first inflectional group's MorphologicalTag is a PROPERNOUN, false otherwise.
     */
    public boolean isProperNoun() {
        return getInflectionalGroup(0).containsTag(MorphologicalTag.PROPERNOUN);
    }

    /**
     * The isPunctuation method returns true if the first inflectional group's MorphologicalTag is a PUNCTUATION, false otherwise.
     *
     * @return true if the first inflectional group's MorphologicalTag is a PUNCTUATION, false otherwise.
     */
    public boolean isPunctuation() {
        return getInflectionalGroup(0).containsTag(MorphologicalTag.PUNCTUATION);
    }

    /**
     * The isCardinal method returns true if the first inflectional group's MorphologicalTag is a CARDINAL, false otherwise.
     *
     * @return true if the first inflectional group's MorphologicalTag is a CARDINAL, false otherwise.
     */
    public boolean isCardinal() {
        return getInflectionalGroup(0).containsTag(MorphologicalTag.CARDINAL);
    }

    /**
     * The isOrdinal method returns true if the first inflectional group's MorphologicalTag is a ORDINAL, false otherwise.
     *
     * @return true if the first inflectional group's MorphologicalTag is a ORDINAL, false otherwise.
     */
    public boolean isOrdinal() {
        return getInflectionalGroup(0).containsTag(MorphologicalTag.ORDINAL);
    }

    /**
     * The isReal method returns true if the first inflectional group's MorphologicalTag is a REAL, false otherwise.
     *
     * @return true if the first inflectional group's MorphologicalTag is a REAL, false otherwise.
     */
    public boolean isReal() {
        return getInflectionalGroup(0).containsTag(MorphologicalTag.REAL);
    }

    /**
     * The isNumber method returns true if the first inflectional group's MorphologicalTag is REAL or CARDINAL, false otherwise.
     *
     * @return true if the first inflectional group's MorphologicalTag is a REAL or CARDINAL, false otherwise.
     */
    public boolean isNumber() {
        return isReal() || isCardinal();
    }

    /**
     * The isTime method returns true if the first inflectional group's MorphologicalTag is a TIME, false otherwise.
     *
     * @return true if the first inflectional group's MorphologicalTag is a TIME, false otherwise.
     */
    public boolean isTime() {
        return getInflectionalGroup(0).containsTag(MorphologicalTag.TIME);
    }

    /**
     * The isDate method returns true if the first inflectional group's MorphologicalTag is a DATE, false otherwise.
     *
     * @return true if the first inflectional group's MorphologicalTag is a DATE, false otherwise.
     */
    public boolean isDate() {
        return getInflectionalGroup(0).containsTag(MorphologicalTag.DATE);
    }

    /**
     * The isHashTag method returns true if the first inflectional group's MorphologicalTag is a HASHTAG, false otherwise.
     *
     * @return true if the first inflectional group's MorphologicalTag is a HASHTAG, false otherwise.
     */
    public boolean isHashTag() {
        return getInflectionalGroup(0).containsTag(MorphologicalTag.HASHTAG);
    }

    /**
     * The isEmail method returns true if the first inflectional group's MorphologicalTag is a EMAIL, false otherwise.
     *
     * @return true if the first inflectional group's MorphologicalTag is a EMAIL, false otherwise.
     */
    public boolean isEmail() {
        return getInflectionalGroup(0).containsTag(MorphologicalTag.EMAIL);
    }

    /**
     * The isPercent method returns true if the first inflectional group's MorphologicalTag is a PERCENT, false otherwise.
     *
     * @return true if the first inflectional group's MorphologicalTag is a PERCENT, false otherwise.
     */
    public boolean isPercent() {
        return getInflectionalGroup(0).containsTag(MorphologicalTag.PERCENT);
    }

    /**
     * The isFraction method returns true if the first inflectional group's MorphologicalTag is a FRACTION, false otherwise.
     *
     * @return true if the first inflectional group's MorphologicalTag is a FRACTION, false otherwise.
     */
    public boolean isFraction() {
        return getInflectionalGroup(0).containsTag(MorphologicalTag.FRACTION);
    }

    /**
     * The isRange method returns true if the first inflectional group's MorphologicalTag is a RANGE, false otherwise.
     *
     * @return true if the first inflectional group's MorphologicalTag is a RANGE, false otherwise.
     */
    public boolean isRange() {
        return getInflectionalGroup(0).containsTag(MorphologicalTag.RANGE);
    }

    /**
     * The isPlural method returns true if {@link InflectionalGroup}'s MorphologicalTags are from the agreement plural
     * or possessive plural, i.e A1PL, A2PL, A3PL, P1PL, P2PL or P3PL, and false otherwise.
     *
     * @return true if {@link InflectionalGroup}'s MorphologicalTags are from the agreement plural or possessive plural.
     */
    public boolean isPlural() {
        for (InflectionalGroup inflectionalGroup : inflectionalGroups)
            if (inflectionalGroup.containsPlural()) {
                return true;
            }
        return false;
    }

    /**
     * The isAuxiliary method returns true if the root equals to the et, ol, or yap, and false otherwise.
     *
     * @return true if the root equals to the et, ol, or yap, and false otherwise.
     */
    public boolean isAuxiliary() {
        return root.getName().equals("et") || root.getName().equals("ol") || root.getName().equals("yap");
    }

    /**
     * The containsTag method takes a MorphologicalTag as an input and loops through the inflectionalGroups {@link ArrayList},
     * returns true if the input matches with on of the tags in the IG, false otherwise.
     *
     * @param tag checked tag
     * @return true if the input matches with on of the tags in the IG, false otherwise.
     */
    public boolean containsTag(MorphologicalTag tag) {
        for (InflectionalGroup inflectionalGroup : inflectionalGroups) {
            if (inflectionalGroup.containsTag(tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The getTreePos method returns the tree pos tag of a morphological analysis.
     *
     * @return Tree pos tag of the morphological analysis in string form.
     */
    public String getTreePos(){
        if (isProperNoun()){
            return "NP";
        } else {
            if (root.getName().equals("değil")){
                return "NEG";
            } else {
                if (isVerb()){
                    if (lastIGContainsTag(MorphologicalTag.ZERO)){
                        return "NOMP";
                    } else {
                        return "VP";
                    }
                } else {
                    if (isAdjective()){
                        return "ADJP";
                    } else {
                        if (isNoun() || isPercent()){
                            return "NP";
                        } else {
                            if (containsTag(MorphologicalTag.ADVERB)){
                                return "ADVP";
                            } else {
                                if (isNumber() || isFraction()){
                                    return "NUM";
                                } else {
                                    if (containsTag(MorphologicalTag.POSTPOSITION)){
                                        return "PP";
                                    } else {
                                        if (containsTag(MorphologicalTag.CONJUNCTION)){
                                            return "CONJP";
                                        } else {
                                            if (containsTag(MorphologicalTag.DETERMINER)){
                                                return "DP";
                                            } else {
                                                if (containsTag(MorphologicalTag.INTERJECTION)){
                                                    return "INTJP";
                                                } else {
                                                    if (containsTag(MorphologicalTag.QUESTIONPRONOUN)){
                                                        return "WP";
                                                    } else {
                                                        if (containsTag(MorphologicalTag.PRONOUN)){
                                                            return "NP";
                                                        } else {
                                                            if (isPunctuation()){
                                                                switch (root.getName()){
                                                                    case "!":
                                                                    case "?":
                                                                        return ".";
                                                                    case ";":
                                                                    case "-":
                                                                    case "--":
                                                                        return ":";
                                                                    case "(":
                                                                    case "-LRB-":
                                                                    case "-lrb-":
                                                                        return "-LRB-";
                                                                    case ")":
                                                                    case "-RRB-":
                                                                    case "-rrb-":
                                                                        return "-RRB-";
                                                                    default:
                                                                        return root.getName();
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
                }
            }
        }
        return "-XXX-";
    }

    private String getPronType(){
        String lemma = root.getName();
        if (containsTag(MorphologicalTag.PERSONALPRONOUN)){
            return "Prs";
        }
        if (lemma.equals("birbiri") || lemma.equals("birbirleri")){
            return "Rcp";
        }
        if (lemma.equals("kim") || lemma.equals("nere") || lemma.equals("ne")
                || lemma.equals("hangi") || lemma.equals("nasıl") || lemma.equals("kaç")
                || lemma.equals("mi") || lemma.equals("mı") || lemma.equals("mu") || lemma.equals("mü")){
            return "Int";
        }
        if (containsTag(MorphologicalTag.DEMONSTRATIVEPRONOUN)){
            return "Dem";
        }
        return null;
    }

    private String getNumType(){
        String lemma = root.getName();
        if (containsTag(MorphologicalTag.CARDINAL) || containsTag(MorphologicalTag.NUMBER) || lemma.equals("kaç")){
            return "Card";
        }
        if (containsTag(MorphologicalTag.ORDINAL) || lemma.equals("kaçıncı")){
            return "Ord";
        }
        if (containsTag(MorphologicalTag.DISTRIBUTIVE)){
            return "Dist";
        }
        return null;
    }

    private String getReflex(){
        String lemma = root.getName();
        if (lemma.equals("kendi")){
            return "Yes";
        }
        return null;
    }

    private String getNumber(){
        if (containsTag(MorphologicalTag.A1SG) || containsTag(MorphologicalTag.A2SG) || containsTag(MorphologicalTag.A3SG)
        || containsTag(MorphologicalTag.P1SG) || containsTag(MorphologicalTag.P2SG) || containsTag(MorphologicalTag.P3SG)){
            return "Sing";
        }
        if (containsTag(MorphologicalTag.A1PL) || containsTag(MorphologicalTag.A2PL) || containsTag(MorphologicalTag.A3PL)
                || containsTag(MorphologicalTag.P1PL) || containsTag(MorphologicalTag.P2PL) || containsTag(MorphologicalTag.P3PL)){
            return "Plur";
        }
        return null;
    }

    private String getCase(){
        if (containsTag(MorphologicalTag.ACCUSATIVE) || containsTag(MorphologicalTag.PCACCUSATIVE)){
            return "Acc";
        }
        if (containsTag(MorphologicalTag.DATIVE) || containsTag(MorphologicalTag.PCDATIVE)){
            return "Dat";
        }
        if (containsTag(MorphologicalTag.GENITIVE) || containsTag(MorphologicalTag.PCGENITIVE)){
            return "Gen";
        }
        if (containsTag(MorphologicalTag.LOCATIVE)){
            return "Loc";
        }
        if (containsTag(MorphologicalTag.INSTRUMENTAL) || containsTag(MorphologicalTag.PCINSTRUMENTAL)){
            return "Ins";
        }
        if (containsTag(MorphologicalTag.ABLATIVE) || containsTag(MorphologicalTag.PCABLATIVE)){
            return "Abl";
        }
        if (containsTag(MorphologicalTag.NOMINATIVE) || containsTag(MorphologicalTag.PCNOMINATIVE)){
            return "Nom";
        }
        return null;
    }

    private String getDefinite(){
        String lemma = root.getName();
        if (containsTag(MorphologicalTag.DETERMINER)){
            if (lemma.equals("bir") || lemma.equals("bazı") || lemma.equals("birkaç")){
                return "Ind";
            }
            if (lemma.equals("her") || lemma.equals("bu") || lemma.equals("şu") || lemma.equals("o") || lemma.equals("bütün")){
                return "Def";
            }
        }
        return null;
    }

    private String getDegree(){
        String lemma = root.getName();
        if (lemma.equals("daha")){
            return "Cmp";
        }
        if (lemma.equals("en") && !isNoun()){
            return "Sup";
        }
        return null;
    }

    private String getPolarity(){
        if (containsTag(MorphologicalTag.POSITIVE)){
            return "Pos";
        }
        if (containsTag(MorphologicalTag.NEGATIVE)){
            return "Neg";
        }
        return null;
    }

    private String getPerson(){
        if (containsTag(MorphologicalTag.A1SG) || containsTag(MorphologicalTag.A1PL)
                || containsTag(MorphologicalTag.P1SG) || containsTag(MorphologicalTag.P1PL)){
            return "1";
        }
        if (containsTag(MorphologicalTag.A2SG) || containsTag(MorphologicalTag.A2PL)
                || containsTag(MorphologicalTag.P2SG) || containsTag(MorphologicalTag.P2PL)){
            return "2";
        }
        if (containsTag(MorphologicalTag.A3SG) || containsTag(MorphologicalTag.A3PL)
                || containsTag(MorphologicalTag.P3SG) || containsTag(MorphologicalTag.P3PL)){
            return "3";
        }
        return null;
    }

    private String getVoice(){
        if (containsTag(MorphologicalTag.PASSIVE)){
            return "Pass";
        }
        if (containsTag(MorphologicalTag.RECIPROCAL)){
            return "Rcp";
        }
        if (containsTag(MorphologicalTag.CAUSATIVE)){
            return "Cau";
        }
        if (containsTag(MorphologicalTag.REFLEXIVE)){
            return "Rfl";
        }
        return null;
    }

    private String getAspect(){
        if (containsTag(MorphologicalTag.PASTTENSE) || containsTag(MorphologicalTag.NARRATIVE) || containsTag(MorphologicalTag.FUTURE)){
            return "Perf";
        }
        if (containsTag(MorphologicalTag.PROGRESSIVE1) || containsTag(MorphologicalTag.PROGRESSIVE2)){
            return "Prog";
        }
        if (containsTag(MorphologicalTag.AORIST)){
            return "Hab";
        }
        if (containsTag(MorphologicalTag.HASTILY)){
            return "Rapid";
        }
        if (containsTag(MorphologicalTag.START) || containsTag(MorphologicalTag.STAY) || containsTag(MorphologicalTag.REPEAT)){
            return "Dur";
        }
        return null;
    }

    private String getTense(){
        if (containsTag(MorphologicalTag.PASTTENSE)){
            return "Past";
        }
        if (containsTag(MorphologicalTag.FUTURE)){
            return "Fut";
        }
        if (containsTag(MorphologicalTag.NARRATIVE) && containsTag(MorphologicalTag.PASTTENSE)){
            return "Pqp";
        }
        if (!containsTag(MorphologicalTag.PASTTENSE) && !containsTag(MorphologicalTag.FUTURE)){
            return "Pres";
        }
        return null;
    }

    private String getMood(){
        if (containsTag(MorphologicalTag.IMPERATIVE)){
            return "Imp";
        }
        if (containsTag(MorphologicalTag.CONDITIONAL)){
            return "Cnd";
        }
        if (containsTag(MorphologicalTag.DESIRE)){
            return "Des";
        }
        if (containsTag(MorphologicalTag.OPTATIVE)){
            return "Opt";
        }
        if (containsTag(MorphologicalTag.NECESSITY)){
            return "Nec";
        }
        if (containsTag(MorphologicalTag.PASTTENSE) || containsTag(MorphologicalTag.PROGRESSIVE1) || containsTag(MorphologicalTag.FUTURE)){
            return "Ind";
        }
        return null;
    }

    private String getVerbForm(){
        if (containsTag(MorphologicalTag.PASTPARTICIPLE) || containsTag(MorphologicalTag.FUTUREPARTICIPLE) || containsTag(MorphologicalTag.PRESENTPARTICIPLE)){
            return "Part";
        }
        if (containsTag(MorphologicalTag.INFINITIVE) || containsTag(MorphologicalTag.INFINITIVE2)){
            return "Vnoun";
        }
        if (containsTag(MorphologicalTag.SINCEDOINGSO) || containsTag(MorphologicalTag.WITHOUTHAVINGDONESO) || containsTag(MorphologicalTag.WITHOUTBEINGABLETOHAVEDONESO) || containsTag(MorphologicalTag.BYDOINGSO) || containsTag(MorphologicalTag.AFTERDOINGSO) || containsTag(MorphologicalTag.INFINITIVE3)){
            return "Conv";
        }
        if (containsTag(MorphologicalTag.AORIST) || containsTag(MorphologicalTag.PASTTENSE) || containsTag(MorphologicalTag.PROGRESSIVE1) || containsTag(MorphologicalTag.FUTURE)){
            return "Fin";
        }
        return null;
    }

    public ArrayList<String> getUniversalDependencyFeatures(){
        ArrayList<String> featureList = new ArrayList<>();
        String pronType = getPronType();
        if (pronType != null){
            featureList.add("PronType=" + pronType);
        }
        String numType = getNumType();
        if (numType != null){
            featureList.add("NumType=" + numType);
        }
        String reflex = getReflex();
        if (reflex != null){
            featureList.add("Reflex=" + reflex);
        }
        String degree = getDegree();
        if (degree != null){
            featureList.add("Degree=" + degree);
        }
        if (isNoun() || isVerb()){
            String number = getNumber();
            if (number != null){
                featureList.add("Number=" + number);
            }
        }
        if (isNoun()) {
            String case_ = getCase();
            if (case_ != null){
                featureList.add("Case=" + case_);
            }
        }
        if (containsTag(MorphologicalTag.DETERMINER)){
            String definite = getDefinite();
            if (definite != null){
                featureList.add("Definite=" + definite);
            }
        }
        if (isVerb()){
            String polarity = getPolarity();
            if (polarity != null){
                featureList.add("Polarity=" + polarity);
            }
            String person = getPerson();
            if (person != null){
                featureList.add("Person=" + person);
            }
            String voice = getVoice();
            if (voice != null){
                featureList.add("Voice=" + voice);
            }
            String aspect = getAspect();
            if (aspect != null){
                featureList.add("Aspect=" + aspect);
            }
            String tense = getTense();
            if (tense != null){
                featureList.add("Tense=" + tense);
            }
            String mood = getMood();
            if (mood != null){
                featureList.add("Mood=" + mood);
            }
            String verbForm = getVerbForm();
            if (verbForm != null){
                featureList.add("VerbForm=" + verbForm);
            }
        }
        featureList.sort(Comparator.comparing(String::toLowerCase));
        return featureList;
    }

    public String getUniversalDependencyPos(){
        String lemma = root.getName();
        if (lemma.equals("değil")){
            return "AUX";
        }
        if (isProperNoun()){
            return "PROPN";
        }
        if (isNoun()){
            return "NOUN";
        }
        if (isAdjective()){
            return "ADJ";
        }
        if (getPos().equals("ADV")){
            return "ADV";
        }
        if (containsTag(MorphologicalTag.INTERJECTION)){
            return "INTJ";
        }
        if (isVerb()){
            return "VERB";
        }
        if (isPunctuation()){
            return "PUNCT";
        }
        if (containsTag(MorphologicalTag.DETERMINER)){
            return "DET";
        }
        if (isNumber() || isDate() || isTime() || isOrdinal() || isFraction() || lemma.equals("%")){
            return "NUM";
        }
        if (getPos().equals("PRON")){
            return "PRON";
        }
        if (getPos().equals("POSTP")){
            return "ADP";
        }
        if (getPos().equals("QUES")){
            return "AUX";
        }
        if (getPos().equals("CONJ")){
            if (lemma.equals("ki") || lemma.equals("eğer") || lemma.equals("diye")){
                return "SCONJ";
            } else {
                return "CCONJ";
            }
        }
        return "X";
    }

    /**
     * The overridden toString method gets the root and the first inflectional group as a result {@link String} then concatenates
     * with ^DB+ and the following inflectional groups.
     *
     * @return result {@link String}.
     */
    public String toString() {
        String result = root.getName() + "+" + inflectionalGroups.get(0).toString();
        for (int i = 1; i < inflectionalGroups.size(); i++)
            result = result + "^DB+" + inflectionalGroups.get(i).toString();
        return result;
    }

}
