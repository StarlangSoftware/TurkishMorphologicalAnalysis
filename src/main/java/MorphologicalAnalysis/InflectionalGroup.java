package MorphologicalAnalysis;

import java.io.Serializable;
import java.util.ArrayList;

public class InflectionalGroup implements Serializable {
    private final ArrayList<MorphologicalTag> IG;
    public static final String[] tags = {"NOUN", "ADV", "ADJ", "VERB", "A1SG",
            "A2SG", "A3SG", "A1PL", "A2PL", "A3PL",
            "P1SG", "P2SG", "P3SG", "P1PL", "P2PL",
            "P3PL", "PROP", "PNON", "NOM", "WITH",
            "WITHOUT", "ACC", "DAT", "GEN", "ABL",
            "ZERO", "ABLE", "NEG", "PAST",
            "CONJ", "DET", "DUP", "INTERJ", "NUM",
            "POSTP", "PUNC", "QUES", "AGT", "BYDOINGSO",
            "CARD", "CAUS", "DEMONSP", "DISTRIB", "FITFOR",
            "FUTPART", "INF", "NESS", "ORD", "PASS",
            "PASTPART", "PRESPART", "QUESP", "QUANTP", "RANGE",
            "RATIO", "REAL", "RECIP", "REFLEX", "REFLEXP",
            "TIME", "WHEN", "WHILE", "WITHOUTHAVINGDONESO", "PCABL",
            "PCACC", "PCDAT", "PCGEN", "PCINS", "PCNOM",
            "ACQUIRE", "ACTOF", "AFTERDOINGSO", "ALMOST", "AS",
            "ASIF", "BECOME", "EVERSINCE", "FEELLIKE", "HASTILY",
            "INBETWEEN", "JUSTLIKE", "LY", "NOTABLESTATE", "RELATED",
            "REPEAT", "SINCE", "SINCEDOINGSO", "START", "STAY",
            "EQU", "INS", "AOR", "DESR", "FUT",
            "IMP", "NARR", "NECES", "OPT", "PAST",
            "PRES", "PROG1", "PROG2", "COND", "COP",
            "POS", "PRON", "LOC", "REL", "DEMONS",
            "INF2", "INF3", "BSTAG", "ESTAG", "BTTAG",
            "ETTAG", "BDTAG", "EDTAG", "INF1", "ASLONGAS",
            "DIST", "ADAMANTLY", "PERCENT", "WITHOUTBEINGABLETOHAVEDONESO", "DIM",
            "PERS", "FRACTION", "HASHTAG", "EMAIL", "DATE",
            "CODE", "METRIC", "POL", "URGE"};
    public static final MorphologicalTag[] morphoTags = {MorphologicalTag.NOUN, MorphologicalTag.ADVERB, MorphologicalTag.ADJECTIVE,
            MorphologicalTag.VERB, MorphologicalTag.A1SG, MorphologicalTag.A2SG, MorphologicalTag.A3SG, MorphologicalTag.A1PL,
            MorphologicalTag.A2PL, MorphologicalTag.A3PL, MorphologicalTag.P1SG, MorphologicalTag.P2SG, MorphologicalTag.P3SG, MorphologicalTag.P1PL,
            MorphologicalTag.P2PL, MorphologicalTag.P3PL, MorphologicalTag.PROPERNOUN, MorphologicalTag.PNON, MorphologicalTag.NOMINATIVE,
            MorphologicalTag.WITH, MorphologicalTag.WITHOUT, MorphologicalTag.ACCUSATIVE, MorphologicalTag.DATIVE, MorphologicalTag.GENITIVE,
            MorphologicalTag.ABLATIVE, MorphologicalTag.ZERO, MorphologicalTag.ABLE, MorphologicalTag.NEGATIVE, MorphologicalTag.PASTTENSE,
            MorphologicalTag.CONJUNCTION, MorphologicalTag.DETERMINER, MorphologicalTag.DUPLICATION, MorphologicalTag.INTERJECTION, MorphologicalTag.NUMBER,
            MorphologicalTag.POSTPOSITION, MorphologicalTag.PUNCTUATION, MorphologicalTag.QUESTION, MorphologicalTag.AGENT, MorphologicalTag.BYDOINGSO,
            MorphologicalTag.CARDINAL, MorphologicalTag.CAUSATIVE, MorphologicalTag.DEMONSTRATIVEPRONOUN, MorphologicalTag.DISTRIBUTIVE, MorphologicalTag.FITFOR,
            MorphologicalTag.FUTUREPARTICIPLE, MorphologicalTag.INFINITIVE, MorphologicalTag.NESS, MorphologicalTag.ORDINAL, MorphologicalTag.PASSIVE,
            MorphologicalTag.PASTPARTICIPLE, MorphologicalTag.PRESENTPARTICIPLE, MorphologicalTag.QUESTIONPRONOUN, MorphologicalTag.QUANTITATIVEPRONOUN, MorphologicalTag.RANGE,
            MorphologicalTag.RATIO, MorphologicalTag.REAL, MorphologicalTag.RECIPROCAL, MorphologicalTag.REFLEXIVE, MorphologicalTag.REFLEXIVEPRONOUN,
            MorphologicalTag.TIME, MorphologicalTag.WHEN, MorphologicalTag.WHILE, MorphologicalTag.WITHOUTHAVINGDONESO, MorphologicalTag.PCABLATIVE,
            MorphologicalTag.PCACCUSATIVE, MorphologicalTag.PCDATIVE, MorphologicalTag.PCGENITIVE, MorphologicalTag.PCINSTRUMENTAL, MorphologicalTag.PCNOMINATIVE,
            MorphologicalTag.ACQUIRE, MorphologicalTag.ACTOF, MorphologicalTag.AFTERDOINGSO, MorphologicalTag.ALMOST, MorphologicalTag.AS,
            MorphologicalTag.ASIF, MorphologicalTag.BECOME, MorphologicalTag.EVERSINCE, MorphologicalTag.FEELLIKE, MorphologicalTag.HASTILY,
            MorphologicalTag.INBETWEEN, MorphologicalTag.JUSTLIKE, MorphologicalTag.LY, MorphologicalTag.NOTABLESTATE, MorphologicalTag.RELATED,
            MorphologicalTag.REPEAT, MorphologicalTag.SINCE, MorphologicalTag.SINCEDOINGSO, MorphologicalTag.START, MorphologicalTag.STAY,
            MorphologicalTag.EQUATIVE, MorphologicalTag.INSTRUMENTAL, MorphologicalTag.AORIST, MorphologicalTag.DESIRE, MorphologicalTag.FUTURE,
            MorphologicalTag.IMPERATIVE, MorphologicalTag.NARRATIVE, MorphologicalTag.NECESSITY, MorphologicalTag.OPTATIVE, MorphologicalTag.PAST,
            MorphologicalTag.PRESENT, MorphologicalTag.PROGRESSIVE1, MorphologicalTag.PROGRESSIVE2, MorphologicalTag.CONDITIONAL, MorphologicalTag.COPULA,
            MorphologicalTag.POSITIVE, MorphologicalTag.PRONOUN, MorphologicalTag.LOCATIVE, MorphologicalTag.RELATIVE, MorphologicalTag.DEMONSTRATIVE,
            MorphologicalTag.INFINITIVE2, MorphologicalTag.INFINITIVE3, MorphologicalTag.BEGINNINGOFSENTENCE, MorphologicalTag.ENDOFSENTENCE, MorphologicalTag.BEGINNINGOFTITLE,
            MorphologicalTag.ENDOFTITLE, MorphologicalTag.BEGINNINGOFDOCUMENT, MorphologicalTag.ENDOFDOCUMENT, MorphologicalTag.INFINITIVE, MorphologicalTag.ASLONGAS,
            MorphologicalTag.DISTRIBUTIVE, MorphologicalTag.ADAMANTLY, MorphologicalTag.PERCENT, MorphologicalTag.WITHOUTBEINGABLETOHAVEDONESO, MorphologicalTag.DIMENSION,
            MorphologicalTag.PERSONALPRONOUN, MorphologicalTag.FRACTION, MorphologicalTag.HASHTAG, MorphologicalTag.EMAIL, MorphologicalTag.DATE,
            MorphologicalTag.CODE, MorphologicalTag.METRIC, MorphologicalTag.POLITE, MorphologicalTag.URGE};

    /**
     * The getMorphologicalTag method takes a String tag as an input and if the input matches with one of the elements of
     * tags array, it then gets the morphoTags of this tag and returns it.
     *
     * @param tag String to get morphoTags from.
     * @return morphoTags if found, null otherwise.
     */
    public static MorphologicalTag getMorphologicalTag(String tag) {
        for (int j = 0; j < tags.length; j++) {
            if (tag.equalsIgnoreCase(tags[j])) {
                return morphoTags[j];
            }
        }
        return null;
    }

    /**
     * The getTag method takes a MorphologicalTag type tag as an input and returns its corresponding tag from tags array.
     *
     * @param tag MorphologicalTag type input to find tag from.
     * @return tag if found, null otherwise.
     */
    public static String getTag(MorphologicalTag tag) {
        for (int j = 0; j < morphoTags.length; j++) {
            if (tag.equals(morphoTags[j])) {
                return tags[j];
            }
        }
        return null;
    }

    /**
     * A constructor of {@link InflectionalGroup} class which initializes the IG {@link ArrayList} by parsing given input
     * String IG by + and calling the getMorphologicalTag method with these substrings. If getMorphologicalTag method returns
     * a tag, it adds this tag to the IG {@link ArrayList}.
     *
     * @param IG String input.
     */
    public InflectionalGroup(String IG) {
        MorphologicalTag tag;
        String morphologicalTag;
        this.IG = new ArrayList<>();
        String st = IG;
        while (st.contains("+")) {
            morphologicalTag = st.substring(0, st.indexOf("+"));
            tag = getMorphologicalTag(morphologicalTag);
            if (tag != null) {
                this.IG.add(tag);
            } else {
                System.out.println("Morphological Tag " + morphologicalTag + " not found\n");
            }
            st = st.substring(st.indexOf("+") + 1);
        }
        morphologicalTag = st;
        tag = getMorphologicalTag(morphologicalTag);
        if (tag != null) {
            this.IG.add(tag);
        } else {
            System.out.println("Morphological Tag " + morphologicalTag + " not found\n");
        }
    }

    /**
     * Another getTag method which takes index as an input and returns the corresponding tag from IG {@link ArrayList}.
     *
     * @param index to get tag.
     * @return tag at input index.
     */
    public MorphologicalTag getTag(int index) {
        return IG.get(index);
    }

    /**
     * The size method returns the size of the IG {@link ArrayList}.
     *
     * @return the size of the IG {@link ArrayList}.
     */
    public int size() {
        return IG.size();
    }

    /**
     * Overridden toString method to return resulting tags in IG {@link ArrayList}.
     *
     * @return String result.
     */
    public String toString() {
        StringBuilder result = new StringBuilder(getTag(IG.get(0)));
        for (int i = 1; i < IG.size(); i++) {
            result.append("+").append(getTag(IG.get(i)));
        }
        return result.toString();
    }

    /**
     * The containsCase method loops through the tags in IG {@link ArrayList} and finds out the tags of the NOMINATIVE,
     * ACCUSATIVE, DATIVE, LOCATIVE or ABLATIVE cases.
     *
     * @return tag which holds the condition.
     */
    public MorphologicalTag containsCase() {
        for (MorphologicalTag tag : IG) {
            if (tag.equals(MorphologicalTag.NOMINATIVE) || tag.equals(MorphologicalTag.ACCUSATIVE) || tag.equals(MorphologicalTag.DATIVE) || tag.equals(MorphologicalTag.LOCATIVE) || tag.equals(MorphologicalTag.ABLATIVE)) {
                return tag;
            }
        }
        return null;
    }

    /**
     * The containsPlural method loops through the tags in IG {@link ArrayList} and checks whether the tags are from
     * the agreement plural or possessive plural, i.e A1PL, A2PL, A3PL, P1PL, P2PL and P3PL.
     *
     * @return true if the tag is plural, false otherwise.
     */
    public boolean containsPlural() {
        for (MorphologicalTag tag : IG) {
            if (tag.equals(MorphologicalTag.A1PL) || tag.equals(MorphologicalTag.A2PL) || tag.equals(MorphologicalTag.A3PL) || tag.equals(MorphologicalTag.P1PL) || tag.equals(MorphologicalTag.P2PL) || tag.equals(MorphologicalTag.P3PL)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The containsTag method takes a MorphologicalTag type tag as an input and loops through the tags in
     * IG {@link ArrayList} and returns true if the input matches with on of the tags in the IG.
     *
     * @param tag MorphologicalTag type input to search for.
     * @return true if tag matches with the tag in IG, false otherwise.
     */
    public boolean containsTag(MorphologicalTag tag) {
        for (MorphologicalTag currentTag : IG) {
            if (currentTag.equals(tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The containsPossessive method loops through the tags in IG {@link ArrayList} and returns true if the tag in IG is
     * one of the possessives: P1PL, P1SG, P2PL, P2SG, P3PL AND P3SG.
     *
     * @return true if it contains possessive tag, false otherwise.
     */
    public boolean containsPossessive() {
        for (MorphologicalTag tag : IG) {
            if (tag.equals(MorphologicalTag.P1PL) || tag.equals(MorphologicalTag.P1SG) || tag.equals(MorphologicalTag.P2PL) || tag.equals(MorphologicalTag.P2SG) || tag.equals(MorphologicalTag.P3PL) || tag.equals(MorphologicalTag.P3SG)) {
                return true;
            }
        }
        return false;
    }
}
