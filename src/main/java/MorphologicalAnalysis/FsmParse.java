package MorphologicalAnalysis;

import Dictionary.TxtWord;
import Dictionary.Word;

import java.util.ArrayList;
import java.util.Comparator;

public class FsmParse extends MorphologicalParse implements Comparable {

    private ArrayList<State> suffixList;
    private ArrayList<String> formList;
    private ArrayList<String> transitionList;
    private ArrayList<String> withList;
    private String initialPos;
    private String pos;
    private String form;
    private String verbAgreement = null;
    private String possesiveAgreement = null;

    /**
     * A constructor of {@link FsmParse} class which takes a {@link Word} as an input and assigns it to root variable.
     *
     * @param root {@link Word} input.
     */
    public FsmParse(Word root) {
        this.root = root;
    }

    /**
     * Another constructor of {@link FsmParse} class which takes an {@link Integer} number and a {@link State} as inputs.
     * First, it creates a {@link TxtWord} with given number and adds flag to this number as IS_SAYI and initializes root variable with
     * number {@link TxtWord}. It also initializes form with root's name, pos and initialPos with given {@link State}'s POS, creates 4 new
     * {@link ArrayList} suffixList, formList, transitionList and withList and adds given {@link State} to suffixList, form to
     * formList.
     *
     * @param number     {@link Integer} input.
     * @param startState {@link State} input.
     */
    public FsmParse(int number, State startState) {
        TxtWord num = new TxtWord("" + number);
        num.addFlag("IS_SAYI");
        this.root = num;
        this.form = root.getName();
        this.pos = startState.getPos();
        this.initialPos = startState.getPos();
        suffixList = new ArrayList<State>();
        suffixList.add(startState);
        formList = new ArrayList<String>();
        formList.add(this.form);
        transitionList = new ArrayList<String>();
        withList = new ArrayList<String>();
    }

    /**
     * Another constructor of {@link FsmParse} class which takes a {@link Double} number and a {@link State} as inputs.
     * First, it creates a {@link TxtWord} with given number and adds flag to this number as IS_SAYI and initializes root variable with
     * number {@link TxtWord}. It also initializes form with root's name, pos and initialPos with given {@link State}'s POS, creates 4 new
     * {@link ArrayList} suffixList, formList, transitionList and withList and adds given {@link State} to suffixList, form to
     * formList.
     *
     * @param number     {@link Double} input.
     * @param startState {@link State} input.
     */
    public FsmParse(double number, State startState) {
        TxtWord num = new TxtWord("" + number);
        num.addFlag("IS_SAYI");
        this.root = num;
        this.form = root.getName();
        this.pos = startState.getPos();
        this.initialPos = startState.getPos();
        suffixList = new ArrayList<State>();
        suffixList.add(startState);
        formList = new ArrayList<String>();
        formList.add(this.form);
        transitionList = new ArrayList<String>();
        withList = new ArrayList<String>();
    }

    /**
     * Another constructor of {@link FsmParse} class which takes a {@link String} punctuation and a {@link State} as inputs.
     * First, it creates a {@link TxtWord} with given punctuation and initializes root variable with this {@link TxtWord}.
     * It also initializes form with root's name, pos and initialPos with given {@link State}'s POS, creates 4 new
     * {@link ArrayList} suffixList, formList, transitionList and withList and adds given {@link State} to suffixList, form to
     * formList.
     *
     * @param punctuation {@link String} input.
     * @param startState  {@link State} input.
     */
    public FsmParse(String punctuation, State startState) {
        this.root = new TxtWord(punctuation);
        this.form = root.getName();
        this.pos = startState.getPos();
        this.initialPos = startState.getPos();
        suffixList = new ArrayList<State>();
        suffixList.add(startState);
        formList = new ArrayList<String>();
        formList.add(this.form);
        transitionList = new ArrayList<String>();
        withList = new ArrayList<String>();
    }

    /**
     * Another constructor of {@link FsmParse} class which takes a {@link TxtWord} root and a {@link State} as inputs.
     * First, initializes root variable with this {@link TxtWord}. It also initializes form with root's name, pos and
     * initialPos with given {@link State}'s POS, creates 4 new {@link ArrayList} suffixList, formList, transitionList
     * and withList and adds given {@link State} to suffixList, form to formList.
     *
     * @param root       {@link TxtWord} input.
     * @param startState {@link State} input.
     */
    public FsmParse(TxtWord root, State startState) {
        this.root = root;
        this.form = root.getName();
        this.pos = startState.getPos();
        this.initialPos = startState.getPos();
        suffixList = new ArrayList<State>();
        suffixList.add(startState);
        formList = new ArrayList<String>();
        formList.add(this.form);
        transitionList = new ArrayList<String>();
        withList = new ArrayList<String>();
    }

    /**
     * The constructInflectionalGroups method initially calls the transitionList method and assigns the resulting {@link String}
     * to the parse variable and creates a new {@link ArrayList} as iGs. If parse {@link String} contains a derivational boundary
     * it adds the substring starting from the 0 to the index of derivational boundary to the iGs. If it does not contain a DB,
     * it directly adds parse to the iGs. Then, creates and initializes new {@link ArrayList} as inflectionalGroups and fills with
     * the items of iGs.
     */
    public void constructInflectionalGroups() {
        String parse = transitionList();
        int i;
        ArrayList<String> iGs;
        iGs = new ArrayList<String>();
        while (parse.contains("^DB+")) {
            iGs.add(parse.substring(0, parse.indexOf("^DB+")));
            parse = parse.substring(parse.indexOf("^DB+") + 4);
        }
        iGs.add(parse);
        inflectionalGroups = new ArrayList<InflectionalGroup>();
        inflectionalGroups.add(new InflectionalGroup(iGs.get(0).substring(iGs.get(0).indexOf('+') + 1)));
        for (i = 1; i < iGs.size(); i++) {
            inflectionalGroups.add(new InflectionalGroup(iGs.get(i)));
        }
    }

    /**
     * Getter for the verbAgreement variable.
     *
     * @return the verbAgreement variable.
     */
    public String getVerbAgreement() {
        return verbAgreement;
    }

    /**
     * Getter for the getPossesiveAgreement variable.
     *
     * @return the possesiveAgreement variable.
     */
    public String getPossesiveAgreement() {
        return possesiveAgreement;
    }

    /**
     * The setAgreement method takes a {@link String} transitionName as an input and if it is one of the A1SG, A2SG, A3SG,
     * A1PL, A2PL or A3PL it assigns transitionName input to the verbAgreement variable. Or if it is ine of the PNON, P1SG, P2SG,P3SG,
     * P1PL, P2PL or P3PL it assigns transitionName input to the possesiveAgreement variable.
     *
     * @param transitionName {@link String} input.
     */
    public void setAgreement(String transitionName) {
        if (transitionName != null && (transitionName.equalsIgnoreCase("A1SG") || transitionName.equalsIgnoreCase("A2SG") || transitionName.equalsIgnoreCase("A3SG") || transitionName.equalsIgnoreCase("A1PL") || transitionName.equalsIgnoreCase("A2PL") || transitionName.equalsIgnoreCase("A3PL"))) {
            this.verbAgreement = transitionName;
        }
        if (transitionName != null && (transitionName.equalsIgnoreCase("PNON") || transitionName.equalsIgnoreCase("P1SG") || transitionName.equalsIgnoreCase("P2SG") || transitionName.equalsIgnoreCase("P3SG") || transitionName.equalsIgnoreCase("P1PL") || transitionName.equalsIgnoreCase("P2PL") || transitionName.equalsIgnoreCase("P3PL"))) {
            this.possesiveAgreement = transitionName;
        }
    }

    /**
     * The getLastLemmaWithTag method takes a String input pos as an input. If given pos is an initial pos then it assigns
     * root to the lemma, and assign null otherwise.  Then, it loops i times where i ranges from 1 to size of the formList,
     * if the item at i-1 of transitionList is not null and contains a derivational boundary with pos but not with ZERO,
     * it assigns the ith item of formList to lemma.
     *
     * @param pos {@link String} input.
     * @return String output lemma.
     */
    public String getLastLemmaWithTag(String pos) {
        String lemma;
        if (initialPos != null && initialPos.equals(pos)) {
            lemma = root.getName();
        } else {
            lemma = null;
        }
        for (int i = 1; i < formList.size(); i++) {
            if (transitionList.get(i - 1) != null && transitionList.get(i - 1).contains("^DB+" + pos) && !transitionList.get(i - 1).contains("^DB+" + pos + "+ZERO")) {
                lemma = formList.get(i);
            }
        }
        return lemma;
    }

    /**
     * The getLastLemma method initially assigns root as lemma. Then, it loops i times where i ranges from 1 to size of the formList,
     * if the item at i-1 of transitionList is not null and contains a derivational boundary, it assigns the ith item of formList to lemma.
     *
     * @return String output lemma.
     */
    public String getLastLemma() {
        String lemma = root.getName();
        for (int i = 1; i < formList.size(); i++) {
            if (transitionList.get(i - 1) != null && transitionList.get(i - 1).contains("^DB+")) {
                lemma = formList.get(i);
            }
        }
        return lemma;
    }

    /**
     * The addSuffix method takes 5 different inputs; {@link State} suffix, {@link String} form, transition, with and toPos.
     * If the pos of given input suffix is not null, it then assigns it to the pos variable. If the pos of the given suffix
     * is null but given toPos is not null than it assigns toPos to pos variable. At the end, it adds suffix to the suffixList,
     * form to the formList, transition to the transitionList and if given with is not 0, it is also added to withList.
     *
     * @param suffix     {@link State} input.
     * @param form       {@link String} input.
     * @param transition {@link String} input.
     * @param with       {@link String} input.
     * @param toPos      {@link String} input.
     */
    public void addSuffix(State suffix, String form, String transition, String with, String toPos) {
        if (suffix.getPos() != null) {
            pos = suffix.getPos();
        } else {
            if (toPos != null) {
                pos = toPos;
            }
        }
        suffixList.add(suffix);
        formList.add(form);
        transitionList.add(transition);
        if (!with.equalsIgnoreCase("0")) {
            withList.add(with);
        }
        this.form = form;
    }

    /**
     * Getter for the form variable.
     *
     * @return the form variable.
     */
    public String getSurfaceForm() {
        return form;
    }

    /**
     * The getStartState method returns the first item of suffixList {@link ArrayList}.
     *
     * @return the first item of suffixList {@link ArrayList}.
     */
    public State getStartState() {
        return suffixList.get(0);
    }

    /**
     * Getter for the pos variable.
     *
     * @return the pos variable.
     */
    public String getFinalPos() {
        return pos;
    }

    /**
     * Getter for the initialPos variable.
     *
     * @return the initialPos variable.
     */
    public String getInitialPos() {
        return initialPos;
    }

    /**
     * The setForm method takes a {@link String} name as an input and assigns it to the form variable, then it removes the first item
     * of formList {@link ArrayList} and adds the given name to the formList.
     *
     * @param name String input to set form.
     */
    public void setForm(String name) {
        form = name;
        formList.remove(0);
        formList.add(name);
    }

    /**
     * The getFinalSuffix method returns the last item of suffixList {@link ArrayList}.
     *
     * @return the last item of suffixList {@link ArrayList}.
     */
    public State getFinalSuffix() {
        return suffixList.get(suffixList.size() - 1);
    }

    /**
     * The overridden clone method creates a new {@link FsmParse} abject with root variable and initializes variables form, pos,
     * initialPos, verbAgreement, possesiveAgreement, and also the {@link ArrayList}s suffixList, formList, transitionList and withList.
     * Then returns newly created and cloned {@link FsmParse} object.
     *
     * @return FsmParse object.
     */
    public FsmParse clone() {
        int i;
        FsmParse p = new FsmParse(root);
        p.form = form;
        p.pos = pos;
        p.initialPos = initialPos;
        p.verbAgreement = verbAgreement;
        p.possesiveAgreement = possesiveAgreement;
        p.suffixList = new ArrayList<State>();
        for (i = 0; i < suffixList.size(); i++) {
            p.suffixList.add(suffixList.get(i));
        }
        p.formList = new ArrayList<String>();
        for (i = 0; i < formList.size(); i++) {
            p.formList.add(formList.get(i));
        }
        p.transitionList = new ArrayList<String>();
        for (i = 0; i < transitionList.size(); i++) {
            p.transitionList.add(transitionList.get(i));
        }
        p.withList = new ArrayList<String>();
        for (i = 0; i < withList.size(); i++) {
            p.withList.add(withList.get(i));
        }
        return p;
    }

    /**
     * The headerTransition method gets the first item of formList and checks for cases;
     * <p>
     * If it is &lt;DOC&gt;, it returns &lt;DOC&gt;+BDTAG which indicates the beginning of a document.
     * If it is &lt;/DOC&gt;, it returns &lt;/DOC&gt;+EDTAG which indicates the ending of a document.
     * If it is &lt;TITLE&gt;, it returns &lt;TITLE&gt;+BTTAG which indicates the beginning of a title.
     * If it is &lt;/TITLE&gt;, it returns &lt;/TITLE&gt;+ETTAG which indicates the ending of a title.
     * If it is &lt;S&gt;, it returns &lt;S&gt;+BSTAG which indicates the beginning of a sentence.
     * If it is &lt;/S&gt;, it returns &lt;/S&gt;+ESTAG which indicates the ending of a sentence.
     *
     * @return corresponding tags of the headers and an empty {@link String} if any case does not match.
     */
    public String headerTransition() {
        if (formList.get(0).equalsIgnoreCase("<DOC>")) {
            return "<DOC>+BDTAG";
        }
        if (formList.get(0).equalsIgnoreCase("</DOC>")) {
            return "</DOC>+EDTAG";
        }
        if (formList.get(0).equalsIgnoreCase("<TITLE>")) {
            return "<TITLE>+BTTAG";
        }
        if (formList.get(0).equalsIgnoreCase("</TITLE>")) {
            return "</TITLE>+ETTAG";
        }
        if (formList.get(0).equalsIgnoreCase("<S>")) {
            return "<S>+BSTAG";
        }
        if (formList.get(0).equalsIgnoreCase("</S>")) {
            return "</S>+ESTAG";
        }
        return "";
    }

    /**
     * The pronounTransition method gets the first item of formList and checks for cases;
     * <p>
     * If it is "kendi", it returns kendi+PRON+REFLEXP which indicates a reflexive pronoun.
     * If it is one of the "hep, öbür, kimse, hiçbiri, tümü, çoğu, hepsi, biri, birbirleri, birbiri, bazı, kimi", it returns
     * +PRON+QUANTP which indicates a quantitative pronoun.
     * If it is one of the "o, bu, şu" and if it is "o" it also checks the first item of suffixList and if it is a PronounRoot(DEMONS),
     * it returns +PRON+DEMONSP which indicates a demonstrative pronoun.
     * If it is "ben", it returns +PRON+PERS+A1SG+PNON which indicates a 1st person singular agreement.
     * If it is "sen", it returns +PRON+PERS+A2SG+PNON which indicates a 2nd person singular agreement.
     * If it is "o" and the first item of suffixList, if it is a PronounRoot(PERS), it returns +PRON+PERS+A3SG+PNON which
     * indicates a 3rd person singular agreement.
     * If it is "biz", it returns +PRON+PERS+A1PL+PNON which indicates a 1st person plural agreement.
     * If it is "siz", it returns +PRON+PERS+A2PL+PNON which indicates a 2nd person plural agreement.
     * If it is "onlar" and the first item of suffixList, if it is a PronounRoot(PERS), it returns o+PRON+PERS+A3PL+PNON which
     * indicates a 3rd person plural agreement.
     * If it is one of the "nere, ne, kim, hangi", it returns +PRON+QUESP which indicates a question pronoun.
     *
     * @return corresponding transitions of pronouns and an empty {@link String} if any case does not match.
     */
    public String pronounTransition() {
        if (formList.get(0).equalsIgnoreCase("kendi")) {
            return "kendi+PRON+REFLEXP";
        }
        if (formList.get(0).equalsIgnoreCase("hep") || formList.get(0).equalsIgnoreCase("öbür") || formList.get(0).equalsIgnoreCase("kimse") || formList.get(0).equalsIgnoreCase("hiçbiri") || formList.get(0).equalsIgnoreCase("tümü") || formList.get(0).equalsIgnoreCase("çoğu") || formList.get(0).equalsIgnoreCase("hepsi") || formList.get(0).equalsIgnoreCase("birbiri") || formList.get(0).equalsIgnoreCase("birbirleri") || formList.get(0).equalsIgnoreCase("biri") || formList.get(0).equalsIgnoreCase("bazı") || formList.get(0).equalsIgnoreCase("kimi")) {
            return formList.get(0) + "+PRON+QUANTP";
        }
        if ((formList.get(0).equalsIgnoreCase("o") && suffixList.get(0).getName().equalsIgnoreCase("PronounRoot(DEMONS)")) || formList.get(0).equalsIgnoreCase("bu") || formList.get(0).equalsIgnoreCase("şu")) {
            return formList.get(0) + "+PRON+DEMONSP";
        }
        if (formList.get(0).equalsIgnoreCase("ben")) {
            return formList.get(0) + "+PRON+PERS+A1SG+PNON";
        }
        if (formList.get(0).equalsIgnoreCase("sen")) {
            return formList.get(0) + "+PRON+PERS+A2SG+PNON";
        }
        if (formList.get(0).equalsIgnoreCase("o") && suffixList.get(0).getName().equalsIgnoreCase("PronounRoot(PERS)")) {
            return formList.get(0) + "+PRON+PERS+A3SG+PNON";
        }
        if (formList.get(0).equalsIgnoreCase("biz")) {
            return formList.get(0) + "+PRON+PERS+A1PL+PNON";
        }
        if (formList.get(0).equalsIgnoreCase("siz")) {
            return formList.get(0) + "+PRON+PERS+A2PL+PNON";
        }
        if (formList.get(0).equalsIgnoreCase("onlar")) {
            return "o+PRON+PERS+A3PL+PNON";
        }
        if (formList.get(0).equalsIgnoreCase("nere") || formList.get(0).equalsIgnoreCase("ne") || formList.get(0).equalsIgnoreCase("kim") || formList.get(0).equalsIgnoreCase("hangi")) {
            return formList.get(0) + "+PRON+QUESP";
        }
        return "";
    }

    /**
     * The transitionList method first creates an empty {@link String} result, then gets the first item of suffixList and checks for cases;
     * <p>
     * If it is one of the "NominalRoot, NominalRootNoPossesive, CompoundNounRoot, NominalRootPlural", it assigns concatenation of first
     * item of formList and +NOUN to the result String.
     * Ex : Birincilik
     * <p>
     * If it is one of the "VerbalRoot, PassiveHn", it assigns concatenation of first item of formList and +VERB to the result String.
     * Ex : Başkalaştı
     * <p>
     * If it is "CardinalRoot", it assigns concatenation of first item of formList and +NUM+CARD to the result String.
     * Ex : Onuncu
     * <p>
     * If it is "FractionRoot", it assigns concatenation of first item of formList and NUM+FRACTION to the result String.
     * Ex : 1/2
     * <p>
     * If it is "TimeRoot", it assigns concatenation of first item of formList and +TIME to the result String.
     * Ex : 14:28
     * <p>
     * If it is "RealRoot", it assigns concatenation of first item of formList and +NUM+REAL to the result String.
     * Ex : 1.2
     * <p>
     * If it is "Punctuation", it assigns concatenation of first item of formList and +PUNC to the result String.
     * Ex : ,
     * <p>
     * If it is "Hashtag", it assigns concatenation of first item of formList and +HASHTAG to the result String.
     * Ex : #
     * <p>
     * If it is "DateRoot", it assigns concatenation of first item of formList and +DATE to the result String.
     * Ex : 11/06/2018
     * <p>
     * If it is "RangeRoot", it assigns concatenation of first item of formList and +RANGE to the result String.
     * Ex : 3-5
     * <p>
     * If it is "Email", it assigns concatenation of first item of formList and +EMAIL to the result String.
     * Ex : abc@
     * <p>
     * If it is "PercentRoot", it assigns concatenation of first item of formList and +PERCENT to the result String.
     * Ex : %12.5
     * <p>
     * If it is "DeterminerRoot", it assigns concatenation of first item of formList and +DET to the result String.
     * Ex : Birtakım
     * <p>
     * If it is "ConjunctionRoot", it assigns concatenation of first item of formList and +CONJ to the result String.
     * Ex : Ama
     * <p>
     * If it is "AdverbRoot", it assigns concatenation of first item of formList and +ADV to the result String.
     * Ex : Acilen
     * <p>
     * If it is "ProperRoot", it assigns concatenation of first item of formList and +NOUN+PROP to the result String.
     * Ex : Ahmet
     * <p>
     * If it is "HeaderRoot", it assigns the result of the headerTransition method to the result String.
     * Ex : &lt;DOC&gt;
     * <p>
     * If it is "InterjectionRoot", it assigns concatenation of first item of formList and +INTERJ to the result String.
     * Ex : Hey
     * <p>
     * If it is "DuplicateRoot", it assigns concatenation of first item of formList and +DUP to the result String.
     * Ex : Allak
     * <p>
     * If it is "QuestionRoot", it assigns concatenation of first item of formList and +QUES to the result String.
     * Ex : Mı
     * <p>
     * If it is "PostP", and the first item of formList is one of the "karşı, ilişkin, göre, kadar, ait, yönelik, rağmen, değin,
     * dek, doğru, karşın, dair, atfen, binaen, hitaben, istinaden, mahsuben, mukabil, nazaran", it assigns concatenation of first
     * item of formList and +POSTP+PCDAT to the result String.
     * Ex : İlişkin
     * <p>
     * If it is "PostP", and the first item of formList is one of the "sonra, önce, beri, fazla, dolayı, itibaren, başka,
     * çok, evvel, ötürü, yana, öte, aşağı, yukarı, dışarı, az, gayrı", it assigns concatenation of first
     * item of formList and +POSTP+PCABL to the result String.
     * Ex : Başka
     * <p>
     * If it is "PostP", and the first item of formList is "yanısıra", it assigns concatenation of first
     * item of formList and +POSTP+PCGEN to the result String.
     * Ex : Yanısıra
     * <p>
     * If it is "PostP", and the first item of formList is one of the "birlikte, beraber", it assigns concatenation of first
     * item of formList and +PPOSTP+PCINS to the result String.
     * Ex : Birlikte
     * <p>
     * If it is "PostP", and the first item of formList is one of the "aşkın, takiben", it assigns concatenation of first
     * item of formList and +POSTP+PCACC to the result String.
     * Ex : Takiben
     * <p>
     * If it is "PostP", it assigns concatenation of first item of formList and +POSTP+PCNOM to the result String.
     * <p>
     * If it is "PronounRoot", it assigns result of the pronounTransition method to the result String.
     * Ex : Ben
     * <p>
     * If it is "OrdinalRoot", it assigns concatenation of first item of formList and +NUM+ORD to the result String.
     * Ex : Altıncı
     * <p>
     * If it starts with "Adjective", it assigns concatenation of first item of formList and +ADJ to the result String.
     * Ex : Güzel
     * <p>
     * At the end, it loops through the formList and concatenates each item with result {@link String}.
     *
     * @return String result accumulated with items of formList.
     */
    public String transitionList() {
        String result = "";
        if (suffixList.get(0).getName().equalsIgnoreCase("NominalRoot") || suffixList.get(0).getName().equalsIgnoreCase("NominalRootNoPossesive") || suffixList.get(0).getName().equalsIgnoreCase("CompoundNounRoot") || suffixList.get(0).getName().equalsIgnoreCase("NominalRootPlural")) {
            result = formList.get(0) + "+NOUN";
        } else {
            if (suffixList.get(0).getName().startsWith("VerbalRoot") || suffixList.get(0).getName().equalsIgnoreCase("PassiveHn")) {
                result = formList.get(0) + "+VERB";
            } else {
                if (suffixList.get(0).getName().equalsIgnoreCase("CardinalRoot")) {
                    result = formList.get(0) + "+NUM+CARD";
                } else {
                    if (suffixList.get(0).getName().equalsIgnoreCase("FractionRoot")) {
                        result = formList.get(0) + "+NUM+FRACTION";
                    } else {
                        if (suffixList.get(0).getName().equalsIgnoreCase("TimeRoot")) {
                            result = formList.get(0) + "+TIME";
                        } else {
                            if (suffixList.get(0).getName().equalsIgnoreCase("RealRoot")) {
                                result = formList.get(0) + "+NUM+REAL";
                            } else {
                                if (suffixList.get(0).getName().equalsIgnoreCase("Punctuation")) {
                                    result = formList.get(0) + "+PUNC";
                                } else {
                                    if (suffixList.get(0).getName().equalsIgnoreCase("Hashtag")) {
                                        result = formList.get(0) + "+HASHTAG";
                                    } else {
                                        if (suffixList.get(0).getName().equalsIgnoreCase("DateRoot")) {
                                            result = formList.get(0) + "+DATE";
                                        } else {
                                            if (suffixList.get(0).getName().equalsIgnoreCase("RangeRoot")) {
                                                result = formList.get(0) + "+RANGE";
                                            } else {
                                                if (suffixList.get(0).getName().equalsIgnoreCase("Email")) {
                                                    result = formList.get(0) + "+EMAIL";
                                                } else {
                                                    if (suffixList.get(0).getName().equalsIgnoreCase("PercentRoot")) {
                                                        result = formList.get(0) + "+PERCENT";
                                                    } else {
                                                        if (suffixList.get(0).getName().equalsIgnoreCase("DeterminerRoot")) {
                                                            result = formList.get(0) + "+DET";
                                                        } else {
                                                            if (suffixList.get(0).getName().equalsIgnoreCase("ConjunctionRoot")) {
                                                                result = formList.get(0) + "+CONJ";
                                                            } else {
                                                                if (suffixList.get(0).getName().equalsIgnoreCase("AdverbRoot")) {
                                                                    result = formList.get(0) + "+ADV";
                                                                } else {
                                                                    if (suffixList.get(0).getName().equalsIgnoreCase("ProperRoot")) {
                                                                        result = formList.get(0) + "+NOUN+PROP";
                                                                    } else {
                                                                        if (suffixList.get(0).getName().equalsIgnoreCase("HeaderRoot")) {
                                                                            result = headerTransition();
                                                                        } else {
                                                                            if (suffixList.get(0).getName().equalsIgnoreCase("InterjectionRoot")) {
                                                                                result = formList.get(0) + "+INTERJ";
                                                                            } else {
                                                                                if (suffixList.get(0).getName().equalsIgnoreCase("DuplicateRoot")) {
                                                                                    result = formList.get(0) + "+DUP";
                                                                                } else {
                                                                                    if (suffixList.get(0).getName().equalsIgnoreCase("QuestionRoot")) {
                                                                                        result = formList.get(0) + "+QUES";
                                                                                    } else {
                                                                                        if (suffixList.get(0).getName().equalsIgnoreCase("PostP")) {
                                                                                            if (formList.get(0).equalsIgnoreCase("karşı") || formList.get(0).equalsIgnoreCase("ilişkin") || formList.get(0).equalsIgnoreCase("göre") || formList.get(0).equalsIgnoreCase("kadar") || formList.get(0).equalsIgnoreCase("ait") || formList.get(0).equalsIgnoreCase("yönelik") || formList.get(0).equalsIgnoreCase("rağmen") || formList.get(0).equalsIgnoreCase("değin") || formList.get(0).equalsIgnoreCase("dek") || formList.get(0).equalsIgnoreCase("doğru") || formList.get(0).equalsIgnoreCase("karşın") || formList.get(0).equalsIgnoreCase("dair") || formList.get(0).equalsIgnoreCase("atfen") || formList.get(0).equalsIgnoreCase("binaen") || formList.get(0).equalsIgnoreCase("hitaben") || formList.get(0).equalsIgnoreCase("istinaden") || formList.get(0).equalsIgnoreCase("mahsuben") || formList.get(0).equalsIgnoreCase("mukabil") || formList.get(0).equalsIgnoreCase("nazaran")) {
                                                                                                result = formList.get(0) + "+POSTP+PCDAT";
                                                                                            } else {
                                                                                                if (formList.get(0).equalsIgnoreCase("sonra") || formList.get(0).equalsIgnoreCase("önce") || formList.get(0).equalsIgnoreCase("beri") || formList.get(0).equalsIgnoreCase("fazla") || formList.get(0).equalsIgnoreCase("dolayı") || formList.get(0).equalsIgnoreCase("itibaren") || formList.get(0).equalsIgnoreCase("başka") || formList.get(0).equalsIgnoreCase("çok") || formList.get(0).equalsIgnoreCase("evvel") || formList.get(0).equalsIgnoreCase("ötürü") || formList.get(0).equalsIgnoreCase("yana") || formList.get(0).equalsIgnoreCase("öte") || formList.get(0).equalsIgnoreCase("aşağı") || formList.get(0).equalsIgnoreCase("yukarı") || formList.get(0).equalsIgnoreCase("dışarı") || formList.get(0).equalsIgnoreCase("az") || formList.get(0).equalsIgnoreCase("gayrı")) {
                                                                                                    result = formList.get(0) + "+POSTP+PCABL";
                                                                                                } else {
                                                                                                    if (formList.get(0).equalsIgnoreCase("yanısıra")) {
                                                                                                        result = formList.get(0) + "+POSTP+PCGEN";
                                                                                                    } else {
                                                                                                        if (formList.get(0).equalsIgnoreCase("birlikte") || formList.get(0).equalsIgnoreCase("beraber")) {
                                                                                                            result = formList.get(0) + "+POSTP+PCINS";
                                                                                                        } else {
                                                                                                            if (formList.get(0).equalsIgnoreCase("aşkın") || formList.get(0).equalsIgnoreCase("takiben")) {
                                                                                                                result = formList.get(0) + "+POSTP+PCACC";
                                                                                                            } else {
                                                                                                                result = formList.get(0) + "+POSTP+PCNOM";
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        } else {
                                                                                            if (suffixList.get(0).getName().startsWith("PronounRoot")) {
                                                                                                result = pronounTransition();
                                                                                            } else {
                                                                                                if (suffixList.get(0).getName().equalsIgnoreCase("OrdinalRoot")) {
                                                                                                    result = formList.get(0) + "+NUM+ORD";
                                                                                                } else {
                                                                                                    if (suffixList.get(0).getName().startsWith("Adjective")) {
                                                                                                        result = formList.get(0) + "+ADJ";
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
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (String transition : transitionList) {
            if (transition != null) {
                if (!transition.startsWith("^")) {
                    result = result + "+" + transition;
                } else {
                    result = result + transition;
                }
            }
        }
        return result;
    }

    /**
     * The suffixList method gets the first items of suffixList and formList and concatenates them with parenthesis and
     * assigns a String result. Then, loops through the formList and it the current ith item is not equal to previous
     * item it accumulates ith items of formList and suffixList to the result {@link String}.
     *
     * @return result {@link String} accumulated with the items of formList and suffixList.
     */
    public String suffixList() {
        String result = suffixList.get(0).getName() + '(' + formList.get(0) + ')';
        for (int i = 1; i < formList.size(); i++) {
            if (!formList.get(i).equalsIgnoreCase(formList.get(i - 1))) {
                result = result + "+" + suffixList.get(i).getName() + '(' + formList.get(i) + ')';
            }
        }
        return result;
    }

    /**
     * The withList method gets the root as a result {@link String} then loops through the withList and concatenates each item
     * with result {@link String}.
     *
     * @return result {@link String} accumulated with items of withList.
     */
    public String withList() {
        String result = root.getName();
        for (String aWith : withList) {
            result = result + "+" + aWith;
        }
        return result;
    }

    /**
     * The overridden toString method which returns transitionList method.
     *
     * @return returns transitionList method.
     */
    public String toString() {
        return transitionList();
    }

    /**
     * The overridden compareTo metgod takes an {@link Object} as an input and if it is an instance of the {@link FsmParse}
     * class it returns the result of comparison of the items of transitionList with input {@link Object}.
     *
     * @param o {@link Object} input to compare.
     * @return comparison of the items of transitionList with input {@link Object}, and returns 0 if input is not an
     * instance of {@link FsmParse} class.
     */
    @Override
    public int compareTo(Object o) {
        if (o instanceof FsmParse) {
            return transitionList().compareTo(((FsmParse) o).transitionList());
        } else {
            return 0;
        }
    }
}
