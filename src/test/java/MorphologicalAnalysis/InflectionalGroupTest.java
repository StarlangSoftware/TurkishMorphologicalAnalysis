package MorphologicalAnalysis;

import org.junit.Test;

import static org.junit.Assert.*;

public class InflectionalGroupTest {

    @Test
    public void testGetMorphologicalTag() {
        assertEquals(InflectionalGroup.getMorphologicalTag("noun"), MorphologicalTag.NOUN);
        assertEquals(InflectionalGroup.getMorphologicalTag("without"), MorphologicalTag.WITHOUT);
        assertEquals(InflectionalGroup.getMorphologicalTag("interj"), MorphologicalTag.INTERJECTION);
        assertEquals(InflectionalGroup.getMorphologicalTag("inf2"), MorphologicalTag.INFINITIVE2);
    }

    @Test
    public void size() {
        InflectionalGroup inflectionalGroup1 = new InflectionalGroup("ADJ");
        assertEquals(1, inflectionalGroup1.size());
        InflectionalGroup inflectionalGroup2 = new InflectionalGroup("ADJ+JUSTLIKE");
        assertEquals(2, inflectionalGroup2.size());
        InflectionalGroup inflectionalGroup3 = new InflectionalGroup("ADJ+FUTPART+P1PL");
        assertEquals(3, inflectionalGroup3.size());
        InflectionalGroup inflectionalGroup4 = new InflectionalGroup("NOUN+A3PL+P1PL+ABL");
        assertEquals(4, inflectionalGroup4.size());
        InflectionalGroup inflectionalGroup5 = new InflectionalGroup("ADJ+WITH+A3SG+P3SG+ABL");
        assertEquals(5, inflectionalGroup5.size());
        InflectionalGroup inflectionalGroup6 = new InflectionalGroup("VERB+ABLE+NEG+FUT+A3PL+COP");
        assertEquals(6, inflectionalGroup6.size());
        InflectionalGroup inflectionalGroup7 = new InflectionalGroup("VERB+ABLE+NEG+AOR+A3SG+COND+A1SG");
        assertEquals(7, inflectionalGroup7.size());
    }

    @Test
    public void containsCase() {
        InflectionalGroup inflectionalGroup1 = new InflectionalGroup("NOUN+ACTOF+A3PL+P1PL+NOM");
        assertNotNull(inflectionalGroup1.containsCase());
        InflectionalGroup inflectionalGroup2 = new InflectionalGroup("NOUN+A3PL+P1PL+ACC");
        assertNotNull(inflectionalGroup2.containsCase());
        InflectionalGroup inflectionalGroup3 = new InflectionalGroup("NOUN+ZERO+A3SG+P3PL+DAT");
        assertNotNull(inflectionalGroup3.containsCase());
        InflectionalGroup inflectionalGroup4 = new InflectionalGroup("PRON+QUANTP+A1PL+P1PL+LOC");
        assertNotNull(inflectionalGroup4.containsCase());
        InflectionalGroup inflectionalGroup5 = new InflectionalGroup("NOUN+AGT+A3SG+P2SG+ABL");
        assertNotNull(inflectionalGroup5.containsCase());
    }

    @Test
    public void containsPlural() {
        InflectionalGroup inflectionalGroup1 = new InflectionalGroup("VERB+NEG+NECES+A1PL");
        assertTrue(inflectionalGroup1.containsPlural());
        InflectionalGroup inflectionalGroup2 = new InflectionalGroup("PRON+PERS+A2PL+PNON+NOM");
        assertTrue(inflectionalGroup2.containsPlural());
        InflectionalGroup inflectionalGroup3 = new InflectionalGroup("NOUN+DIM+A3PL+P2SG+GEN");
        assertTrue(inflectionalGroup3.containsPlural());
        InflectionalGroup inflectionalGroup4 = new InflectionalGroup("NOUN+A3PL+P1PL+GEN");
        assertTrue(inflectionalGroup4.containsPlural());
        InflectionalGroup inflectionalGroup5 = new InflectionalGroup("NOUN+ZERO+A3SG+P2PL+INS");
        assertTrue(inflectionalGroup5.containsPlural());
        InflectionalGroup inflectionalGroup6 = new InflectionalGroup("PRON+QUANTP+A3PL+P3PL+LOC");
        assertTrue(inflectionalGroup6.containsPlural());
    }

    @Test
    public void containsTag() {
        InflectionalGroup inflectionalGroup1 = new InflectionalGroup("NOUN+ZERO+A3SG+P1SG+NOM");
        assertTrue(inflectionalGroup1.containsTag(MorphologicalTag.NOUN));
        InflectionalGroup inflectionalGroup2 = new InflectionalGroup("NOUN+AGT+A3PL+P2SG+ABL");
        assertTrue(inflectionalGroup2.containsTag(MorphologicalTag.AGENT));
        InflectionalGroup inflectionalGroup3 = new InflectionalGroup("NOUN+INF2+A3PL+P3SG+NOM");
        assertTrue(inflectionalGroup3.containsTag(MorphologicalTag.NOMINATIVE));
        InflectionalGroup inflectionalGroup4 = new InflectionalGroup("NOUN+ZERO+A3SG+P1PL+ACC");
        assertTrue(inflectionalGroup4.containsTag(MorphologicalTag.ZERO));
        InflectionalGroup inflectionalGroup5 = new InflectionalGroup("NOUN+ZERO+A3SG+P2PL+INS");
        assertTrue(inflectionalGroup5.containsTag(MorphologicalTag.P2PL));
        InflectionalGroup inflectionalGroup6 = new InflectionalGroup("PRON+QUANTP+A3PL+P3PL+LOC");
        assertTrue(inflectionalGroup6.containsTag(MorphologicalTag.QUANTITATIVEPRONOUN));
    }

    @Test
    public void containsPossessive() {
        InflectionalGroup inflectionalGroup1 = new InflectionalGroup("NOUN+ZERO+A3SG+P1SG+NOM");
        assertTrue(inflectionalGroup1.containsPossessive());
        InflectionalGroup inflectionalGroup2 = new InflectionalGroup("NOUN+AGT+A3PL+P2SG+ABL");
        assertTrue(inflectionalGroup2.containsPossessive());
        InflectionalGroup inflectionalGroup3 = new InflectionalGroup("NOUN+INF2+A3PL+P3SG+NOM");
        assertTrue(inflectionalGroup3.containsPossessive());
        InflectionalGroup inflectionalGroup4 = new InflectionalGroup("NOUN+ZERO+A3SG+P1PL+ACC");
        assertTrue(inflectionalGroup4.containsPossessive());
        InflectionalGroup inflectionalGroup5 = new InflectionalGroup("NOUN+ZERO+A3SG+P2PL+INS");
        assertTrue(inflectionalGroup5.containsPossessive());
        InflectionalGroup inflectionalGroup6 = new InflectionalGroup("PRON+QUANTP+A3PL+P3PL+LOC");
        assertTrue(inflectionalGroup6.containsPossessive());
    }
}