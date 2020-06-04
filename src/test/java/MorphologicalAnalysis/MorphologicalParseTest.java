package MorphologicalAnalysis;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MorphologicalParseTest {
    MorphologicalParse parse1, parse2, parse3, parse4, parse5, parse6, parse7;

    @Before
    public void setUp(){
        parse1 = new MorphologicalParse("bayan+NOUN+A3SG+PNON+NOM");
        parse2 = new MorphologicalParse("yaşa+VERB+POS^DB+ADJ+PRESPART");
        parse3 = new MorphologicalParse("serbest+ADJ");
        parse4 = new MorphologicalParse("et+VERB^DB+VERB+PASS^DB+VERB+ABLE+NEG+AOR+A3SG");
        parse5 = new MorphologicalParse("sür+VERB^DB+VERB+CAUS^DB+VERB+PASS+POS^DB+NOUN+INF2+A3SG+P3SG+NOM");
        parse6 = new MorphologicalParse("değiş+VERB^DB+VERB+CAUS^DB+VERB+PASS+POS^DB+VERB+ABLE+AOR^DB+ADJ+ZERO");
        parse7 = new MorphologicalParse("iyi+ADJ^DB+VERB+BECOME^DB+VERB+CAUS^DB+VERB+PASS+POS^DB+VERB+ABLE^DB+NOUN+INF2+A3PL+P3PL+ABL");
    }

    @Test
    public void testGetTransitionList(){
        assertEquals("NOUN+A3SG+PNON+NOM", parse1.getTransitionList());
        assertEquals("VERB+POS+ADJ+PRESPART", parse2.getTransitionList());
        assertEquals("ADJ", parse3.getTransitionList());
        assertEquals("VERB+VERB+PASS+VERB+ABLE+NEG+AOR+A3SG", parse4.getTransitionList());
        assertEquals("VERB+VERB+CAUS+VERB+PASS+POS+NOUN+INF2+A3SG+P3SG+NOM", parse5.getTransitionList());
        assertEquals("VERB+VERB+CAUS+VERB+PASS+POS+VERB+ABLE+AOR+ADJ+ZERO", parse6.getTransitionList());
        assertEquals("ADJ+VERB+BECOME+VERB+CAUS+VERB+PASS+POS+VERB+ABLE+NOUN+INF2+A3PL+P3PL+ABL", parse7.getTransitionList());
    }

    @Test
    public void testGetTag() {
        assertEquals("A3SG", parse1.getTag(2));
        assertEquals("PRESPART", parse2.getTag(4));
        assertEquals("serbest", parse3.getTag(0));
        assertEquals("AOR", parse4.getTag(7));
        assertEquals("P3SG", parse5.getTag(10));
        assertEquals("ABLE", parse6.getTag(8));
        assertEquals("ABL", parse7.getTag(15));
    }

    @Test
    public void testGetTagSize() {
        assertEquals(5, parse1.tagSize());
        assertEquals(5, parse2.tagSize());
        assertEquals(2, parse3.tagSize());
        assertEquals(9, parse4.tagSize());
        assertEquals(12, parse5.tagSize());
        assertEquals(12, parse6.tagSize());
        assertEquals(16, parse7.tagSize());
    }

    @Test
    public void testSize() {
        assertEquals(1, parse1.size());
        assertEquals(2, parse2.size());
        assertEquals(1, parse3.size());
        assertEquals(3, parse4.size());
        assertEquals(4, parse5.size());
        assertEquals(5, parse6.size());
        assertEquals(6, parse7.size());
    }

    @Test
    public void testGetRootPos() {
        assertEquals("NOUN", parse1.getRootPos());
        assertEquals("VERB", parse2.getRootPos());
        assertEquals("ADJ", parse3.getRootPos());
        assertEquals("VERB", parse4.getRootPos());
        assertEquals("VERB", parse5.getRootPos());
        assertEquals("VERB", parse6.getRootPos());
        assertEquals("ADJ", parse7.getRootPos());
    }

    @Test
    public void testGetPos() {
        assertEquals("NOUN", parse1.getPos());
        assertEquals("ADJ", parse2.getPos());
        assertEquals("ADJ", parse3.getPos());
        assertEquals("VERB", parse4.getPos());
        assertEquals("NOUN", parse5.getPos());
        assertEquals("ADJ", parse6.getPos());
        assertEquals("NOUN", parse7.getPos());
    }

    @Test
    public void testGetWordWithPos() {
        assertEquals("bayan+NOUN", parse1.getWordWithPos().getName());
        assertEquals("yaşa+VERB", parse2.getWordWithPos().getName());
        assertEquals("serbest+ADJ", parse3.getWordWithPos().getName());
        assertEquals("et+VERB", parse4.getWordWithPos().getName());
        assertEquals("sür+VERB", parse5.getWordWithPos().getName());
        assertEquals("değiş+VERB", parse6.getWordWithPos().getName());
        assertEquals("iyi+ADJ", parse7.getWordWithPos().getName());
    }

    @Test
    public void testLastIGContainsCase() {
        assertEquals("NOM", parse1.lastIGContainsCase());
        assertEquals("NULL", parse2.lastIGContainsCase());
        assertEquals("NULL", parse3.lastIGContainsCase());
        assertEquals("NULL", parse4.lastIGContainsCase());
        assertEquals("NOM", parse5.lastIGContainsCase());
        assertEquals("NULL", parse6.lastIGContainsCase());
        assertEquals("ABL", parse7.lastIGContainsCase());
    }

    @Test
    public void testLastIGContainsPossessive() {
        assertFalse(parse1.lastIGContainsPossessive());
        assertFalse(parse2.lastIGContainsPossessive());
        assertFalse(parse3.lastIGContainsPossessive());
        assertFalse(parse4.lastIGContainsPossessive());
        assertTrue(parse5.lastIGContainsPossessive());
        assertFalse(parse6.lastIGContainsPossessive());
        assertTrue(parse7.lastIGContainsPossessive());
    }

    @Test
    public void testIsPlural() {
        assertFalse(parse1.isPlural());
        assertFalse(parse2.isPlural());
        assertFalse(parse3.isPlural());
        assertFalse(parse4.isPlural());
        assertFalse(parse5.isPlural());
        assertFalse(parse6.isPlural());
        assertTrue(parse7.isPlural());
    }

    @Test
    public void testIsAuxiliary() {
        assertFalse(parse1.isAuxiliary());
        assertFalse(parse2.isAuxiliary());
        assertFalse(parse3.isAuxiliary());
        assertTrue(parse4.isAuxiliary());
        assertFalse(parse5.isAuxiliary());
        assertFalse(parse6.isAuxiliary());
        assertFalse(parse7.isAuxiliary());
    }

    @Test
    public void testIsNoun() {
        assertTrue(parse1.isNoun());
        assertTrue(parse5.isNoun());
        assertTrue(parse7.isNoun());
    }

    @Test
    public void testIsAdjective() {
        assertTrue(parse2.isAdjective());
        assertTrue(parse3.isAdjective());
        assertTrue(parse6.isAdjective());
    }

    @Test
    public void testIsVerb() {
        assertTrue(parse4.isVerb());
    }

    @Test
    public void testIsRootVerb() {
        assertTrue(parse2.isRootVerb());
        assertTrue(parse4.isRootVerb());
        assertTrue(parse5.isRootVerb());
        assertTrue(parse6.isRootVerb());
    }

}