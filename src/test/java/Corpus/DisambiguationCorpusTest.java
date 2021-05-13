package Corpus;

import org.junit.Test;

import static org.junit.Assert.*;

public class DisambiguationCorpusTest {

    @Test
    public void testCorpus() {
        DisambiguationCorpus corpus = new DisambiguationCorpus("penntreebank.txt");
        assertEquals(19109, corpus.sentenceCount());
        assertEquals(170211, corpus.numberOfWords());
    }

}