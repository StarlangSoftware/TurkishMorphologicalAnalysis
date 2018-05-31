package MorphologicalAnalysis;

import Dictionary.TxtWord;
import Language.TurkishLanguage;

public class Transition {
    private State toState;
    private String with;
    private String withName;
    private String formationToCheck;
    private String toPos;

    public Transition(State toState, String with, String withName){
        this.toState = toState;
        this.with = with;
        this.withName = withName;
        toPos = null;
    }

    public Transition(State toState, String with, String withName, String toPos){
        this.toState = toState;
        this.with = with;
        this.withName = withName;
        this.toPos = toPos;
    }

    public Transition(String with){
        toState = null;
        withName = null;
        toPos = null;
        this.with = with;
    }

    public State toState(){
        return toState;
    }

    public String toPos(){
        return toPos;
    }

    public boolean transitionPossible(String currentSurfaceForm, String realSurfaceForm){
        if (currentSurfaceForm.length() == 0 || currentSurfaceForm.length() >= realSurfaceForm.length()){
            return true;
        }
        String searchString = realSurfaceForm.substring(currentSurfaceForm.length());
        for (int i = 0; i < with.length(); i++){
            switch (with.charAt(i)){
                case 'C':
                    return searchString.contains("c") || searchString.contains("ç");
                case 'D':
                    return searchString.contains("d") || searchString.contains("t");
                case 'c':
                case 'e':
                case 'r':
                case 'p':
                case 'l':
                case 'b':
                case 'g':
                case 'o':
                case 'm':
                case 'v':
                case 'i':
                case 'ü':
                case 'z':
                    return searchString.contains("" + with.charAt(i));
                case 'A':
                    return searchString.contains("a") || searchString.contains("e");
                case 'k':
                    return searchString.contains("k") || searchString.contains("g") || searchString.contains("ğ");
            }
        }
        return true;
    }

    public boolean transitionPossible(FsmParse currentFsmParse){
        if (with.equalsIgnoreCase("Ar") && currentFsmParse.getSurfaceForm().endsWith("l") && !currentFsmParse.getWord().getName().equalsIgnoreCase(currentFsmParse.getSurfaceForm())){
            return false;
        }
        if (currentFsmParse.getVerbAgreement() != null && currentFsmParse.getPossesiveAgreement() != null && withName != null){
            if (currentFsmParse.getVerbAgreement().equalsIgnoreCase("A3PL") && withName.equalsIgnoreCase("^DB+VERB+ZERO+PRES+A1SG")){
                return false;
            }
            if (currentFsmParse.getVerbAgreement().equalsIgnoreCase("A3SG") && (currentFsmParse.getPossesiveAgreement().equalsIgnoreCase("P1SG") || currentFsmParse.getPossesiveAgreement().equalsIgnoreCase("P2SG")) && withName.equalsIgnoreCase("^DB+VERB+ZERO+PRES+A1PL")){
                return false;
            }
        }
        return true;
    }

    public boolean transitionPossible(TxtWord root, State fromState){
        if (root.isAdjective() && ((root.isNominal() && !root.isExceptional()) || root.isPronoun()) && toState.getName().equalsIgnoreCase("NominalRoot(ADJ)") && with.equalsIgnoreCase("0")){
            return false;
        }
        if (root.isAdjective() && root.isNominal() && with.equalsIgnoreCase("^DB+VERB+ZERO+PRES+A3PL") && fromState.getName().equalsIgnoreCase("AdjectiveRoot")){
            return false;
        }
        if (root.isAdjective() && root.isNominal() && with.equalsIgnoreCase("SH") && fromState.getName().equalsIgnoreCase("AdjectiveRoot")){
            return false;
        }
        if (with.equalsIgnoreCase("ki")){
            return root.takesRelativeSuffixKi();
        }
        if (with.equalsIgnoreCase("kü")){
            return root.takesRelativeSuffixKu();
        }
        if (with.equalsIgnoreCase("dHr")){
            if (toState.getName().equalsIgnoreCase("Adverb")){
                return true;
            } else {
                return root.takesSuffixDIRAsFactitive();
            }
        }
        if (with.equalsIgnoreCase("Hr") && (toState.getName().equalsIgnoreCase("AdjectiveRoot(VERB)") || toState.getName().equalsIgnoreCase("OtherTense") || toState.getName().equalsIgnoreCase("OtherTense2"))){
            return root.takesSuffixIRAsAorist();
        }
        return true;
    }

    private char beforeLastVowel(String stem){
        int i, before = 1;
        char last = '0';
        for (i = stem.length() - 1; i >= 0; i--){
            if (TurkishLanguage.isVowel(stem.charAt(i))){
                if (before == 1){
                    last = stem.charAt(i);
                    before--;
                    continue;
                }
                return stem.charAt(i);
            }
        }
        return last;
    }

    private char lastVowel(String stem){
        int i;
        for (i = stem.length() - 1; i >= 0; i--){
            if (TurkishLanguage.isVowel(stem.charAt(i))){
                return stem.charAt(i);
            }
        }
        for (i = stem.length() - 1; i >= 0; i--){
            if (stem.charAt(i) >= '0' && stem.charAt(i) <= '9'){
                return stem.charAt(i);
            }
        }
        return '0';
    }

    private char lastPhoneme(String stem){
        if (stem.length() == 0){
            return ' ';
        }
        if (stem.charAt(stem.length() - 1) != '\''){
            return stem.charAt(stem.length() - 1);
        } else {
            return stem.charAt(stem.length() - 2);                        
        }
    }

    private char withFirstChar(){
        if (with.length() == 0){
            return '$';
        }
        if (with.charAt(0) != '\''){
            return with.charAt(0);
        } else {
            if (with.length() == 1){
                return with.charAt(0);
            } else {
                return with.charAt(1);                
            }
        }
    }

    private boolean startWithVowelorConsonantDrops(){
        if (TurkishLanguage.isConsonantDrop(withFirstChar()) && (!with.equalsIgnoreCase("ylA")) && (!with.equalsIgnoreCase("ysA")) && (!with.equalsIgnoreCase("ymHs")) && (!with.equalsIgnoreCase("yDH")) && (!with.equalsIgnoreCase("yken"))){
            return true;
        }
        if (withFirstChar() == 'A' || withFirstChar() == 'H' || TurkishLanguage.isVowel(withFirstChar())){
            return true;
        }
        return false;
    }

    public boolean softenDuringSuffixation(TxtWord root){
        if ((root.isNominal() || root.isAdjective()) && root.nounSoftenDuringSuffixation() && (with.equalsIgnoreCase("Hm") || with.equalsIgnoreCase("nDAn") || with.equalsIgnoreCase("ncA") || with.equalsIgnoreCase("nDA") || with.equalsIgnoreCase("yA") || with.equalsIgnoreCase("yHm") || with.equalsIgnoreCase("yHz") || with.equalsIgnoreCase("yH") || with.equalsIgnoreCase("nH") || with.equalsIgnoreCase("nA") || with.equalsIgnoreCase("nHn") || with.equalsIgnoreCase("H") || with.equalsIgnoreCase("sH") || with.equalsIgnoreCase("Hn") || with.equalsIgnoreCase("HnHz") || with.equalsIgnoreCase("HmHz"))){
            return true;
        }
        if (root.isVerb() && root.verbSoftenDuringSuffixation() && (with.startsWith("Hyor") || with.equalsIgnoreCase("yHs") || with.equalsIgnoreCase("yAn") || with.equalsIgnoreCase("yA") || with.startsWith("yAcAk") || with.equalsIgnoreCase("yAsH") || with.equalsIgnoreCase("yHncA") || with.equalsIgnoreCase("yHp") || with.equalsIgnoreCase("yAlH") || with.equalsIgnoreCase("yArAk") || with.equalsIgnoreCase("yAdur") || with.equalsIgnoreCase("yHver") || with.equalsIgnoreCase("yAgel") || with.equalsIgnoreCase("yAgor") || with.equalsIgnoreCase("yAbil") || with.equalsIgnoreCase("yAyaz") || with.equalsIgnoreCase("yAkal") || with.equalsIgnoreCase("yAkoy") || with.equalsIgnoreCase("yAmA") || with.equalsIgnoreCase("yHcH") || with.equalsIgnoreCase("HCH") || with.startsWith("Hr") || with.equalsIgnoreCase("Hs") || with.equalsIgnoreCase("Hn") || with.equalsIgnoreCase("yHn") || with.equalsIgnoreCase("yHnHz") || with.startsWith("Ar") || with.equalsIgnoreCase("Hl"))){
            return true;
        }
        return false;
    }

    public String makeTransition(TxtWord root, String stem){
        if (root.isVerb()){
            return makeTransition(root, stem, new State("VerbalRoot", true, false));
        } else {
            return makeTransition(root, stem, new State("NominalRoot", true, false));
        }
    }

    public String makeTransition(TxtWord root, String stem, State startState){
        boolean rootWord = root.getName().equalsIgnoreCase(stem) || (root.getName() + "'").equalsIgnoreCase(stem);
        String formation = stem;
        int i = 0;
        if (with.equalsIgnoreCase("0")){
            return stem;
        }
        if ((stem.equals("bu") || stem.equals("şu") || stem.equals("o")) && rootWord && with.equalsIgnoreCase("ylA")){
            return stem + "nunla";
        }
        if (with.equalsIgnoreCase("yA")){
            if (stem.equals("ben")){
                return "bana";
            }
        }
        formationToCheck = stem;
        if (rootWord && withFirstChar() == 'y' && root.vowelEChangesToIDuringYSuffixation() && with.charAt(1) != 'H'){
            formation = stem.substring(0, stem.length() - 1) + 'i';
            formationToCheck = formation;
        } else {
            if (rootWord && (with.equalsIgnoreCase("Hl") || with.equalsIgnoreCase("Hn")) && root.lastIdropsDuringPassiveSuffixation()){
                formation = stem.substring(0, stem.length() - 2) + stem.charAt(stem.length() - 1);
                formationToCheck = stem;
            } else {
                if (rootWord && root.showsSuRegularities() && startWithVowelorConsonantDrops() && !with.startsWith("y")){
                    formation = stem + 'y';
                    formationToCheck = formation;
                } else {
                    if (rootWord && root.duplicatesDuringSuffixation() && TurkishLanguage.isConsonantDrop(with.charAt(0))){
                        if (softenDuringSuffixation(root)){
                            switch (lastPhoneme(stem)){
                                case 'p':
                                    formation = stem.substring(0, stem.length() - 1) + "bb";
                                    break;
                                case 'ç':
                                    formation = stem.substring(0, stem.length() - 1) + "cc";
                                    break;
                                case 't':
                                    formation = stem.substring(0, stem.length() - 1) + "dd";
                                    break;
                            }
                        } else {
                            formation = stem + stem.charAt(stem.length() - 1);
                        }
                        formationToCheck = formation;
                    } else {
                        if (rootWord && root.lastIdropsDuringSuffixation() && !startState.getName().startsWith("VerbalRoot") && !startState.getName().startsWith("ProperRoot") && startWithVowelorConsonantDrops()){
                            if (softenDuringSuffixation(root)){
                                switch (lastPhoneme(stem)){
                                    case 'p':
                                        formation = stem.substring(0, stem.length() - 2) + 'b';
                                        break;
                                    case 't':
                                        formation = stem.substring(0, stem.length() - 2) + 'd';
                                        break;
                                    case 'ç':
                                        formation = stem.substring(0, stem.length() - 2) + 'c';
                                        break;
                                    case 'k':
                                        formation = stem.substring(0, stem.length() - 2) + 'g';
                                        break;
                                }
                            } else {
                                formation = stem.substring(0, stem.length() - 2) + stem.charAt(stem.length() - 1);
                            }
                            formationToCheck = stem;
                        } else {
                            switch (lastPhoneme(stem)){
                                case 'p':if (startWithVowelorConsonantDrops() && rootWord && softenDuringSuffixation(root)){
                                            formation = stem.substring(0, stem.length() - 1) + 'b';
                                         }
                                         break;
                                case 't':if (startWithVowelorConsonantDrops() && rootWord && softenDuringSuffixation(root)){
                                            formation = stem.substring(0, stem.length() - 1) + 'd';
                                         }
                                         break;
                                case 'ç':if (startWithVowelorConsonantDrops() && rootWord && softenDuringSuffixation(root)){
                                            formation = stem.substring(0, stem.length() - 1) + 'c';
                                         }
                                         break;
                                case 'g':if (startWithVowelorConsonantDrops() && rootWord && softenDuringSuffixation(root)){
                                            formation = stem.substring(0, stem.length() - 1) + 'ğ';
                                         }
                                         break;
                                case 'k':if (startWithVowelorConsonantDrops() && rootWord && root.endingKChangesIntoG() && !root.isProperNoun()){
                                            formation = stem.substring(0, stem.length() - 1) + 'g';
                                         } else {
                                             //Özel isimler yumuşamaz istisnası
                                             if (startWithVowelorConsonantDrops() && (!rootWord || (softenDuringSuffixation(root) && (!root.isProperNoun() || !startState.toString().equals("ProperRoot"))))){
                                                 formation = stem.substring(0, stem.length() - 1) + 'ğ';
                                             }
                                         }
                                         break;
                            }
                            formationToCheck = formation;
                        }
                    }
                }
            }            
        }
        if (TurkishLanguage.isConsonantDrop(withFirstChar()) && !TurkishLanguage.isVowel(stem.charAt(stem.length() - 1)) && (root.isNumeral() || root.isReal() || root.isFraction() || root.isTime() || root.isDate() || root.isPercent() || root.isRange()) && (root.getName().endsWith("1") || root.getName().endsWith("3") || root.getName().endsWith("4") || root.getName().endsWith("5") || root.getName().endsWith("8") || root.getName().endsWith("9") || root.getName().endsWith("10") || root.getName().endsWith("30") || root.getName().endsWith("40") || root.getName().endsWith("60") || root.getName().endsWith("70") || root.getName().endsWith("80") || root.getName().endsWith("90") || root.getName().endsWith("00"))){
            if (with.charAt(0) == '\''){
                formation = formation + '\'';
                i = 2;
            } else {
                i = 1;
            }
        } else {
            if ((TurkishLanguage.isConsonantDrop(withFirstChar()) && TurkishLanguage.isConsonant(lastPhoneme(stem))) || (rootWord && root.consonantSMayInsertedDuringPossesiveSuffixation())){
                if (with.charAt(0) == '\''){
                    formation = formation + '\'';
                    if (root.isAbbreviation())
                        i = 1;
                    else
                        i = 2;
                } else {
                    i = 1;
                }
            }
        }
        for (; i < with.length(); i++){
            switch (with.charAt(i)){
                case 'D':formation = resolveD(root, formation);
                         break;
                case 'A':formation = resolveA(root, formation, rootWord);
                         break;
                case 'H':
                    if (with.charAt(0) != '\''){
                        formation = resolveH(root, formation, i == 0, with.startsWith("Hyor"), rootWord);
                    } else {
                        formation = resolveH(root, formation, i == 1, false, rootWord);
                    }
                    break;
                case 'C':formation = resolveC(formation);
                         break;
                case 'S':formation = resolveS(formation);
                         break;
                case 'Ş':formation = resolveSh(formation);
                         break;
                default :if (i == with.length() - 1 && with.charAt(i) == 's'){
                             formation += 'ş';                    
                         } else {
                             formation += with.charAt(i);
                         }
            }
            formationToCheck = formation;
        }
        return formation;
    }

    private String resolveD(TxtWord root, String formation){
        if (root.isAbbreviation()){
            return formation + 'd';
        }
        if (lastPhoneme(formationToCheck) >= '0' && lastPhoneme(formationToCheck) <= '9'){
            switch (lastPhoneme(formationToCheck)){
                case '3':
                case '4':
                case '5':
                    return formation + 't';
                case '0':
                    if (root.getName().endsWith("40") || root.getName().endsWith("60") || root.getName().endsWith("70"))
                        return formation + 't';
                    else
                        return formation + 'd';
                default:
                    return formation + 'd';
            }
        } else {
            if (TurkishLanguage.isSertSessiz(lastPhoneme(formationToCheck))){
                return formation + 't';
            } else {
                return formation + 'd';
            }
        }
    }

    private String resolveA(TxtWord root, String formation, boolean rootWord){
        if (root.isAbbreviation()){
            return formation + 'e';
        }
        if (lastVowel(formationToCheck) >= '0' && lastVowel(formationToCheck) <= '9'){
            switch (lastVowel(formationToCheck)){
                case '6':
                case '9':
                    return formation + 'a';
                case '0':
                    if (root.getName().endsWith("10") || root.getName().endsWith("30") || root.getName().endsWith("40") || root.getName().endsWith("60") || root.getName().endsWith("90"))
                        return formation + 'a';
                    else
                        return formation + 'e';
                default:
                    return formation + 'e';
            }
        }
        if (TurkishLanguage.isBackVowel(lastVowel(formationToCheck))){
            if (root.notObeysVowelHarmonyDuringAgglutination() && rootWord){
                return formation + 'e';
            } else {
                return formation + 'a';
            }
        }
        if (TurkishLanguage.isFrontVowel(lastVowel(formationToCheck))){
            if (root.notObeysVowelHarmonyDuringAgglutination() && rootWord){
                return formation + 'a';
            } else {
                return formation + 'e';
            }
        }
        if (root.isNumeral() || root.isFraction() || root.isReal()){
            if (root.getName().endsWith("6") || root.getName().endsWith("9") || root.getName().endsWith("10") || root.getName().endsWith("30") || root.getName().endsWith("40") || root.getName().endsWith("60") || root.getName().endsWith("90")){
                return formation + 'a';
            } else {
                return formation + 'e';
            }
        }
        return formation;
    }

    private String resolveH(TxtWord root, String formation, boolean beginningOfSuffix, boolean specialCaseTenseSuffix, boolean rootWord){
        if (root.isAbbreviation())
            return formation + 'i';
        if (beginningOfSuffix && TurkishLanguage.isVowel(lastPhoneme(formationToCheck)) && !specialCaseTenseSuffix){
            return formation;
        }
        if (specialCaseTenseSuffix){
            if (rootWord){
                if (root.vowelAChangesToIDuringYSuffixation()){
                    if (TurkishLanguage.isFrontRoundedVowel(beforeLastVowel(formationToCheck))){
                        return formation.substring(0, formation.length() - 1) + 'ü';
                    }
                    if (TurkishLanguage.isFrontUnroundedVowel(beforeLastVowel(formationToCheck))){
                        return formation.substring(0, formation.length() - 1) + 'i';
                    }
                    if (TurkishLanguage.isBackRoundedVowel(beforeLastVowel(formationToCheck))){
                        return formation.substring(0, formation.length() - 1) + 'u';
                    }
                    if (TurkishLanguage.isBackUnroundedVowel(beforeLastVowel(formationToCheck))){
                        return formation.substring(0, formation.length() - 1) + 'ı';
                    }
                }
            }
            if (TurkishLanguage.isVowel(lastPhoneme(formationToCheck))){
                if (TurkishLanguage.isFrontRoundedVowel(beforeLastVowel(formationToCheck))){
                    return formation.substring(0, formation.length() - 1) + 'ü';
                }
                if (TurkishLanguage.isFrontUnroundedVowel(beforeLastVowel(formationToCheck))){
                    return formation.substring(0, formation.length() - 1) + 'i';
                }
                if (TurkishLanguage.isBackRoundedVowel(beforeLastVowel(formationToCheck))){
                    return formation.substring(0, formation.length() - 1) + 'u';
                }
                if (TurkishLanguage.isBackUnroundedVowel(beforeLastVowel(formationToCheck))){
                    return formation.substring(0, formation.length() - 1) + 'ı';
                }
            }
        }
        if (TurkishLanguage.isFrontRoundedVowel(lastVowel(formationToCheck)) || (TurkishLanguage.isBackRoundedVowel(lastVowel(formationToCheck)) && root.notObeysVowelHarmonyDuringAgglutination())){
            return formation + 'ü';
        }
        if (TurkishLanguage.isFrontUnroundedVowel(lastVowel(formationToCheck)) || (lastVowel(formationToCheck) == 'a' && root.notObeysVowelHarmonyDuringAgglutination())){
            return formation + 'i';
        }
        if (TurkishLanguage.isBackRoundedVowel(lastVowel(formationToCheck))){
            return formation + 'u';
        }
        if (TurkishLanguage.isBackUnroundedVowel(lastVowel(formationToCheck))){
            return formation + 'ı';
        }
        if (root.isNumeral() || root.isFraction() || root.isReal()){
            if (root.getName().endsWith("6") || root.getName().endsWith("40") || root.getName().endsWith("60") || root.getName().endsWith("90")){
                return formation + 'ı';
            } else {
                if (root.getName().endsWith("3") || root.getName().endsWith("4") || root.getName().endsWith("00")){
                    return formation + 'ü';
                } else {
                    if (root.getName().endsWith("9") || root.getName().endsWith("10") || root.getName().endsWith("30")){
                        return formation + 'u';
                    } else {
                        return formation + 'i';
                    }
                }
            }
        }
        return formation;
    }

    private String resolveC(String formation){
        if (TurkishLanguage.isSertSessiz(lastPhoneme(formationToCheck))){
            return formation + 'ç';
        } else {
            return formation + 'c';
        }
    }

    private String resolveS(String formation){
        return formation + 's';
    }

    private String resolveSh(String formation){
        if (TurkishLanguage.isVowel(formation.charAt(formation.length() - 1))){
            return formation + 'ş';
        } else {
            if (formation.charAt(formation.length() - 1) != 't')
                return formation;
            else
                return formation.substring(0, formation.length() - 1) + 'd';
        }
    }

    public String toString(){
        return with;
    }

    public String with(){
        return withName;
    }
}
