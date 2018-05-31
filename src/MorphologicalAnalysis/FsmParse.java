package MorphologicalAnalysis;

import Dictionary.TxtWord;
import Dictionary.Word;

import java.util.ArrayList;
import java.util.Comparator;

public class FsmParse extends MorphologicalParse implements Comparable{

    private ArrayList<State> suffixList;
    private ArrayList<String> formList;
    private ArrayList<String> transitionList;
    private ArrayList<String> withList;
    private String initialPos;
    private String pos;
    private String form;
    private String verbAgreement = null;
    private String possesiveAgreement = null;

    public FsmParse(Word root){
        this.root = root;
    }

    public FsmParse(int number, State startState){
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

    public FsmParse(double number, State startState){
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

    public FsmParse(String punctuation, State startState){
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

    public FsmParse(TxtWord root, State startState){
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

    public void constructInflectionalGroups(){
        String parse = transitionList();
        int i;
        ArrayList<String> iGs;
        iGs = new ArrayList<String>();
        while (parse.contains("^DB+")){
            iGs.add(parse.substring(0, parse.indexOf("^DB+")));
            parse = parse.substring(parse.indexOf("^DB+") + 4);
        }
        iGs.add(parse);
        inflectionalGroups = new ArrayList<InflectionalGroup>();
        inflectionalGroups.add(new InflectionalGroup(iGs.get(0).substring(iGs.get(0).indexOf('+') + 1)));
        for (i = 1; i < iGs.size(); i++){
            inflectionalGroups.add(new InflectionalGroup(iGs.get(i)));
        }
    }

    public String getVerbAgreement(){
        return verbAgreement;
    }

    public String getPossesiveAgreement(){
        return possesiveAgreement;
    }

    public void setAgreement(String transitionName){
        if (transitionName != null && (transitionName.equalsIgnoreCase("A1SG") || transitionName.equalsIgnoreCase("A2SG") || transitionName.equalsIgnoreCase("A3SG") || transitionName.equalsIgnoreCase("A1PL") || transitionName.equalsIgnoreCase("A2PL") || transitionName.equalsIgnoreCase("A3PL"))){
            this.verbAgreement = transitionName;
        }
        if (transitionName != null && (transitionName.equalsIgnoreCase("PNON") || transitionName.equalsIgnoreCase("P1SG") || transitionName.equalsIgnoreCase("P2SG") || transitionName.equalsIgnoreCase("P3SG") || transitionName.equalsIgnoreCase("P1PL") || transitionName.equalsIgnoreCase("P2PL") || transitionName.equalsIgnoreCase("P3PL"))){
            this.possesiveAgreement = transitionName;
        }
    }

    public String getLastLemmaWithTag(String pos){
        String lemma;
        if (initialPos != null && initialPos.equals(pos)){
            lemma = root.getName();
        } else {
            lemma = null;
        }
        for (int i = 1; i < formList.size(); i++){
            if (transitionList.get(i - 1) != null && transitionList.get(i - 1).contains("^DB+" + pos) && !transitionList.get(i - 1).contains("^DB+" + pos + "+ZERO")){
                lemma = formList.get(i);
            }
        }
        return lemma;
    }

    public String getLastLemma(){
        String lemma = root.getName();
        for (int i = 1; i < formList.size(); i++){
            if (transitionList.get(i - 1) != null && transitionList.get(i - 1).contains("^DB+")){
                lemma = formList.get(i);
            }
        }
        return lemma;
    }

    public void addSuffix(State suffix, String form, String transition, String with, String toPos){
        if (suffix.getPos() != null) {
            pos = suffix.getPos();
        } else {
            if (toPos != null){
                pos = toPos;
            }
        }
        suffixList.add(suffix);
        formList.add(form);
        transitionList.add(transition);
        if (!with.equalsIgnoreCase("0")){
            withList.add(with);
        }
        this.form = form;
    }

    public String getSurfaceForm(){
        return form;
    }
    
    public State getStartState(){
        return suffixList.get(0);
    }

    public String getFinalPos(){
        return pos;
    }

    public String getInitialPos(){
        return initialPos;
    }

    public void setForm(String name){
        form = name;
        formList.remove(0);
        formList.add(name);
    }

    public State getFinalSuffix(){
        return suffixList.get(suffixList.size() - 1);
    }

    public FsmParse clone(){
        int i;
        FsmParse p = new FsmParse(root);
        p.form = form;
        p.pos = pos;
        p.initialPos = initialPos;
        p.verbAgreement = verbAgreement;
        p.possesiveAgreement = possesiveAgreement;
        p.suffixList = new ArrayList<State>();
        for (i = 0; i < suffixList.size(); i++){
            p.suffixList.add(suffixList.get(i));
        }
        p.formList = new ArrayList<String>();
        for (i = 0; i < formList.size(); i++){
            p.formList.add(formList.get(i));
        }
        p.transitionList = new ArrayList<String>();
        for (i = 0; i < transitionList.size(); i++){
            p.transitionList.add(transitionList.get(i));
        }
        p.withList = new ArrayList<String>();
        for (i = 0; i < withList.size(); i++){
            p.withList.add(withList.get(i));
        }
        return p;
    }

    public String headerTransition(){
        if (formList.get(0).equalsIgnoreCase("<DOC>")){
            return "<DOC>+BDTAG";
        }
        if (formList.get(0).equalsIgnoreCase("</DOC>")){
            return "</DOC>+EDTAG";
        }
        if (formList.get(0).equalsIgnoreCase("<TITLE>")){
            return "<TITLE>+BTTAG";
        }
        if (formList.get(0).equalsIgnoreCase("</TITLE>")){
            return "</TITLE>+ETTAG";
        }
        if (formList.get(0).equalsIgnoreCase("<S>")){
            return "<S>+BSTAG";
        }
        if (formList.get(0).equalsIgnoreCase("</S>")){
            return "</S>+ESTAG";
        }
        return "";
    }

    public String pronounTransition(){
        if (formList.get(0).equalsIgnoreCase("kendi")){
            return "kendi+PRON+REFLEXP";
        }
        if (formList.get(0).equalsIgnoreCase("hep") || formList.get(0).equalsIgnoreCase("öbür") || formList.get(0).equalsIgnoreCase("kimse") || formList.get(0).equalsIgnoreCase("hiçbiri") || formList.get(0).equalsIgnoreCase("tümü") || formList.get(0).equalsIgnoreCase("çoğu") || formList.get(0).equalsIgnoreCase("hepsi") || formList.get(0).equalsIgnoreCase("birbiri") || formList.get(0).equalsIgnoreCase("birbirleri") || formList.get(0).equalsIgnoreCase("biri") || formList.get(0).equalsIgnoreCase("bazı")  || formList.get(0).equalsIgnoreCase("kimi")){
            return formList.get(0) + "+PRON+QUANTP";
        }
        if ((formList.get(0).equalsIgnoreCase("o") && suffixList.get(0).getName().equalsIgnoreCase("PronounRoot(DEMONS)")) || formList.get(0).equalsIgnoreCase("bu") || formList.get(0).equalsIgnoreCase("şu")){
            return formList.get(0) + "+PRON+DEMONSP";
        }
        if (formList.get(0).equalsIgnoreCase("ben")){
            return formList.get(0) + "+PRON+PERS+A1SG+PNON";
        }
        if (formList.get(0).equalsIgnoreCase("sen")){
            return formList.get(0) + "+PRON+PERS+A2SG+PNON";
        }
        if (formList.get(0).equalsIgnoreCase("o") && suffixList.get(0).getName().equalsIgnoreCase("PronounRoot(PERS)")){
            return formList.get(0) + "+PRON+PERS+A3SG+PNON";
        }
        if (formList.get(0).equalsIgnoreCase("biz")){
            return formList.get(0) + "+PRON+PERS+A1PL+PNON";
        }
        if (formList.get(0).equalsIgnoreCase("siz")){
            return formList.get(0) + "+PRON+PERS+A2PL+PNON";
        }
        if (formList.get(0).equalsIgnoreCase("onlar")){
            return "o+PRON+PERS+A3PL+PNON";
        }
        if (formList.get(0).equalsIgnoreCase("nere") || formList.get(0).equalsIgnoreCase("ne") || formList.get(0).equalsIgnoreCase("kim") || formList.get(0).equalsIgnoreCase("hangi")){
            return formList.get(0) + "+PRON+QUESP";
        }
        return "";
    }

    public String transitionList(){
        String result = "";
        if (suffixList.get(0).getName().equalsIgnoreCase("NominalRoot") || suffixList.get(0).getName().equalsIgnoreCase("NominalRootNoPossesive") || suffixList.get(0).getName().equalsIgnoreCase("CompoundNounRoot") || suffixList.get(0).getName().equalsIgnoreCase("NominalRootPlural")){
            result = formList.get(0) + "+NOUN";
        } else {
            if (suffixList.get(0).getName().startsWith("VerbalRoot") || suffixList.get(0).getName().equalsIgnoreCase("PassiveHn")){
                result = formList.get(0) + "+VERB";
            } else {
                if (suffixList.get(0).getName().equalsIgnoreCase("CardinalRoot")){
                    result = formList.get(0) + "+NUM+CARD";
                } else {
                    if (suffixList.get(0).getName().equalsIgnoreCase("FractionRoot")){
                        result = formList.get(0) + "+NUM+FRACTION";
                    } else {
                        if (suffixList.get(0).getName().equalsIgnoreCase("TimeRoot")){
                            result = formList.get(0) + "+TIME";
                        } else {
                            if (suffixList.get(0).getName().equalsIgnoreCase("RealRoot")){
                                result = formList.get(0) + "+NUM+REAL";
                            } else {
                                if (suffixList.get(0).getName().equalsIgnoreCase("Punctuation")){
                                    result = formList.get(0) + "+PUNC";
                                } else {
                                    if (suffixList.get(0).getName().equalsIgnoreCase("Hashtag")){
                                        result = formList.get(0) + "+HASHTAG";
                                    } else {
                                        if (suffixList.get(0).getName().equalsIgnoreCase("DateRoot")){
                                            result = formList.get(0) + "+DATE";
                                        } else {
                                            if (suffixList.get(0).getName().equalsIgnoreCase("RangeRoot")){
                                                result = formList.get(0) + "+RANGE";
                                            } else {
                                                if (suffixList.get(0).getName().equalsIgnoreCase("Email")){
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

    public String suffixList(){
        String result = suffixList.get(0).getName() + '(' + formList.get(0) + ')';
        for (int i = 1; i < formList.size(); i++){
            if (!formList.get(i).equalsIgnoreCase(formList.get(i - 1))){
                result = result + "+" + suffixList.get(i).getName() + '(' + formList.get(i) + ')';
            }
        }
        return result;
    }

    public String withList(){
        String result = root.getName();
        for (String aWith : withList) {
            result = result + "+" + aWith;
        }
        return result;
    }

    public String toString(){
        return transitionList();
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof FsmParse){
            return transitionList().compareTo(((FsmParse) o).transitionList());
        } else {
            return 0;
        }
    }
}
