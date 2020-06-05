package MorphologicalAnalysis;

import Corpus.Corpus;
import Dictionary.Word;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FsmParseListTest {
    FsmParseList parse1, parse2, parse3, parse4, parse5, parse6, parse7, parse8, parse9;

    @Before
    public void setUp() {
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer();
        parse1 = fsm.morphologicalAnalysis("açılır");
        parse2 = fsm.morphologicalAnalysis("koparılarak");
        parse3 = fsm.morphologicalAnalysis("toplama");
        parse4 = fsm.morphologicalAnalysis("değerlendirmede");
        parse5 = fsm.morphologicalAnalysis("soruşturmasının");
        parse6 = fsm.morphologicalAnalysis("karşılaştırmalı");
        parse7 = fsm.morphologicalAnalysis("esaslarını");
        parse8 = fsm.morphologicalAnalysis("güçleriyle");
        parse9 = fsm.morphologicalAnalysis("bulmayacakları");
    }

    @Test
    public void testSize() {
        assertEquals(2, parse1.size());
        assertEquals(2, parse2.size());
        assertEquals(6, parse3.size());
        assertEquals(4, parse4.size());
        assertEquals(5, parse5.size());
        assertEquals(12, parse6.size());
        assertEquals(8, parse7.size());
        assertEquals(6, parse8.size());
        assertEquals(5, parse9.size());
    }

    @Test
    public void testRootWords() {
        assertEquals("aç", parse1.rootWords());
        assertEquals("kop$kopar", parse2.rootWords());
        assertEquals("topla$toplam$toplama", parse3.rootWords());
        assertEquals("değer$değerlen$değerlendir$değerlendirme", parse4.rootWords());
        assertEquals("sor$soru$soruş$soruştur$soruşturma", parse5.rootWords());
        assertEquals("karşı$karşıla$karşılaş$karşılaştır$karşılaştırma$karşılaştırmalı", parse6.rootWords());
        assertEquals("esas", parse7.rootWords());
        assertEquals("güç", parse8.rootWords());
        assertEquals("bul", parse9.rootWords());
    }

    @Test
    public void testGetParseWithLongestRootWord() {
        assertEquals(new Word("kopar"), parse2.getParseWithLongestRootWord().root);
        assertEquals(new Word("toplama"), parse3.getParseWithLongestRootWord().root);
        assertEquals(new Word("değerlendirme"), parse4.getParseWithLongestRootWord().root);
        assertEquals(new Word("soruşturma"), parse5.getParseWithLongestRootWord().root);
        assertEquals(new Word("karşılaştırmalı"), parse6.getParseWithLongestRootWord().root);
    }

    @Test
    public void testReduceToParsesWithSameRootAndPos() {
        parse2.reduceToParsesWithSameRootAndPos(new Word("kop+VERB"));
        assertEquals(1, parse2.size());
        parse3.reduceToParsesWithSameRootAndPos(new Word("topla+VERB"));
        assertEquals(2, parse3.size());
        parse6.reduceToParsesWithSameRootAndPos(new Word("karşıla+VERB"));
        assertEquals(2, parse6.size());
    }

    @Test
    public void testReduceToParsesWithSameRoot() {
        parse2.reduceToParsesWithSameRoot("kop");
        assertEquals(1, parse2.size());
        parse3.reduceToParsesWithSameRoot("topla");
        assertEquals(3, parse3.size());
        parse6.reduceToParsesWithSameRoot("karşı");
        assertEquals(4, parse6.size());
        parse7.reduceToParsesWithSameRoot("esas");
        assertEquals(8, parse7.size());
        parse8.reduceToParsesWithSameRoot("güç");
        assertEquals(6, parse8.size());
    }

    @Test
    public void testConstructParseListForDifferentRootWithPos(){
        assertEquals(1, parse1.constructParseListForDifferentRootWithPos().size());
        assertEquals(2, parse2.constructParseListForDifferentRootWithPos().size());
        assertEquals(5, parse3.constructParseListForDifferentRootWithPos().size());
        assertEquals(4, parse4.constructParseListForDifferentRootWithPos().size());
        assertEquals(5, parse5.constructParseListForDifferentRootWithPos().size());
        assertEquals(7, parse6.constructParseListForDifferentRootWithPos().size());
        assertEquals(2, parse7.constructParseListForDifferentRootWithPos().size());
        assertEquals(2, parse8.constructParseListForDifferentRootWithPos().size());
        assertEquals(1, parse9.constructParseListForDifferentRootWithPos().size());
    }
}