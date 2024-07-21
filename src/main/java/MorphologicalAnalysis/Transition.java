package MorphologicalAnalysis;

import Dictionary.TxtWord;
import Dictionary.Word;
import Language.TurkishLanguage;

public class Transition {
    private final State toState;
    private final String with;
    private final String withName;
    private final String toPos;

    /**
     * A constructor of {@link Transition} class which takes  a {@link State}, and two {@link String}s as input. Then it
     * initializes toState, with and withName variables with given inputs.
     *
     * @param toState  {@link State} input.
     * @param with     String input.
     * @param withName String input.
     */
    public Transition(State toState, String with, String withName) {
        this.toState = toState;
        this.with = with;
        this.withName = withName;
        toPos = null;
    }

    /**
     * Another constructor of {@link Transition} class which takes  a {@link State}, and three {@link String}s as input. Then it
     * initializes toState, with, withName and toPos variables with given inputs.
     *
     * @param toState  {@link State} input.
     * @param with     String input.
     * @param withName String input.
     * @param toPos    String input.
     */
    public Transition(State toState, String with, String withName, String toPos) {
        this.toState = toState;
        this.with = with;
        this.withName = withName;
        this.toPos = toPos;
    }

    /**
     * Another constructor of {@link Transition} class which only takes a {@link String}s as an input. Then it
     * initializes toState, withName and toPos variables as null and with variable with the given input.
     *
     * @param with String input.
     */
    public Transition(String with) {
        toState = null;
        withName = null;
        toPos = null;
        this.with = with;
    }

    /**
     * Getter for the toState variable.
     *
     * @return toState variable.
     */
    public State toState() {
        return toState;
    }

    /**
     * Getter for the toPos variable.
     *
     * @return toPos variable.
     */
    public String toPos() {
        return toPos;
    }

    /**
     * The transitionPossible method takes two {@link String} as inputs; currentSurfaceForm and realSurfaceForm. If the
     * length of the given currentSurfaceForm is greater than the given realSurfaceForm, it directly returns true. If not,
     * it takes a substring from given realSurfaceForm with the size of currentSurfaceForm. Then checks for the characters of
     * with variable.
     * <p>
     * If the character of with that makes transition is C, it returns true if the substring contains c or ç.
     * If the character of with that makes transition is D, it returns true if the substring contains d or t.
     * If the character of with that makes transition is A, it returns true if the substring contains a or e.
     * If the character of with that makes transition is K, it returns true if the substring contains k, g or ğ.
     * If the character of with that makes transition is other than the ones above, it returns true if the substring
     * contains the same character as with.
     *
     * @param currentSurfaceForm {@link String} input.
     * @param realSurfaceForm    {@link String} input.
     * @return true when the transition is possible according to Turkish grammar, false otherwise.
     */
    public boolean transitionPossible(String currentSurfaceForm, String realSurfaceForm) {
        if (currentSurfaceForm.isEmpty() || currentSurfaceForm.length() >= realSurfaceForm.length()) {
            return true;
        }
        String searchString = realSurfaceForm.substring(currentSurfaceForm.length());
        for (int i = 0; i < with.length(); i++) {
            switch (with.charAt(i)) {
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

    /**
     * The transitionPossible method takes a current parse as an input. It then checks some special  cases.
     *
     * @param currentFsmParse Parse to be checked
     * @return true if transition is possible false otherwise
     */
    public boolean transitionPossible(FsmParse currentFsmParse) {
        if (with.equalsIgnoreCase("Ar") && currentFsmParse.getSurfaceForm().endsWith("l") && !currentFsmParse.getWord().getName().equalsIgnoreCase(currentFsmParse.getSurfaceForm())) {
            return false;
        }
        if (currentFsmParse.getVerbAgreement() != null && currentFsmParse.getPossesiveAgreement() != null && withName != null) {
            if (currentFsmParse.getVerbAgreement().equalsIgnoreCase("A3PL") && withName.equalsIgnoreCase("^DB+VERB+ZERO+PRES+A1SG")) {
                return false;
            }
            return !currentFsmParse.getVerbAgreement().equalsIgnoreCase("A3SG") || (!currentFsmParse.getPossesiveAgreement().equalsIgnoreCase("P1SG") && !currentFsmParse.getPossesiveAgreement().equalsIgnoreCase("P2SG")) || !withName.equalsIgnoreCase("^DB+VERB+ZERO+PRES+A1PL");
        }
        return true;
    }

    /**
     * The transitionPossible method takes root and current parse as inputs. It then checks some special cases.
     *
     * @param root Current root word
     * @param fromState From which state we arrived to this state.
     * @return true if transition is possible false otherwise
     */
    public boolean transitionPossible(TxtWord root, State fromState) {
        if (root.isAdjective() && ((root.isNominal() && !root.isExceptional()) || root.isPronoun()) && toState.getName().equalsIgnoreCase("NominalRoot(ADJ)") && with.equalsIgnoreCase("0")) {
            return false;
        }
        if (root.isAdjective() && root.isNominal() && with.equalsIgnoreCase("^DB+VERB+ZERO+PRES+A3PL") && fromState.getName().equalsIgnoreCase("AdjectiveRoot")) {
            return false;
        }
        if (root.isAdjective() && root.isNominal() && with.equalsIgnoreCase("SH") && fromState.getName().equalsIgnoreCase("AdjectiveRoot")) {
            return false;
        }
        if (with.equalsIgnoreCase("ki")) {
            return root.takesRelativeSuffixKi();
        }
        if (with.equalsIgnoreCase("kü")) {
            return root.takesRelativeSuffixKu();
        }
        if (with.equalsIgnoreCase("dHr")) {
            if (toState.getName().equalsIgnoreCase("Adverb")) {
                return true;
            } else {
                return root.takesSuffixDIRAsFactitive();
            }
        }
        if (with.equalsIgnoreCase("Hr") && (toState.getName().equalsIgnoreCase("AdjectiveRoot(VERB)") || toState.getName().equalsIgnoreCase("OtherTense") || toState.getName().equalsIgnoreCase("OtherTense2"))) {
            return root.takesSuffixIRAsAorist();
        }
        return true;
    }

    /**
     * The withFirstChar method returns the first character of the with variable.
     *
     * @return the first character of the with variable.
     */
    private char withFirstChar() {
        if (with.isEmpty()) {
            return '$';
        }
        if (with.charAt(0) != '\'') {
            return with.charAt(0);
        } else {
            if (with.length() == 1) {
                return with.charAt(0);
            } else {
                return with.charAt(1);
            }
        }
    }

    /**
     * The startWithVowelorConsonantDrops method checks for some cases. If the first character of with variable is "nsy",
     * and with variable does not equal to one of the Strings; "ylA, ysA, ymHs, yDH, yken", it returns true. If
     * <p>
     * Or, if the first character of with variable is 'A', 'H': or any other vowels, it returns true.
     *
     * @return true if it starts with vowel or consonant drops, false otherwise.
     */
    private boolean startWithVowelorConsonantDrops() {
        if (TurkishLanguage.isConsonantDrop(withFirstChar()) && !with.equalsIgnoreCase("ylA") && !with.equalsIgnoreCase("ysA") && !with.equalsIgnoreCase("ymHs") && !with.equalsIgnoreCase("yDH") && !with.equalsIgnoreCase("yken")) {
            return true;
        }
        return withFirstChar() == 'A' || withFirstChar() == 'H' || TurkishLanguage.isVowel(withFirstChar());
    }

    /**
     * The softenDuringSuffixation method takes a {@link TxtWord} root as an input. It checks two cases; first case returns
     * true if the given root is nominal or adjective and has one of the flags "IS_SD, IS_B_SD, IS_SDD" and with variable
     * equals o one of the Strings "Hm, nDAn, ncA, nDA, yA, yHm, yHz, yH, nH, nA, nHn, H, sH, Hn, HnHz, HmHz".
     * <p>
     * And the second case returns true if the given root is verb and has the "F_SD" flag, also with variable starts with
     * "Hyor" or equals one of the Strings "yHs, yAn, yA, yAcAk, yAsH, yHncA, yHp, yAlH, yArAk, yAdur, yHver, yAgel, yAgor,
     * yAbil, yAyaz, yAkal, yAkoy, yAmA, yHcH, HCH, Hr, Hs, Hn, yHn", yHnHz, Ar, Hl").
     *
     * @param root {@link TxtWord} input.
     * @return true if there is softening during suffixation of the given root, false otherwise.
     */
    public boolean softenDuringSuffixation(TxtWord root) {
        if ((root.isNominal() || root.isAdjective()) && root.nounSoftenDuringSuffixation() && (with.equalsIgnoreCase("Hm") || with.equalsIgnoreCase("nDAn") || with.equalsIgnoreCase("ncA") || with.equalsIgnoreCase("nDA") || with.equalsIgnoreCase("yA") || with.equalsIgnoreCase("yHm") || with.equalsIgnoreCase("yHz") || with.equalsIgnoreCase("yH") || with.equalsIgnoreCase("nH") || with.equalsIgnoreCase("nA") || with.equalsIgnoreCase("nHn") || with.equalsIgnoreCase("H") || with.equalsIgnoreCase("sH") || with.equalsIgnoreCase("Hn") || with.equalsIgnoreCase("HnHz") || with.equalsIgnoreCase("HmHz"))) {
            return true;
        }
        return root.isVerb() && root.verbSoftenDuringSuffixation() && (with.startsWith("Hyor") || with.equalsIgnoreCase("yHs") || with.equalsIgnoreCase("yAn") || with.equalsIgnoreCase("yA") || with.startsWith("yAcAk") || with.equalsIgnoreCase("yAsH") || with.equalsIgnoreCase("yHncA") || with.equalsIgnoreCase("yHp") || with.equalsIgnoreCase("yAlH") || with.equalsIgnoreCase("yArAk") || with.equalsIgnoreCase("yAdur") || with.equalsIgnoreCase("yHver") || with.equalsIgnoreCase("yAgel") || with.equalsIgnoreCase("yAgor") || with.equalsIgnoreCase("yAbil") || with.equalsIgnoreCase("yAyaz") || with.equalsIgnoreCase("yAkal") || with.equalsIgnoreCase("yAkoy") || with.equalsIgnoreCase("yAmA") || with.equalsIgnoreCase("yHcH") || with.equalsIgnoreCase("HCH") || with.startsWith("Hr") || with.equalsIgnoreCase("Hs") || with.equalsIgnoreCase("Hn") || with.equalsIgnoreCase("yHn") || with.equalsIgnoreCase("yHnHz") || with.startsWith("Ar") || with.equalsIgnoreCase("Hl"));
    }

    /**
     * The makeTransition method takes a {@link TxtWord} root and s {@link String} stem as inputs. If given root is a verb,
     * it makes transition with given root and stem with the verbal root state. If given root is not verb, it makes transition
     * with given root and stem and the nominal root state.
     *
     * @param root {@link TxtWord} input.
     * @param stem String input.
     * @return String type output that has the transition.
     */
    public String makeTransition(TxtWord root, String stem) {
        if (root.isVerb()) {
            return makeTransition(root, stem, new State("VerbalRoot", true, false));
        } else {
            return makeTransition(root, stem, new State("NominalRoot", true, false));
        }
    }

    /**
     * The method is main driving method to accomplish the current transition from one state to another depending on
     * the root form of the word, current value of the word form, and the type of the start state. The method
     * (a) returns the original word form if the transition is an epsilon transition, (b) adds 'nunla' if the current
     * stem is 'bu', 'şu' or 'o', (c) returns 'bana' or 'sana' if the current stem is 'ben' or 'sen' respectively.
     * For other cases, the method first modifies current stem and then adds the transition using special metamorpheme
     * resolving methods. These cases are: (d) Converts 'y' of the first character of the transition to 'i' if the
     * current stem is 'ye' or 'de'. (e) Drops the last two characters and adds last character when the transition is
     * ('Hl' or 'Hn') and last 'I' drops during passive suffixation. (f) Adds 'y' character when the word ends with 'su'
     * and the transition does not start with 'y'. (g) Adds the last character again when the root duplicates during
     * suffixation. (h) Drops the last two characters and adds the last character when last 'i' drops during
     * suffixation. (i) Replaces the last character with a soft one when the root soften during suffixation.
     * @param root Root of the current word form
     * @param stem Current word form
     * @param startState The state from which this Fsm morphological analysis search has started.
     * @return The current value of the word form after this transition is completed in the finite state machine.
     */
    public String makeTransition(TxtWord root, String stem, State startState) {
        boolean rootWord = root.getName().equalsIgnoreCase(stem) || (root.getName() + "'").equalsIgnoreCase(stem);
        String formation = stem;
        int i = 0;
        if (with.equalsIgnoreCase("0")) {
            return stem;
        }
        if ((stem.equals("bu") || stem.equals("şu") || stem.equals("o")) && rootWord && with.equalsIgnoreCase("ylA")) {
            return stem + "nunla";
        }
        if (with.equalsIgnoreCase("yA")) {
            if (stem.equals("ben")) {
                return "bana";
            }
            if (stem.equals("sen")) {
                return "sana";
            }
        }
        String formationToCheck;
        //---vowelEChangesToIDuringYSuffixation---
        //de->d(i)yor, ye->y(i)yor
        if (rootWord && withFirstChar() == 'y' && root.vowelEChangesToIDuringYSuffixation() && (with.charAt(1) != 'H' || root.getName().equals("ye"))) {
            formation = stem.substring(0, stem.length() - 1) + 'i';
            formationToCheck = formation;
        } else {
            //---lastIdropsDuringPassiveSuffixation---
            // yoğur->yoğrul, ayır->ayrıl, buyur->buyrul, çağır->çağrıl, çevir->çevril, devir->devril,
            // kavur->kavrul, kayır->kayrıl, kıvır->kıvrıl, savur->savrul, sıyır->sıyrıl, yoğur->yoğrul
            if (rootWord && (with.equalsIgnoreCase("Hl") || with.equalsIgnoreCase("Hn")) && root.lastIdropsDuringPassiveSuffixation()) {
                formation = stem.substring(0, stem.length() - 2) + stem.charAt(stem.length() - 1);
                formationToCheck = stem;
            } else {
                //---showsSuRegularities---
                //karasu->karasuyu, su->suyu, ağırsu->ağırsuyu, akarsu->akarsuyu, bengisu->bengisuyu
                if (rootWord && root.showsSuRegularities() && startWithVowelorConsonantDrops()) {
                    formation = stem + 'y';
                    i = 1;
                    formationToCheck = formation;
                } else {
                    if (rootWord && root.duplicatesDuringSuffixation() && !startState.getName().startsWith("VerbalRoot") && TurkishLanguage.isConsonantDrop(with.charAt(0))) {
                        //---duplicatesDuringSuffixation---
                        if (softenDuringSuffixation(root)) {
                            //--extra softenDuringSuffixation
                            switch (Word.lastPhoneme(stem)) {
                                case 'p':
                                    //tıp->tıbbı
                                    formation = stem.substring(0, stem.length() - 1) + "bb";
                                    break;
                                case 't':
                                    //cet->ceddi, met->meddi, ret->reddi, serhat->serhaddi, zıt->zıddı, şet->şeddi
                                    formation = stem.substring(0, stem.length() - 1) + "dd";
                                    break;
                            }
                        } else {
                            //cer->cerri, emrihak->emrihakkı, fek->fekki, fen->fenni, had->haddi, hat->hattı,
                            // haz->hazzı, his->hissi
                            formation = stem + stem.charAt(stem.length() - 1);
                        }
                        formationToCheck = formation;
                    } else {
                        if (rootWord && root.lastIdropsDuringSuffixation() && !startState.getName().startsWith("VerbalRoot") && !startState.getName().startsWith("ProperRoot") && startWithVowelorConsonantDrops()) {
                            //---lastIdropsDuringSuffixation---
                            String stemExceptLastTwo = stem.substring(0, stem.length() - 2);
                            if (softenDuringSuffixation(root)) {
                                //---softenDuringSuffixation---
                                switch (Word.lastPhoneme(stem)) {
                                    case 'p':
                                        //hizip->hizbi, kayıp->kaybı, kayıt->kaydı, kutup->kutbu
                                        formation = stemExceptLastTwo + 'b';
                                        break;
                                    case 't':
                                        //akit->akdi, ahit->ahdi, lahit->lahdi, nakit->nakdi, vecit->vecdi
                                        formation = stemExceptLastTwo + 'd';
                                        break;
                                    case 'ç':
                                        //eviç->evci, nesiç->nesci
                                        formation = stemExceptLastTwo + 'c';
                                        break;
                                }
                            } else {
                                //sarıağız->sarıağzı, zehir->zehri, zikir->zikri, nutuk->nutku, omuz->omzu, ömür->ömrü
                                //lütuf->lütfu, metin->metni, kavim->kavmi, kasıt->kastı
                                formation = stemExceptLastTwo + stem.charAt(stem.length() - 1);
                            }
                            formationToCheck = stem;
                        } else {
                            switch (Word.lastPhoneme(stem)) {
                                //---nounSoftenDuringSuffixation or verbSoftenDuringSuffixation
                                case 'p':
                                    //adap->adabı, amip->amibi, azap->azabı, gazap->gazabı
                                    if (startWithVowelorConsonantDrops() && rootWord && softenDuringSuffixation(root)) {
                                        formation = stem.substring(0, stem.length() - 1) + 'b';
                                    }
                                    break;
                                case 't':
                                    //adet->adedi, akort->akordu, armut->armudu
                                    //affet->affedi, yoket->yokedi, sabret->sabredi, rakset->raksedi
                                    if (startWithVowelorConsonantDrops() && rootWord && softenDuringSuffixation(root)) {
                                        formation = stem.substring(0, stem.length() - 1) + 'd';
                                    }
                                    break;
                                case 'ç':
                                    //ağaç->ağacı, almaç->almacı, akaç->akacı, avuç->avucu
                                    if (startWithVowelorConsonantDrops() && rootWord && softenDuringSuffixation(root)) {
                                        formation = stem.substring(0, stem.length() - 1) + 'c';
                                    }
                                    break;
                                case 'g':
                                    //arkeolog->arkeoloğu, filolog->filoloğu, minerolog->mineroloğu
                                    if (startWithVowelorConsonantDrops() && rootWord && softenDuringSuffixation(root)) {
                                        formation = stem.substring(0, stem.length() - 1) + 'ğ';
                                    }
                                    break;
                                case 'k':
                                    //ahenk->ahengi, künk->küngü, renk->rengi, pelesenk->pelesengi
                                    if (startWithVowelorConsonantDrops() && rootWord && root.endingKChangesIntoG() && (!root.isProperNoun() || !startState.toString().equals("ProperRoot"))) {
                                        formation = stem.substring(0, stem.length() - 1) + 'g';
                                    } else {
                                        //ablak->ablağı, küllük->küllüğü, kitaplık->kitaplığı, evcilik->evciliği
                                        if (startWithVowelorConsonantDrops() && (!rootWord || (softenDuringSuffixation(root) && (!root.isProperNoun() || !startState.toString().equals("ProperRoot"))))) {
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
        if (TurkishLanguage.isConsonantDrop(withFirstChar()) && !TurkishLanguage.isVowel(stem.charAt(stem.length() - 1)) && (root.isNumeral() || root.isReal() || root.isFraction() || root.isTime() || root.isDate() || root.isPercent() || root.isRange()) && (root.getName().endsWith("1") || root.getName().endsWith("3") || root.getName().endsWith("4") || root.getName().endsWith("5") || root.getName().endsWith("8") || root.getName().endsWith("9") || root.getName().endsWith("10") || root.getName().endsWith("30") || root.getName().endsWith("40") || root.getName().endsWith("60") || root.getName().endsWith("70") || root.getName().endsWith("80") || root.getName().endsWith("90") || root.getName().endsWith("00"))) {
            if (with.charAt(0) == '\'') {
                formation = formation + '\'';
                i = 2;
            } else {
                i = 1;
            }
        } else {
            if ((TurkishLanguage.isConsonantDrop(withFirstChar()) && TurkishLanguage.isConsonant(Word.lastPhoneme(stem))) || (rootWord && root.consonantSMayInsertedDuringPossesiveSuffixation())) {
                if (with.charAt(0) == '\'') {
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
        for (; i < with.length(); i++) {
            switch (with.charAt(i)) {
                case 'D':
                    formation = MorphotacticEngine.resolveD(root, formation, formationToCheck);
                    break;
                case 'A':
                    formation = MorphotacticEngine.resolveA(root, formation, rootWord, formationToCheck);
                    break;
                case 'H':
                    if (with.charAt(0) != '\'') {
                        formation = MorphotacticEngine.resolveH(root, formation, i == 0, with.startsWith("Hyor"), rootWord, formationToCheck);
                    } else {
                        formation = MorphotacticEngine.resolveH(root, formation, i == 1, false, rootWord, formationToCheck);
                    }
                    break;
                case 'C':
                    formation = MorphotacticEngine.resolveC(formation, formationToCheck);
                    break;
                case 'S':
                    formation = MorphotacticEngine.resolveS(formation);
                    break;
                case 'Ş':
                    formation = MorphotacticEngine.resolveSh(formation);
                    break;
                default:
                    if (i == with.length() - 1 && with.charAt(i) == 's') {
                        formation += 'ş';
                    } else {
                        formation += with.charAt(i);
                    }
            }
            formationToCheck = formation;
        }
        return formation;
    }

    /**
     * An overridden toString method which returns the with variable.
     *
     * @return with variable.
     */
    public String toString() {
        return with;
    }

    /**
     * The with method returns the withName variable.
     *
     * @return the withName variable.
     */
    public String with() {
        return withName;
    }
}
