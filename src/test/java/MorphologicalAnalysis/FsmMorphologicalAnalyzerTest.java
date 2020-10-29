package MorphologicalAnalysis;

import Dictionary.TurkishWordComparator;
import Dictionary.TxtDictionary;
import Dictionary.TxtWord;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import static org.junit.Assert.*;

public class FsmMorphologicalAnalyzerTest {
    FsmMorphologicalAnalyzer fsm;

    @Before
    public void setUp() {
        fsm = new FsmMorphologicalAnalyzer();
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
        for (int i = 0; i < dictionary.size(); i++){
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isProperNoun()){
                assertTrue(fsm.morphologicalAnalysis(word.getName().toUpperCase(new Locale("tr"))).size() != 0);
            }
        }
    }

    @Test
    public void morphologicalAnalysisNounSoftenDuringSuffixation() {
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++){
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isNominal() && word.nounSoftenDuringSuffixation()){
                State transitionState = new State("Possessive", false, false);
                State startState = new State("NominalRoot", true, false);
                Transition transition = new Transition(transitionState,"yH", "ACC");
                String surfaceForm = transition.makeTransition(word, word.getName(), startState);
                assertTrue(fsm.morphologicalAnalysis(surfaceForm).size() != 0);
            }
        }
    }

    @Test
    public void morphologicalAnalysisVowelAChangesToIDuringYSuffixation(){
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++){
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isVerb() && word.vowelAChangesToIDuringYSuffixation()){
                State transitionState = new State("VerbalStem", false, false);
                State startState = new State("VerbalRoot", true, false);
                Transition transition = new Transition(transitionState,"Hyor", "PROG1");
                String surfaceForm = transition.makeTransition(word, word.getName(), startState);
                assertTrue(fsm.morphologicalAnalysis(surfaceForm).size() != 0);
            }
        }
    }

    @Test
    public void morphologicalAnalysisIsPortmanteau(){
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++){
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isNominal() && word.isPortmanteau() && !word.isPlural() && !word.isPortmanteauFacedVowelEllipsis()){
                State transitionState = new State("CompoundNounRoot", true, false);
                State startState = new State("CompoundNounRoot", true, false);
                Transition transition = new Transition(transitionState,"lArH", "A3PL+P3PL");
                String rootForm, surfaceForm, exceptLast2, exceptLast;
                exceptLast2 = word.getName().substring(0, word.getName().length() - 2);
                exceptLast = word.getName().substring(0, word.getName().length() - 1);
                if (word.isPortmanteauFacedSoftening()){
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
                    if (word.isPortmanteauEndingWithSI()){
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
        for (int i = 0; i < dictionary.size(); i++){
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isNominal() && word.notObeysVowelHarmonyDuringAgglutination()){
                State transitionState = new State("Possessive", false, false);
                State startState = new State("NominalRoot", true, false);
                Transition transition = new Transition(transitionState,"yH", "ACC");
                String surfaceForm = transition.makeTransition(word, word.getName(), startState);
                assertTrue(fsm.morphologicalAnalysis(surfaceForm).size() != 0);
            }
        }
    }

    @Test
    public void morphologicalAnalysisLastIdropsDuringSuffixation() {
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++){
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isNominal() && word.lastIdropsDuringSuffixation()){
                State transitionState = new State("Possessive", false, false);
                State startState = new State("NominalRoot", true, false);
                Transition transition = new Transition(transitionState,"yH", "ACC");
                String surfaceForm = transition.makeTransition(word, word.getName(), startState);
                assertTrue(fsm.morphologicalAnalysis(surfaceForm).size() != 0);
            }
        }
    }

    @Test
    public void morphologicalAnalysisVerbSoftenDuringSuffixation(){
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++){
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isVerb() && word.verbSoftenDuringSuffixation()){
                State transitionState = new State("VerbalStem", false, false);
                State startState = new State("VerbalRoot", true, false);
                Transition transition = new Transition(transitionState,"Hyor", "PROG1");
                String surfaceForm = transition.makeTransition(word, word.getName(), startState);
                assertTrue(fsm.morphologicalAnalysis(surfaceForm).size() != 0);
            }
        }
    }

    @Test
    public void morphologicalAnalysisDuplicatesDuringSuffixation() {
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++){
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isNominal() && word.duplicatesDuringSuffixation()){
                State transitionState = new State("Possessive", false, false);
                State startState = new State("NominalRoot", true, false);
                Transition transition = new Transition(transitionState,"yH", "ACC");
                String surfaceForm = transition.makeTransition(word, word.getName(), startState);
                assertTrue(fsm.morphologicalAnalysis(surfaceForm).size() != 0);
            }
        }
    }

    @Test
    public void morphologicalAnalysisEndingKChangesIntoG() {
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++){
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isNominal() && word.endingKChangesIntoG()){
                State transitionState = new State("Possessive", false, false);
                State startState = new State("NominalRoot", true, false);
                Transition transition = new Transition(transitionState,"yH", "ACC");
                String surfaceForm = transition.makeTransition(word, word.getName(), startState);
                assertTrue(fsm.morphologicalAnalysis(surfaceForm).size() != 0);
            }
        }
    }

    @Test
    public void morphologicalAnalysisLastIdropsDuringPassiveSuffixation(){
        TxtDictionary dictionary = fsm.getDictionary();
        for (int i = 0; i < dictionary.size(); i++){
            TxtWord word = (TxtWord) dictionary.getWord(i);
            if (word.isVerb() && word.lastIdropsDuringPassiveSuffixation()){
                State transitionState = new State("VerbalStem", false, false);
                State startState = new State("VerbalRoot", true, false);
                Transition transition = new Transition(transitionState,"Hl", "^DB+VERB+PASS");
                String surfaceForm = transition.makeTransition(word, word.getName(), startState);
                assertTrue(fsm.morphologicalAnalysis(surfaceForm).size() != 0);
            }
        }
    }

}