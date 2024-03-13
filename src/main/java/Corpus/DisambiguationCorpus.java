package Corpus;

import DataStructure.CounterHashMap;
import MorphologicalAnalysis.MorphologicalParse;

import java.io.*;
import java.util.ArrayList;

public class DisambiguationCorpus extends Corpus {

    /**
     * Constructor which creates an {@link ArrayList} of sentences and a {@link CounterHashMap} of wordList.
     */
    public DisambiguationCorpus() {
        super();
    }

    /**
     * Constructor which creates a new empty copy of the {@link DisambiguationCorpus}.
     *
     * @return An empty copy of the {@link DisambiguationCorpus}.
     */
    public DisambiguationCorpus emptyCopy() {
        return new DisambiguationCorpus();
    }

    /**
     * Constructor which takes a file name {@link String} as an input and reads the file line by line. It takes each word of the line,
     * and creates a new {@link DisambiguatedWord} with current word and its {@link MorphologicalParse}. It also creates a new {@link Sentence}
     * when a new sentence starts, and adds each word to this sentence till the end of that sentence.
     *
     * @param fileName File which will be read and parsed.
     */
    public DisambiguationCorpus(String fileName) {
        super();
        int i = 1;
        String line, word, parse;
        DisambiguatedWord newWord;
        Sentence newSentence = null;
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            while (line != null) {
                word = line.substring(0, line.indexOf("\t"));
                parse = line.substring(line.indexOf("\t") + 1);
                if (!word.isEmpty() && !parse.isEmpty()) {
                    newWord = new DisambiguatedWord(word, new MorphologicalParse(parse));
                    if (word.equals("<S>")) {
                        newSentence = new Sentence();
                    } else {
                        if (word.equals("</S>")) {
                            addSentence(newSentence);
                        } else {
                            if (!word.equals("<DOC>") && !word.equals("</DOC>") && !word.equals("<TITLE>") && !word.equals("</TITLE>")) {
                                if (newSentence != null) {
                                    newSentence.addWord(newWord);
                                } else {
                                    System.out.println("Word " + word + " out of place\n");
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Not enough items in " + line + " " + i + "\n");
                }
                i++;
                line = br.readLine();
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * The writeToFile method takes a {@link String} file name as an input and writes the elements of sentences {@link ArrayList}
     * to this file with proper tags which indicates the beginnings and endings of the document and sentence.
     *
     * @param fileName File which will be filled with the sentences.
     */
    public void writeToFile(String fileName) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(fileName, "UTF-8");
            writer.println("<DOC>\t<DOC>+BDTag");
            for (Sentence sentence : sentences) {
                writer.println("<S>\t<S>+BSTag");
                for (int i = 0; i < sentence.wordCount(); i++) {
                    DisambiguatedWord word = (DisambiguatedWord) sentence.getWord(i);
                    writer.println(word.getName() + "\t" + word.getParse());
                }
                writer.println("</S>\t</S>+ESTag");
            }
            writer.println("</DOC>\t</DOC>+EDTag");
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ignored) {
        }
    }

    /**
     * The writeToFile method which takes a file name {@link String} and a {@link WordFormat} as input simply implies the
     * output format of the words to be written to the file with given name. Word format can be surface, letter2, letter3, and letter4.
     * Surface form leaves the output as it is, and letterK divides the output as the substrings of length of K.
     *
     * @param fileName File which will be filled with the sentences.
     * @param format   Output format of the words to be written to the file.
     */
    public void writeToFile(String fileName, WordFormat format) {
        if (format.equals(WordFormat.SURFACE) || format.equals(WordFormat.LETTER_2) || format.equals(WordFormat.LETTER_3) || format.equals(WordFormat.LETTER_4)) {
            super.writeToFile(fileName, format);
        }
    }

}
