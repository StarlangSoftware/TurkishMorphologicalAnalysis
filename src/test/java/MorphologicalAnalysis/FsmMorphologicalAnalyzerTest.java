package MorphologicalAnalysis;

import Corpus.Sentence;
import Dictionary.TxtDictionary;
import Dictionary.TxtWord;
import Util.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

import static MorphologicalAnalysis.MetamorphicParse.createWithList;
import static org.junit.Assert.*;

public class FsmMorphologicalAnalyzerTest {
    FsmMorphologicalAnalyzer fsm;

    @Before
    public void setUp() {
        fsm = new FsmMorphologicalAnalyzer();
    }

    private void createSuffixList(String fileName, String[] examples){
        Scanner suffixInput;
        String metaSuffix;
        suffixInput = new Scanner(FileUtils.getInputStream(fileName));
        while (suffixInput.hasNext()) {
            metaSuffix = suffixInput.next();
            ArrayList<String> list = createWithList(metaSuffix);
            for (String word : examples) {
                String added = word;
                for (String suffix : list) {
                    Transition transition = new Transition(suffix);
                    added = transition.makeTransition((TxtWord) fsm.getDictionary().getWord(word), added);
                }
                if (added.startsWith(word)) {
                    System.out.println(added.substring(word.length()));
                }
            }
        }
        suffixInput.close();
    }
    @Test
    public void createSuffixList() {
        String[] nouns = {"yara", "av", "ev", "sene", "yapı", "kırım", "kedi", "zeytin", "koro", "kor", "indikatör", "banliyö", "boru", "iridyum", "güdü", "ölüm"};
        String[] verbs = {"kamçıla", "kanatlan", "kandilleş", "kaşele", "kaşı", "kaşın", "ekşi", "kemir", "hallol", "göm", "koru", "koştur", "sürü", "köpür"};
        createSuffixList("noun-meta-suffixes.txt", nouns);
        createSuffixList("verb-meta-suffixes.txt", verbs);
    }

    @Test
    public void morphologicalAnalysisSpecialProperNoun() {
        assertTrue(fsm.morphologicalAnalysis("Times'ın").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("Times'tır").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("Times'mış").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("Twitter'ın").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("Twitter'dır").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("Twitter'mış").size() != 0);
    }

    @Test
    public void morphologicalAnalysisNewWords() {
        assertTrue(fsm.robustMorphologicalAnalysis("googlecılardan").size() != 0);
        assertTrue(fsm.robustMorphologicalAnalysis("zaptıraplaştırılmayana").size() != 0);
        assertTrue(fsm.robustMorphologicalAnalysis("abzürtleşenmiş").size() != 0);
        assertTrue(fsm.robustMorphologicalAnalysis("vışlığından").size() != 0);
    }

    @Test
    public void morphologicalAnalysisDataTimeNumber() {
        assertTrue(fsm.morphologicalAnalysis("3/4").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("3\\/4").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("4/2/1973").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("14/2/1993").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("14/12/1933").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("6/12/1903").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("%34.5").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("%3").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("%56").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("2:3").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("12:3").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("4:23").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("11:56").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("1:2:3").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("3:12:3").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("5:4:23").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("7:11:56").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("12:2:3").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("10:12:3").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("11:4:23").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("22:11:56").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("45").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("34.23").size() != 0);
    }

    @Test
    public void morphologicalAnalysisProperNoun() {
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++) {
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isProperNoun()) {
                assertTrue(fsm.morphologicalAnalysis(word.getName().toUpperCase(new Locale("tr"))).size() != 0);
            }
        }
    }

    @Test
    public void morphologicalAnalysisNounSoftenDuringSuffixation() {
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++) {
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isNominal() && word.nounSoftenDuringSuffixation()) {
                State transitionState = new State("Possessive", false, false);
                State startState = new State("NominalRoot", true, false);
                Transition transition = new Transition(transitionState, "yH", "ACC");
                String surfaceForm = transition.makeTransition(word, word.getName(), startState);
                assertTrue(fsm.morphologicalAnalysis(surfaceForm).size() != 0);
            }
        }
    }

    @Test
    public void morphologicalAnalysisVowelAChangesToIDuringYSuffixation() {
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++) {
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isVerb() && word.vowelAChangesToIDuringYSuffixation()) {
                State transitionState = new State("VerbalStem", false, false);
                State startState = new State("VerbalRoot", true, false);
                Transition transition = new Transition(transitionState, "Hyor", "PROG1");
                String surfaceForm = transition.makeTransition(word, word.getName(), startState);
                assertTrue(fsm.morphologicalAnalysis(surfaceForm).size() != 0);
            }
        }
    }

    @Test
    public void morphologicalAnalysisIsPortmanteau() {
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++) {
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isNominal() && word.isPortmanteau() && !word.isPlural() && !word.isPortmanteauFacedVowelEllipsis()) {
                State transitionState = new State("CompoundNounRoot", true, false);
                State startState = new State("CompoundNounRoot", true, false);
                Transition transition = new Transition(transitionState, "lArH", "A3PL+P3PL");
                String rootForm, surfaceForm, exceptLast2, exceptLast;
                exceptLast2 = word.getName().substring(0, word.getName().length() - 2);
                exceptLast = word.getName().substring(0, word.getName().length() - 1);
                if (word.isPortmanteauFacedSoftening()) {
                    switch (word.getName().charAt(word.getName().length() - 2)) {
                        case 'b':
                            rootForm = exceptLast2 + 'p';
                            break;
                        case 'c':
                            rootForm = exceptLast2 + 'ç';
                            break;
                        case 'd':
                            rootForm = exceptLast2 + 't';
                            break;
                        case 'ğ':
                            rootForm = exceptLast2 + 'k';
                            break;
                        default:
                            rootForm = exceptLast;
                    }
                } else {
                    if (word.isPortmanteauEndingWithSI()) {
                        rootForm = exceptLast2;
                    } else {
                        rootForm = exceptLast;
                    }
                }
                surfaceForm = transition.makeTransition(word, rootForm, startState);
                assertTrue(fsm.morphologicalAnalysis(surfaceForm).size() != 0);
            }
        }
    }

    @Test
    public void morphologicalAnalysisNotObeysVowelHarmonyDuringAgglutination() {
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++) {
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isNominal() && word.notObeysVowelHarmonyDuringAgglutination()) {
                State transitionState = new State("Possessive", false, false);
                State startState = new State("NominalRoot", true, false);
                Transition transition = new Transition(transitionState, "yH", "ACC");
                String surfaceForm = transition.makeTransition(word, word.getName(), startState);
                assertTrue(fsm.morphologicalAnalysis(surfaceForm).size() != 0);
            }
        }
    }

    @Test
    public void morphologicalAnalysisLastIdropsDuringSuffixation() {
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++) {
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isNominal() && word.lastIdropsDuringSuffixation()) {
                State transitionState = new State("Possessive", false, false);
                State startState = new State("NominalRoot", true, false);
                Transition transition = new Transition(transitionState, "yH", "ACC");
                String surfaceForm = transition.makeTransition(word, word.getName(), startState);
                assertTrue(fsm.morphologicalAnalysis(surfaceForm).size() != 0);
            }
        }
    }

    @Test
    public void morphologicalAnalysisVerbSoftenDuringSuffixation() {
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++) {
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isVerb() && word.verbSoftenDuringSuffixation()) {
                State transitionState = new State("VerbalStem", false, false);
                State startState = new State("VerbalRoot", true, false);
                Transition transition = new Transition(transitionState, "Hyor", "PROG1");
                String surfaceForm = transition.makeTransition(word, word.getName(), startState);
                assertTrue(fsm.morphologicalAnalysis(surfaceForm).size() != 0);
            }
        }
    }

    @Test
    public void morphologicalAnalysisDuplicatesDuringSuffixation() {
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++) {
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isNominal() && word.duplicatesDuringSuffixation()) {
                State transitionState = new State("Possessive", false, false);
                State startState = new State("NominalRoot", true, false);
                Transition transition = new Transition(transitionState, "yH", "ACC");
                String surfaceForm = transition.makeTransition(word, word.getName(), startState);
                assertTrue(fsm.morphologicalAnalysis(surfaceForm).size() != 0);
            }
        }
    }

    @Test
    public void morphologicalAnalysisEndingKChangesIntoG() {
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++) {
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isNominal() && word.endingKChangesIntoG()) {
                State transitionState = new State("Possessive", false, false);
                State startState = new State("NominalRoot", true, false);
                Transition transition = new Transition(transitionState, "yH", "ACC");
                String surfaceForm = transition.makeTransition(word, word.getName(), startState);
                assertTrue(fsm.morphologicalAnalysis(surfaceForm).size() != 0);
            }
        }
    }

    @Test
    public void morphologicalAnalysisLastIdropsDuringPassiveSuffixation() {
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++) {
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isVerb() && word.lastIdropsDuringPassiveSuffixation()) {
                State transitionState = new State("VerbalStem", false, false);
                State startState = new State("VerbalRoot", true, false);
                Transition transition = new Transition(transitionState, "Hl", "^DB+VERB+PASS");
                String surfaceForm = transition.makeTransition(word, word.getName(), startState);
                assertTrue(fsm.morphologicalAnalysis(surfaceForm).size() != 0);
            }
        }
    }

    @Test
    public void testReplaceWord() {
        assertEquals("Şvesterine söyle kazağı güzelmiş", fsm.replaceWord(new Sentence("Hemşirene söyle kazağı güzelmiş"), "hemşire", "şvester").toString());
        assertEquals("Burada çok abartma var", fsm.replaceWord(new Sentence("Burada çok mübalağa var"), "mübalağa", "abartma").toString());
        assertEquals("Bu bina çok kötü şekilsizleştirildi", fsm.replaceWord(new Sentence("Bu bina çok kötü biçimsizleştirildi"), "biçimsizleş", "şekilsizleş").toString());
        assertEquals("Abim geçen yıl ölmüştü gibi", fsm.replaceWord(new Sentence("Abim geçen yıl son yolculuğa çıkmıştı gibi"), "son yolculuğa çık", "öl").toString());
        assertEquals("Hemşirenle evlendim", fsm.replaceWord(new Sentence("Kız kardeşinle evlendim"), "kız kardeş", "hemşire").toString());
        assertEquals("Dün yaptığı güreş maçında yenildi", fsm.replaceWord(new Sentence("Dün yaptığı güreş maçında mağlup oldu"), "mağlup ol", "yenil").toString());
        assertEquals("Abim geçen yıl son yolculuğa çıkmıştı gibi", fsm.replaceWord(new Sentence("Abim geçen yıl ölmüştü gibi"), "öl", "son yolculuğa çık").toString());
        assertEquals("Kız kardeşinle evlendim", fsm.replaceWord(new Sentence("Hemşirenle evlendim"), "hemşire", "kız kardeş").toString());
        assertEquals("Dün yaptığı güreş maçında mağlup oldu", fsm.replaceWord(new Sentence("Dün yaptığı güreş maçında yenildi"), "yenil", "mağlup ol").toString());
        assertEquals("Dün yaptığı güreş maçında alt oldu sanki", fsm.replaceWord(new Sentence("Dün yaptığı güreş maçında mağlup oldu sanki"), "mağlup ol", "alt ol").toString());
        assertEquals("Yemin billah vermişlerdi vazoyu kırmadığına", fsm.replaceWord(new Sentence("Yemin etmişlerdi vazoyu kırmadığına"), "yemin et", "yemin billah ver").toString());
        assertEquals("Yemin etmişlerdi vazoyu kırmadığına", fsm.replaceWord(new Sentence("Yemin billah vermişlerdi vazoyu kırmadığına"), "yemin billah ver", "yemin et").toString());
    }

    @Test
    public void testGenerateAllParses() throws IOException {
        String[] testWords = {"açıkla", "yıldönümü", "resim", "hal", "cenk", "emlak", "git", "kalp", "kavur", "ye", "yemek", "göç", "ak", "sıska", "yıka", "bul",
                "cevapla", "coş", "böl", "del", "giy", "kaydol", "anla", "çök", "çık", "doldur", "azal", "göster", "aksa"};
        ArrayList<FsmParse> parsesGenerated;
        BufferedReader reader;
        String line;
        for (int i = 0; i < testWords.length; i++) {
            TxtWord word = (TxtWord) fsm.getDictionary().getWord(testWords[i]);
            ArrayList<String> parsesExpected = new ArrayList<>();
            reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("parses/" + word.getName() + ".txt")), StandardCharsets.UTF_8));
            while ((line = reader.readLine()) != null) {
                parsesExpected.add(line.split(" ")[1]);
            }
            reader.close();
            parsesGenerated = fsm.generateAllParses(word, word.getName().length() + 5);
            assertEquals("\"" + word.getName() + ".txt\"", parsesExpected.size(), parsesGenerated.size());
            for (FsmParse parseGenerated : parsesGenerated) {
                assertTrue("\"" + word.getName() + ".txt\"", parsesExpected.contains(parseGenerated.toString()));
            }
        }
    }
}