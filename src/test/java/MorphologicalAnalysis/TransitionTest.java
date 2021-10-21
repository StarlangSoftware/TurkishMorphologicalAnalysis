package MorphologicalAnalysis;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransitionTest {
    FsmMorphologicalAnalyzer fsm;

    @Before
    public void setUp() {
        fsm = new FsmMorphologicalAnalyzer();
    }

    @Test
    public void testNumberWithAccusative() {
        assertTrue(fsm.morphologicalAnalysis("2'yi").size() != 0);
        assertEquals(0, fsm.morphologicalAnalysis("2'i").size());
        assertTrue(fsm.morphologicalAnalysis("5'i").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("9'u").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("10'u").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("30'u").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("3'ü").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("4'ü").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("100'ü").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("6'yı").size() != 0);
        assertEquals(0, fsm.morphologicalAnalysis("6'ı").size());
        assertTrue(fsm.morphologicalAnalysis("40'ı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("60'ı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("90'ı").size() != 0);
    }

    @Test
    public void testNumberWithDative() {
        assertTrue(fsm.morphologicalAnalysis("6'ya").size() != 0);
        assertEquals(0, fsm.morphologicalAnalysis("6'a").size());
        assertTrue(fsm.morphologicalAnalysis("9'a").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("10'a").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("30'a").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("40'a").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("60'a").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("90'a").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("2'ye").size() != 0);
        assertEquals(0, fsm.morphologicalAnalysis("2'e").size());
        assertTrue(fsm.morphologicalAnalysis("8'e").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("5'e").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("4'e").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("1'e").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("3'e").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("7'ye").size() != 0);
        assertEquals(0, fsm.morphologicalAnalysis("7'e").size());
    }

    @Test
    public void testPresentTense() {
        assertTrue(fsm.morphologicalAnalysis("büyülüyor").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("bölümlüyor").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("buğuluyor").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("bulguluyor").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("açıklıyor").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("çalkalıyor").size() != 0);
    }

    @Test
    public void testA() {
        assertTrue(fsm.morphologicalAnalysis("alkole").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("anormale").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("sakala").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("kabala").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("faika").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("halika").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("kediye").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("eve").size() != 0);
    }

    @Test
    public void testC() {
        assertTrue(fsm.morphologicalAnalysis("gripçi").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("güllaççı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("gülütçü").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("gülükçü").size() != 0);
    }

    @Test
    public void testSH() {
        assertTrue(fsm.morphologicalAnalysis("altışar").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("yedişer").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("üçer").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("beşer").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("dörder").size() != 0);
    }

    @Test
    public void testNumberWithD() {
        assertTrue(fsm.morphologicalAnalysis("1'di").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("2'ydi").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("3'tü").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("4'tü").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("5'ti").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("6'ydı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("7'ydi").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("8'di").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("9'du").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("30'du").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("40'tı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("60'tı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("70'ti").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("50'ydi").size() != 0);
    }

    @Test
    public void testD() {
        assertTrue(fsm.morphologicalAnalysis("koştu").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("kitaptı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("kaçtı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("evdi").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("fraktı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("sattı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("aftı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("kesti").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("ahtı").size() != 0);
    }

    @Test
    public void testExceptions() {
        assertTrue(fsm.morphologicalAnalysis("yiyip").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("sana").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("bununla").size() != 0);
        assertEquals(0, fsm.morphologicalAnalysis("buyla").size());
        assertTrue(fsm.morphologicalAnalysis("onunla").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("şununla").size() != 0);
        assertEquals(0, fsm.morphologicalAnalysis("şuyla").size());
        assertTrue(fsm.morphologicalAnalysis("bana").size() != 0);
    }

    @Test
    public void testVowelEChangesToIDuringYSuffixation() {
        assertTrue(fsm.morphologicalAnalysis("diyor").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("yiyor").size() != 0);
    }

    @Test
    public void testLastIdropsDuringPassiveSuffixation() {
        assertTrue(fsm.morphologicalAnalysis("yoğruldu").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("buyruldu").size() != 0);
    }

    @Test
    public void testShowsSuRegularities() {
        assertTrue(fsm.morphologicalAnalysis("karasuyu").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("suyu").size() != 0);
    }

    @Test
    public void testDuplicatesDuringSuffixation() {
        assertTrue(fsm.morphologicalAnalysis("tıbbı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("ceddi").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("zıddı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("serhaddi").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("fenni").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("haddi").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("hazzı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("şakkı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("şakı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("halli").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("hali").size() != 0);
    }

    @Test
    public void testLastIdropsDuringSuffixation() {
        assertTrue(fsm.morphologicalAnalysis("hizbi").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("kaybı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("ahdi").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("nesci").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("zehri").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("zikri").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("metni").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("metini").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("katli").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("katili").size() != 0);
    }

    @Test
    public void testNounSoftenDuringSuffixation() {
        assertTrue(fsm.morphologicalAnalysis("adabı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("amibi").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("armudu").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("ağacı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("akacı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("arkeoloğu").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("filoloğu").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("ahengi").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("küngü").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("kitaplığı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("küllüğü").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("adedi").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("adeti").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("ağıdı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("ağıtı").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("anotu").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("anodu").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("Kuzguncuk'u").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("Leylak'ı").size() != 0);
    }

    @Test
    public void testVerbSoftenDuringSuffixation() {
        assertTrue(fsm.morphologicalAnalysis("cezbediyor").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("ediyor").size() != 0);
        assertTrue(fsm.morphologicalAnalysis("bahsediyor").size() != 0);
    }

}