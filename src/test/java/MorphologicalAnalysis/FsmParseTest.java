package MorphologicalAnalysis;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FsmParseTest {
    FsmParse parse1, parse2, parse3, parse4, parse5, parse6, parse7, parse8, parse9;

    @Before
    public void setUp() {
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer();
        parse1 = fsm.morphologicalAnalysis("açılır").getFsmParse(0);
        parse2 = fsm.morphologicalAnalysis("koparılarak").getFsmParse(0);
        parse3 = fsm.morphologicalAnalysis("toplama").getFsmParse(0);
        parse4 = fsm.morphologicalAnalysis("değerlendirmede").getFsmParse(0);
        parse5 = fsm.morphologicalAnalysis("soruşturmasının").getFsmParse(0);
        parse6 = fsm.morphologicalAnalysis("karşılaştırmalı").getFsmParse(0);
        parse7 = fsm.morphologicalAnalysis("esaslarını").getFsmParse(0);
        parse8 = fsm.morphologicalAnalysis("güçleriyle").getFsmParse(0);
        parse9 = fsm.morphologicalAnalysis("bulmayacakları").getFsmParse(0);
    }

    @Test
    public void testGetLastLemmaWithTag() {
        assertEquals("açıl", parse1.getLastLemmaWithTag("VERB"));
        assertEquals("koparıl", parse2.getLastLemmaWithTag("VERB"));
        assertEquals("değerlendir", parse4.getLastLemmaWithTag("VERB"));
        assertEquals("soruştur", parse5.getLastLemmaWithTag("VERB"));
        assertEquals("karşı", parse6.getLastLemmaWithTag("ADJ"));
    }

    @Test
    public void testGetLastLemma() {
        assertEquals("açıl", parse1.getLastLemma());
        assertEquals("koparılarak", parse2.getLastLemma());
        assertEquals("değerlendirme", parse4.getLastLemma());
        assertEquals("soruşturma", parse5.getLastLemma());
        assertEquals("karşılaştır", parse6.getLastLemma());
    }

    @Test
    public void testGetTransitionList() {
        assertEquals("aç+VERB^DB+VERB+PASS+POS+AOR+A3SG", parse1.toString());
        assertEquals("kop+VERB^DB+VERB+CAUS^DB+VERB+PASS+POS^DB+ADV+BYDOINGSO", parse2.toString());
        assertEquals("topla+NOUN+A3SG+P1SG+DAT", parse3.toString());
        assertEquals("değer+NOUN+A3SG+PNON+NOM^DB+VERB+ACQUIRE^DB+VERB+CAUS+POS^DB+NOUN+INF2+A3SG+PNON+LOC", parse4.toString());
        assertEquals("sor+VERB+RECIP^DB+VERB+CAUS+POS^DB+NOUN+INF2+A3SG+P3SG+GEN", parse5.toString());
        assertEquals("karşı+ADJ^DB+VERB+BECOME^DB+VERB+CAUS+POS+NECES+A3SG", parse6.toString());
        assertEquals("esas+ADJ^DB+NOUN+ZERO+A3PL+P2SG+ACC", parse7.toString());
        assertEquals("güç+ADJ^DB+NOUN+ZERO+A3PL+P3PL+INS", parse8.toString());
        assertEquals("bul+VERB+NEG^DB+ADJ+FUTPART+P3PL", parse9.toString());
    }

    @Test
    public void testWithList() {
        assertEquals("aç+Hl+Hr", parse1.withList());
        assertEquals("kop+Ar+Hl+yArAk", parse2.withList());
        assertEquals("topla+Hm+yA", parse3.withList());
        assertEquals("değer+lAn+DHr+mA+DA", parse4.withList());
        assertEquals("sor+Hs+DHr+mA+sH+nHn", parse5.withList());
        assertEquals("karşı+lAs+DHr+mAlH", parse6.withList());
        assertEquals("esas+lAr+Hn+yH", parse7.withList());
        assertEquals("güç+lArH+ylA", parse8.withList());
        assertEquals("bul+mA+yAcAk+lArH", parse9.withList());
    }

    @Test
    public void testSuffixList() {
        assertEquals("VerbalRoot(F5PR)(aç)+PassiveHl(açıl)+OtherTense2(açılır)", parse1.suffixList());
        assertEquals("VerbalRoot(F1P1)(kop)+CausativeAr(kopar)+PassiveHl(koparıl)+Adverb1(koparılarak)", parse2.suffixList());
        assertEquals("NominalRoot(topla)+Possessive(toplam)+Case1(toplama)", parse3.suffixList());
        assertEquals("NominalRoot(değer)+VerbalRoot(F5PR)(değerlen)+CausativeDHr(değerlendir)+NominalRoot(değerlendirme)+Case1(değerlendirmede)", parse4.suffixList());
        assertEquals("VerbalRoot(F5PR)(sor)+Reciprocal(soruş)+CausativeDHr(soruştur)+NominalRoot(soruşturma)+Possessive3(soruşturması)+Case1(soruşturmasının)", parse5.suffixList());
        assertEquals("AdjectiveRoot(karşı)+VerbalRoot(F5PR)(karşılaş)+CausativeDHr(karşılaştır)+OtherTense(karşılaştırmalı)", parse6.suffixList());
        assertEquals("AdjectiveRoot(esas)+Plural(esaslar)+Possessive(esasların)+AccusativeNoun(esaslarını)", parse7.suffixList());
        assertEquals("AdjectiveRoot(güç)+Possesive3(güçleri)+Case1(güçleriyle)", parse8.suffixList());
        assertEquals("VerbalRoot(F5PW)(bul)+Negativema(bulma)+AdjectiveParticiple(bulmayacak)+Adjective(bulmayacakları)", parse9.suffixList());
    }

}