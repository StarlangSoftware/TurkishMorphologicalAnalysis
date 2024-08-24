package MorphologicalAnalysis;

import Dictionary.Word;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

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
        iGs = new ArrayList<>();
        String st = parse;
        while (st.contains("^DB+")) {
            iGs.add(st.substring(0, st.indexOf("^DB+")));
            st = st.substring(st.indexOf("^DB+") + 4);
        }
        iGs.add(st);
        inflectionalGroups = new ArrayList<>();
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
        this.inflectionalGroups = new ArrayList<>();
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
        StringBuilder result = new StringBuilder(inflectionalGroups.get(0).toString());
        for (int i = 1; i < inflectionalGroups.size(); i++) {
            result.append("+").append(inflectionalGroups.get(i).toString());
        }
        return result.toString();
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
     * MorphologicalTag matches with one of the tags in the IG {@link ArrayList}, false otherwise.
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
     * The isNoun method returns true if the part of speech is NOUN, false otherwise.
     *
     * @return true if the part of speech is NOUN, false otherwise.
     */
    public boolean isNoun() {
        return (getPos().equals("NOUN"));
    }

    /**
     * The isVerb method returns true if the part of speech is VERB, false otherwise.
     *
     * @return true if the part of speech is VERB, false otherwise.
     */
    public boolean isVerb() {
        return (getPos().equals("VERB"));
    }

    /**
     * The isRootVerb method returns true if the part of speech of root is BERV, false otherwise.
     *
     * @return true if the part of speech of root is VERB, false otherwise.
     */
    public boolean isRootVerb() {
        return (getRootPos().equals("VERB"));
    }

    /**
     * The isAdjective method returns true if the part of speech is ADJ, false otherwise.
     *
     * @return true if the part of speech is ADJ, false otherwise.
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
        for (InflectionalGroup inflectionalGroup : inflectionalGroups){
            if (inflectionalGroup.containsPlural()) {
                return true;
            }
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

    /**
     * Returns the pronoun type of the parse for universal dependency feature ProType.
     * @return "Art" if the pronoun is also a determiner; "Prs" if the pronoun is personal pronoun; "Rcp" if the
     * pronoun is 'birbiri'; "Ind" if the pronoun is an indeterminate pronoun; "Neg" if the pronoun is 'hiçbiri';
     * "Int" if the pronoun is a question pronoun; "Dem" if the pronoun is a demonstrative pronoun.
     */
    private String getPronType(){
        String lemma = root.getName();
        if (containsTag(MorphologicalTag.DETERMINER)){
            return "Art";
        }
        if (lemma.equals("kendi") || containsTag(MorphologicalTag.PERSONALPRONOUN)){
            return "Prs";
        }
        if (lemma.equals("birbiri") || lemma.equals("birbirleri")){
            return "Rcp";
        }
        if (lemma.equals("birçoğu") || lemma.equals("hep") || lemma.equals("kimse")
                || lemma.equals("bazı") || lemma.equals("biri") || lemma.equals("çoğu")
                || lemma.equals("hepsi") || lemma.equals("diğeri") || lemma.equals("tümü")
                || lemma.equals("herkes") || lemma.equals("kimi") || lemma.equals("öbür")
                || lemma.equals("öteki") || lemma.equals("birkaçı") || lemma.equals("topu")
                || lemma.equals("başkası")){
            return "Ind";
        }
        if (lemma.equals("hiçbiri")){
            return "Neg";
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

    /**
     * Returns the numeral type of the parse for universal dependency feature NumType.
     * @return "Ord" if the parse is Time, Ordinal or the word is '%' or 'kaçıncı'; "Dist" if the word is a
     * distributive number such as 'beşinci'; "Card" if the number is cardinal or any number or the word is 'kaç'.
     */
    private String getNumType(){
        String lemma = root.getName();
        if (lemma.equals("%") || containsTag(MorphologicalTag.TIME)){
            return "Ord";
        }
        if (containsTag(MorphologicalTag.ORDINAL) || lemma.equals("kaçıncı")){
            return "Ord";
        }
        if (containsTag(MorphologicalTag.DISTRIBUTIVE)){
            return "Dist";
        }
        if (containsTag(MorphologicalTag.CARDINAL) || containsTag(MorphologicalTag.NUMBER) || lemma.equals("kaç")){
            return "Card";
        }
        return null;
    }

    /**
     * Returns the value for the dependency feature Reflex.
     * @return "Yes" if the root word is 'kendi', null otherwise.
     */
    private String getReflex(){
        String lemma = root.getName();
        if (lemma.equals("kendi")){
            return "Yes";
        }
        return null;
    }

    /**
     * Returns the agreement of the parse for the universal dependency feature Number.
     * @return "Sing" if the agreement of the parse is singular (contains A1SG, A2SG, A3SG); "Plur" if the agreement
     * of the parse is plural (contains A1PL, A2PL, A3PL).
     */
    private String getNumber(){
        if (containsTag(MorphologicalTag.A1SG) || containsTag(MorphologicalTag.A2SG) || containsTag(MorphologicalTag.A3SG)){
            return "Sing";
        }
        if (containsTag(MorphologicalTag.A1PL) || containsTag(MorphologicalTag.A2PL) || containsTag(MorphologicalTag.A3PL)){
            return "Plur";
        }
        return null;
    }

    /**
     * Returns the possessive agreement of the parse for the universal dependency feature [Pos].
     * @return "Sing" if the possessive agreement of the parse is singular (contains P1SG, P2SG, P3SG); "Plur" if the
     * possessive agreement of the parse is plural (contains P1PL, P2PL, P3PL).
     */
    private String getPossessiveNumber(){
        if (containsTag(MorphologicalTag.P1SG) || containsTag(MorphologicalTag.P2SG) || containsTag(MorphologicalTag.P3SG)){
            return "Sing";
        }
        if (containsTag(MorphologicalTag.P1PL) || containsTag(MorphologicalTag.P2PL) || containsTag(MorphologicalTag.P3PL)){
            return "Plur";
        }
        return null;
    }

    /**
     * Returns the case marking of the parse for the universal dependency feature case.
     * @return "Acc" for accusative marker; "Dat" for dative marker; "Gen" for genitive marker; "Loc" for locative
     * marker; "Ins" for instrumentative marker; "Abl" for ablative marker; "Nom" for nominative marker.
     */
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
        if (containsTag(MorphologicalTag.EQUATIVE)){
            return "Equ";
        }
        if (containsTag(MorphologicalTag.NOMINATIVE) || containsTag(MorphologicalTag.PCNOMINATIVE)){
            return "Nom";
        }
        return null;
    }

    /**
     * Returns the definiteness of the parse for the universal dependency feature definite. It applies only for
     * determiners in Turkish.
     * @return "Ind" for 'bir', 'bazı', or 'birkaç'. "Def" for 'her', 'bu', 'şu', 'o', 'bütün'.
     */
    private String getDefinite(){
        String lemma = root.getName();
        if (containsTag(MorphologicalTag.DETERMINER)){
            if (lemma.equals("bir") || lemma.equals("bazı") || lemma.equals("birkaç") || lemma.equals("birçok") || lemma.equals("kimi")){
                return "Ind";
            }
            if (lemma.equals("her") || lemma.equals("hangi") || lemma.equals("bu") || lemma.equals("şu") || lemma.equals("o") || lemma.equals("bütün")){
                return "Def";
            }
        }
        return null;
    }

    /**
     * Returns the degree of the parse for the universal dependency feature degree.
     * @return "Cmp" for comparative adverb 'daha'; "Sup" for superlative adjective or adverb 'en'.
     */
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

    /**
     * Returns the polarity of the verb for the universal dependency feature polarity.
     * @return "Pos" for positive polarity containing tag POS; "Neg" for negative polarity containing tag NEG.
     */
    private String getPolarity(){
        if (root.getName().equals("değil")){
            return "Neg";
        }
        if (containsTag(MorphologicalTag.POSITIVE)){
            return "Pos";
        }
        if (containsTag(MorphologicalTag.NEGATIVE)){
            return "Neg";
        }
        return null;
    }

    /**
     * Returns the person of the agreement of the parse for the universal dependency feature person.
     * @return "1" for first person; "2" for second person; "3" for third person.
     */
    private String getPerson(){
        if (containsTag(MorphologicalTag.A1SG) || containsTag(MorphologicalTag.A1PL)){
            return "1";
        }
        if (containsTag(MorphologicalTag.A2SG) || containsTag(MorphologicalTag.A2PL)){
            return "2";
        }
        if (containsTag(MorphologicalTag.A3SG) || containsTag(MorphologicalTag.A3PL)){
            return "3";
        }
        return null;
    }

    /**
     * Returns the person of the possessive agreement of the parse for the universal dependency feature [pos].
     * @return "1" for first person; "2" for second person; "3" for third person.
     */
    private String getPossessivePerson(){
        if (containsTag(MorphologicalTag.P1SG) || containsTag(MorphologicalTag.P1PL)){
            return "1";
        }
        if (containsTag(MorphologicalTag.P2SG) || containsTag(MorphologicalTag.P2PL)){
            return "2";
        }
        if (containsTag(MorphologicalTag.P3SG) || containsTag(MorphologicalTag.P3PL)){
            return "3";
        }
        return null;
    }

    /**
     * Returns the voice of the verb parse for the universal dependency feature voice.
     * @return "CauPass" if the verb parse is both causative and passive; "Pass" if the verb parse is only passive;
     * "Rcp" if the verb parse is reciprocal; "Cau" if the verb parse is only causative; "Rfl" if the verb parse is
     * reflexive.
     */
    private String getVoice(){
        if (containsTag(MorphologicalTag.CAUSATIVE) && containsTag(MorphologicalTag.PASSIVE)){
            return "CauPass";
        }
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

    /**
     * Returns the aspect of the verb parse for the universal dependency feature aspect.
     * @return "Perf" for past, narrative and future tenses; "Prog" for progressive tenses; "Hab" for Aorist; "Rapid"
     * for parses containing HASTILY tag; "Dur" for parses containing START, STAY or REPEAT tags.
     */
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

    /**
     * Returns the tense of the verb parse for universal dependency feature tense.
     * @return "Past" for simple past tense; "Fut" for future tense; "Pqp" for narrative past tense; "Pres" for other
     * past tenses.
     */
    private String getTense(){
        if (containsTag(MorphologicalTag.NARRATIVE) && containsTag(MorphologicalTag.PASTTENSE)){
            return "Pqp";
        }
        if (containsTag(MorphologicalTag.PASTTENSE) || containsTag(MorphologicalTag.NARRATIVE)){
            return "Past";
        }
        if (containsTag(MorphologicalTag.FUTURE)){
            return "Fut";
        }
        if (!containsTag(MorphologicalTag.PASTTENSE) && !containsTag(MorphologicalTag.FUTURE)){
            return "Pres";
        }
        return null;
    }

    /**
     * Returns the modality of the verb parse for the universal dependency feature mood.
     * @return "GenNecPot" if both necessitative and potential is combined with a suffix of general modality;
     * "CndGenPot" if both conditional and potential is combined with a suffix of general modality;
     * "GenNec" if necessitative is combined with a suffix of general modality;
     * "GenPot" if potential is combined with a suffix of general modality;
     * "NecPot" if necessitative is combined with potential;
     * "DesPot" if desiderative is combined with potential;
     * "CndPot" if conditional is combined with potential;
     * "CndGen" if conditional is combined with a suffix of general modality;
     * "Imp" for imperative; "Cnd" for simple conditional; "Des" for simple desiderative; "Opt" for optative; "Nec" for
     * simple necessitative; "Pot" for simple potential; "Gen" for simple suffix of a general modality.
     */
    private String getMood(){
        if ((containsTag(MorphologicalTag.COPULA) || containsTag(MorphologicalTag.AORIST)) && containsTag(MorphologicalTag.NECESSITY) && containsTag(MorphologicalTag.ABLE)){
            return "GenNecPot";
        }
        if ((containsTag(MorphologicalTag.COPULA) || containsTag(MorphologicalTag.AORIST)) && containsTag(MorphologicalTag.CONDITIONAL) && containsTag(MorphologicalTag.ABLE)){
            return "CndGenPot";
        }
        if ((containsTag(MorphologicalTag.COPULA) || containsTag(MorphologicalTag.AORIST)) && containsTag(MorphologicalTag.NECESSITY)){
            return "GenNec";
        }
        if ((containsTag(MorphologicalTag.COPULA) || containsTag(MorphologicalTag.AORIST)) && containsTag(MorphologicalTag.ABLE)){
            return "GenPot";
        }
        if (containsTag(MorphologicalTag.NECESSITY) && containsTag(MorphologicalTag.ABLE)){
            return "NecPot";
        }
        if (containsTag(MorphologicalTag.DESIRE) && containsTag(MorphologicalTag.ABLE)){
            return "DesPot";
        }
        if (containsTag(MorphologicalTag.CONDITIONAL) && containsTag(MorphologicalTag.ABLE)){
            return "CndPot";
        }
        if (containsTag(MorphologicalTag.CONDITIONAL) && (containsTag(MorphologicalTag.COPULA) || containsTag(MorphologicalTag.AORIST))){
            return "CndGen";
        }
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
        if (containsTag(MorphologicalTag.ABLE)){
            return "Pot";
        }
        if (containsTag(MorphologicalTag.PASTTENSE) || containsTag(MorphologicalTag.NARRATIVE) || containsTag(MorphologicalTag.PROGRESSIVE1) || containsTag(MorphologicalTag.PROGRESSIVE2) || containsTag(MorphologicalTag.FUTURE)){
            return "Ind";
        }
        if ((containsTag(MorphologicalTag.COPULA) || containsTag(MorphologicalTag.AORIST))){
            return "Gen";
        }
        if (containsTag(MorphologicalTag.ZERO) && !containsTag(MorphologicalTag.A3PL)){
            return "Gen";
        }
        return null;
    }

    /**
     * Returns the form of the verb parse for the universal dependency feature verbForm.
     * @return "Part" for participles; "Vnoun" for infinitives; "Conv" for parses contaning tags SINCEDOINGSO,
     * WITHOUTHAVINGDONESO, WITHOUTBEINGABLETOHAVEDONESO, BYDOINGSO, AFTERDOINGSO, INFINITIVE3; "Fin" for others.
     */
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
        if (containsTag(MorphologicalTag.COPULA) || containsTag(MorphologicalTag.ABLE) || containsTag(MorphologicalTag.AORIST) || containsTag(MorphologicalTag.PROGRESSIVE2)
                || containsTag(MorphologicalTag.DESIRE) || containsTag(MorphologicalTag.NECESSITY) || containsTag(MorphologicalTag.CONDITIONAL) || containsTag(MorphologicalTag.IMPERATIVE) || containsTag(MorphologicalTag.OPTATIVE)
                || containsTag(MorphologicalTag.PASTTENSE) || containsTag(MorphologicalTag.NARRATIVE) || containsTag(MorphologicalTag.PROGRESSIVE1) || containsTag(MorphologicalTag.FUTURE)
                || (containsTag(MorphologicalTag.ZERO) && !containsTag(MorphologicalTag.A3PL))){
            return "Fin";
        }
        return null;
    }

    private String getEvident(){
        if (containsTag(MorphologicalTag.NARRATIVE)){
            return "Nfh";
        } else {
            if (containsTag(MorphologicalTag.COPULA) || containsTag(MorphologicalTag.ABLE) || containsTag(MorphologicalTag.AORIST) || containsTag(MorphologicalTag.PROGRESSIVE2)
                    || containsTag(MorphologicalTag.DESIRE) || containsTag(MorphologicalTag.NECESSITY) || containsTag(MorphologicalTag.CONDITIONAL) || containsTag(MorphologicalTag.IMPERATIVE) || containsTag(MorphologicalTag.OPTATIVE)
                    || containsTag(MorphologicalTag.PASTTENSE) || containsTag(MorphologicalTag.NARRATIVE) || containsTag(MorphologicalTag.PROGRESSIVE1) || containsTag(MorphologicalTag.FUTURE)) {
                return "Fh";
            }
        }
        return null;
    }

    /**
     * Construct the universal dependency features as an array of strings. Each element represents a single feature.
     * Every feature is given as featureType = featureValue.
     * @param uPos Universal dependency part of speech tag for the parse.
     * @return An array of universal dependency features for this parse.
     */
    public ArrayList<String> getUniversalDependencyFeatures(String uPos){
        ArrayList<String> featureList = new ArrayList<>();
        String pronType = getPronType();
        if (pronType != null && !uPos.equalsIgnoreCase("NOUN") && !uPos.equalsIgnoreCase("ADJ") && !uPos.equalsIgnoreCase("VERB") && !uPos.equalsIgnoreCase("CCONJ") && !uPos.equalsIgnoreCase("PROPN")){
            featureList.add("PronType=" + pronType);
        }
        String numType = getNumType();
        if (numType != null && !uPos.equalsIgnoreCase("VERB") && !uPos.equalsIgnoreCase("NOUN") && !uPos.equalsIgnoreCase("ADV")){
            featureList.add("NumType=" + numType);
        }
        String reflex = getReflex();
        if (reflex != null && !uPos.equalsIgnoreCase("ADJ") && !uPos.equalsIgnoreCase("VERB")){
            featureList.add("Reflex=" + reflex);
        }
        String degree = getDegree();
        if (degree != null && !uPos.equalsIgnoreCase("ADJ")){
            featureList.add("Degree=" + degree);
        }
        if (isNoun() || isVerb() || root.getName().equals("mi") || (pronType != null && !pronType.equals("Art"))){
            String number = getNumber();
            if (number != null){
                featureList.add("Number=" + number);
            }
            String possessiveNumber = getPossessiveNumber();
            if (possessiveNumber != null){
                featureList.add("Number[psor]=" + possessiveNumber);
            }
            String person = getPerson();
            if (person != null && !uPos.equalsIgnoreCase("PROPN")){
                featureList.add("Person=" + person);
            }
            String possessivePerson = getPossessivePerson();
            if (possessivePerson != null && !uPos.equalsIgnoreCase("PROPN")){
                featureList.add("Person[psor]=" + possessivePerson);
            }
        }
        if (isNoun() || (pronType != null && !pronType.equals("Art"))) {
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
        if (isVerb() || root.getName().equals("mi")){
            String polarity = getPolarity();
            if (polarity != null){
                featureList.add("Polarity=" + polarity);
            }
            String voice = getVoice();
            if (voice != null && !root.getName().equals("mi")){
                featureList.add("Voice=" + voice);
            }
            String aspect = getAspect();
            if (aspect != null && !uPos.equalsIgnoreCase("PROPN") && !root.getName().equals("mi")){
                featureList.add("Aspect=" + aspect);
            }
            String tense = getTense();
            if (tense != null && !uPos.equalsIgnoreCase("PROPN")){
                featureList.add("Tense=" + tense);
            }
            String mood = getMood();
            if (mood != null && !uPos.equalsIgnoreCase("PROPN") && !root.getName().equals("mi")){
                featureList.add("Mood=" + mood);
            }
            String verbForm = getVerbForm();
            if (verbForm != null && !uPos.equalsIgnoreCase("PROPN")){
                featureList.add("VerbForm=" + verbForm);
            }
            String evident = getEvident();
            if (evident != null && !root.getName().equals("mi")){
                featureList.add("Evident=" + evident);
            }
        }
        featureList.sort(Comparator.comparing(String::toLowerCase));
        return featureList;
    }

    /**
     * Returns the universal dependency part of speech for this parse.
     * @return "AUX" for word 'değil; "PROPN" for proper nouns; "NOUN for nouns; "ADJ" for adjectives; "ADV" for
     * adverbs; "INTJ" for interjections; "VERB" for verbs; "PUNCT" for punctuation symbols; "DET" for determiners;
     * "NUM" for numerals; "PRON" for pronouns; "ADP" for post participles; "SCONJ" or "CCONJ" for conjunctions.
     */
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
        if (isPunctuation() || isHashTag()){
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
        StringBuilder result = new StringBuilder(root.getName() + "+" + inflectionalGroups.get(0).toString());
        for (int i = 1; i < inflectionalGroups.size(); i++)
            result.append("^DB+").append(inflectionalGroups.get(i).toString());
        return result.toString();
    }

}
