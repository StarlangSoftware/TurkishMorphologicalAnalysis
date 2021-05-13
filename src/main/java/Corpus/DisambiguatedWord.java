package Corpus;

import Dictionary.Word;
import MorphologicalAnalysis.MorphologicalParse;

public class DisambiguatedWord extends Word {
    private MorphologicalParse parse;

    /**
     * The constructor of {@link DisambiguatedWord} class which takes a {@link String} and a {@link MorphologicalParse}
     * as inputs. It creates a new {@link MorphologicalParse} with given MorphologicalParse. It generates a new instance with
     * given {@link String}.
     *
     * @param name  Instances that will be a DisambiguatedWord.
     * @param parse {@link MorphologicalParse} of the {@link DisambiguatedWord}.
     */
    public DisambiguatedWord(String name, MorphologicalParse parse) {
        super(name);
        this.parse = parse;
    }

    /**
     * Accessor for the {@link MorphologicalParse}.
     *
     * @return MorphologicalParse.
     */
    public MorphologicalParse getParse() {
        return parse;
    }

}
