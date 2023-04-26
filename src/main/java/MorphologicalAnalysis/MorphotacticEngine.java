package MorphologicalAnalysis;

import Dictionary.TxtWord;
import Dictionary.Word;
import Language.TurkishLanguage;

public class MorphotacticEngine {

    /**
     * The resolveSh method takes a {@link String} formation as an input. If the last character is a vowel, it concatenates
     * given formation with ş, if the last character is not a vowel, and not 't' it directly returns given formation, but if it
     * is equal to 't', it transforms it to 'd'.
     *
     * @param formation {@link String} input.
     * @return resolved String.
     */
    public static String resolveSh(String formation) {
        if (TurkishLanguage.isVowel(formation.charAt(formation.length() - 1))) {
            return formation + 'ş';
        } else {
            if (formation.charAt(formation.length() - 1) != 't')
                return formation;
            else
                return formation.substring(0, formation.length() - 1) + 'd';
        }
    }

    /**
     * The resolveS method takes a {@link String} formation as an input. It then concatenates given formation with 's'.
     *
     * @param formation {@link String} input.
     * @return resolved String.
     */
    public static String resolveS(String formation) {
        return formation + 's';
    }

    public static String resolveD(TxtWord root, String formation, String formationToCheck) {
        if (root.isAbbreviation()) {
            return formation + 'd';
        }
        if (Word.lastPhoneme(formationToCheck) >= '0' && Word.lastPhoneme(formationToCheck) <= '9') {
            switch (Word.lastPhoneme(formationToCheck)) {
                case '3':
                case '4':
                case '5':
                    //3->3'tü, 5->5'ti, 4->4'tü
                    return formation + 't';
                case '0':
                    if (root.getName().endsWith("40") || root.getName().endsWith("60") || root.getName().endsWith("70"))
                        //40->40'tı, 60->60'tı, 70->70'ti
                        return formation + 't';
                    else
                        //30->30'du, 50->50'ydi, 80->80'di
                        return formation + 'd';
                default:
                    return formation + 'd';
            }
        } else {
            if (TurkishLanguage.isSertSessiz(Word.lastPhoneme(formationToCheck))) {
                //yap+DH->yaptı
                return formation + 't';
            } else {
                //sar+DH->sardı
                return formation + 'd';
            }
        }
    }

    //// TODO: 7/23/2018
    public static String resolveA(TxtWord root, String formation, boolean rootWord, String formationToCheck) {
        if (root.isAbbreviation()) {
            return formation + 'e';
        }
        if (Word.lastVowel(formationToCheck) >= '0' && Word.lastVowel(formationToCheck) <= '9') {
            switch (Word.lastVowel(formationToCheck)) {
                case '6':
                case '9':
                    //6'ya, 9'a
                    return formation + 'a';
                case '0':
                    if (root.getName().endsWith("10") || root.getName().endsWith("30") || root.getName().endsWith("40") || root.getName().endsWith("60") || root.getName().endsWith("90"))
                        //10'a, 30'a, 40'a, 60'a, 90'a
                        return formation + 'a';
                    else
                        //20'ye, 50'ye, 80'e, 70'e
                        return formation + 'e';
                default:
                    //3'e, 8'e, 4'e, 2'ye
                    return formation + 'e';
            }
        }
        if (TurkishLanguage.isBackVowel(Word.lastVowel(formationToCheck))) {
            if (root.notObeysVowelHarmonyDuringAgglutination() && rootWord) {
                //alkole, anormale
                return formation + 'e';
            } else {
                //sakala, kabala
                return formation + 'a';
            }
        }
        if (TurkishLanguage.isFrontVowel(Word.lastVowel(formationToCheck))) {
            if (root.notObeysVowelHarmonyDuringAgglutination() && rootWord) {
                //faika, halika
                return formation + 'a';
            } else {
                //kediye, eve
                return formation + 'e';
            }
        }
        if (root.isNumeral() || root.isFraction() || root.isReal()) {
            if (root.getName().endsWith("6") || root.getName().endsWith("9") || root.getName().endsWith("10") || root.getName().endsWith("30") || root.getName().endsWith("40") || root.getName().endsWith("60") || root.getName().endsWith("90")) {
                return formation + 'a';
            } else {
                return formation + 'e';
            }
        }
        return formation;
    }

    public static String resolveH(TxtWord root, String formation, boolean beginningOfSuffix, boolean specialCaseTenseSuffix, boolean rootWord, String formationToCheck) {
        if (root.isAbbreviation())
            return formation + 'i';
        if (beginningOfSuffix && TurkishLanguage.isVowel(Word.lastPhoneme(formationToCheck)) && !specialCaseTenseSuffix) {
            return formation;
        }
        if (specialCaseTenseSuffix) {
            //eğer ek Hyor eki ise,
            if (rootWord) {
                if (root.vowelAChangesToIDuringYSuffixation()) {
                    if (TurkishLanguage.isFrontRoundedVowel(Word.beforeLastVowel(formationToCheck))) {
                        //büyülüyor, bölümlüyor, çözümlüyor, döşüyor
                        return formation.substring(0, formation.length() - 1) + 'ü';
                    }
                    if (TurkishLanguage.isFrontUnroundedVowel(Word.beforeLastVowel(formationToCheck))) {
                        //adresliyor, alevliyor, ateşliyor, bekliyor
                        return formation.substring(0, formation.length() - 1) + 'i';
                    }
                    if (TurkishLanguage.isBackRoundedVowel(Word.beforeLastVowel(formationToCheck))) {
                        //buğuluyor, bulguluyor, çamurluyor, aforozluyor
                        return formation.substring(0, formation.length() - 1) + 'u';
                    }
                    if (TurkishLanguage.isBackUnroundedVowel(Word.beforeLastVowel(formationToCheck))) {
                        //açıklıyor, çalkalıyor, gazlıyor, gıcırdıyor
                        return formation.substring(0, formation.length() - 1) + 'ı';
                    }
                }
            }
            if (TurkishLanguage.isVowel(Word.lastPhoneme(formationToCheck))) {
                if (TurkishLanguage.isFrontRoundedVowel(Word.beforeLastVowel(formationToCheck))) {
                    return formation.substring(0, formation.length() - 1) + 'ü';
                }
                if (TurkishLanguage.isFrontUnroundedVowel(Word.beforeLastVowel(formationToCheck))) {
                    return formation.substring(0, formation.length() - 1) + 'i';
                }
                if (TurkishLanguage.isBackRoundedVowel(Word.beforeLastVowel(formationToCheck))) {
                    return formation.substring(0, formation.length() - 1) + 'u';
                }
                if (TurkishLanguage.isBackUnroundedVowel(Word.beforeLastVowel(formationToCheck))) {
                    return formation.substring(0, formation.length() - 1) + 'ı';
                }
            }
        }
        if (TurkishLanguage.isFrontRoundedVowel(Word.lastVowel(formationToCheck)) || (TurkishLanguage.isBackRoundedVowel(Word.lastVowel(formationToCheck)) && root.notObeysVowelHarmonyDuringAgglutination())) {
            return formation + 'ü';
        }
        if ((TurkishLanguage.isFrontUnroundedVowel(Word.lastVowel(formationToCheck)) && !root.notObeysVowelHarmonyDuringAgglutination()) || ((Word.lastVowel(formationToCheck) == 'a' || Word.lastVowel(formationToCheck) == 'â') && root.notObeysVowelHarmonyDuringAgglutination())) {
            return formation + 'i';
        }
        if (TurkishLanguage.isBackRoundedVowel(Word.lastVowel(formationToCheck))) {
            return formation + 'u';
        }
        if (TurkishLanguage.isBackUnroundedVowel(Word.lastVowel(formationToCheck)) || (TurkishLanguage.isFrontUnroundedVowel(Word.lastVowel(formationToCheck)) && root.notObeysVowelHarmonyDuringAgglutination())) {
            return formation + 'ı';
        }
        if (root.isNumeral() || root.isFraction() || root.isReal()) {
            if (root.getName().endsWith("6") || root.getName().endsWith("40") || root.getName().endsWith("60") || root.getName().endsWith("90")) {
                //6'yı, 40'ı, 60'ı
                return formation + 'ı';
            } else {
                if (root.getName().endsWith("3") || root.getName().endsWith("4") || root.getName().endsWith("00")) {
                    //3'ü, 4'ü, 100'ü
                    return formation + 'ü';
                } else {
                    if (root.getName().endsWith("9") || root.getName().endsWith("10") || root.getName().endsWith("30")) {
                        //9'u, 10'u, 30'u
                        return formation + 'u';
                    } else {
                        //2'yi, 5'i, 8'i
                        return formation + 'i';
                    }
                }
            }
        }
        return formation;
    }

    /**
     * The resolveC method takes a {@link String} formation as an input. If the last phoneme is on of the "çfhkpsşt", it
     * concatenates given formation with 'ç', if not it concatenates given formation with 'c'.
     *
     * @param formation {@link String} input.
     * @param formationToCheck Formation produced until so far.
     * @return resolved String.
     */
    public static String resolveC(String formation, String formationToCheck) {
        if (TurkishLanguage.isSertSessiz(Word.lastPhoneme(formationToCheck))) {
            return formation + 'ç';
        } else {
            return formation + 'c';
        }
    }

}
