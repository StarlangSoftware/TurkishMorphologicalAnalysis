package MorphologicalAnalysis;

import Dictionary.Word;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class MetamorphicParse implements Serializable{

    public static final String[] metaMorphemes = {"Ar", "Ar", "CA", "CA",
            "CA", "cAsHnA", "CH", "CHk",
            "DA", "DAn", "DH", "DHk",
            "DHkCA", "DHr", "DHr", "DHr",
            "H", "Hl", "Hm", "Hn",
            "Hn", "Hn", "HmHz", "HncH",
            "HnHz", "Hr", "Hr", "Hs",
            "Ht", "Hyor", "Hz", "k",
            "ki", "kü", "lAn", "lAr",
            "lArDHr", "lArH", "lArH'", "lAs",
            "lH", "lHk", "lHm", "m",
            "mA", "mA", "mAcA", "mAdAn",
            "mAk", "mAksHzHn", "mAktA", "mAlH",
            "mAzlHk", "mHs", "n", "n",
            "nA", "ncA", "nDA", "nDAn",
            "nH", "nHn", "nHz", "nlAr",
            "SA", "SAl", "sH", "SH",
            "SH", "SHn", "SHnHz", "SHnlAr",
            "SHz", "ŞAr", "t", "yA",
            "yA", "yAbil", "yAcAk", "yAcAk",
            "yAdur", "yAgel", "yAlH", "yAmA",
            "yAmAdAn", "yAn", "yArAk", "yAsH",
            "yDH", "yH", "yHcH", "yHm",
            "yHn", "yHncA", "yHp", "yHs",
            "yHver", "yHz", "yken", "ylA",
            "ymHs", "ysA", "z", "zsHn",
            "zsHnHz", "zlAr", "yAkal", "yAkoy",
            "yAgor"
    };
    public static final MorphologicalTag[] morphotacticTags = {MorphologicalTag.AORIST, MorphologicalTag.CAUSATIVE, MorphologicalTag.ASIF, MorphologicalTag.LY,
            MorphologicalTag.EQUATIVE, MorphologicalTag.ASIF, MorphologicalTag.AGENT, MorphologicalTag.DIMENSION,
            MorphologicalTag.LOCATIVE, MorphologicalTag.ABLATIVE, MorphologicalTag.PASTTENSE, MorphologicalTag.PASTPARTICIPLE,
            MorphologicalTag.ASLONGAS, MorphologicalTag.COPULA, MorphologicalTag.SINCE, MorphologicalTag.CAUSATIVE,
            MorphologicalTag.P3SG, MorphologicalTag.PASSIVE, MorphologicalTag.P1SG, MorphologicalTag.REFLEXIVE,
            MorphologicalTag.PASSIVE, MorphologicalTag.P2SG, MorphologicalTag.P1PL, MorphologicalTag.ORDINAL,
            MorphologicalTag.P2PL, MorphologicalTag.AORIST, MorphologicalTag.CAUSATIVE, MorphologicalTag.RECIPROCAL,
            MorphologicalTag.CAUSATIVE, MorphologicalTag.PROGRESSIVE1, MorphologicalTag.A1PL, MorphologicalTag.A1PL,
            MorphologicalTag.RELATIVE, MorphologicalTag.RELATIVE, MorphologicalTag.ACQUIRE, MorphologicalTag.A3PL,
            MorphologicalTag.SINCE, MorphologicalTag.P3PL, MorphologicalTag.P3PL, MorphologicalTag.BECOME,
            MorphologicalTag.WITH, MorphologicalTag.NESS, MorphologicalTag.A1PL, MorphologicalTag.A1SG,
            MorphologicalTag.INFINITIVE2, MorphologicalTag.NEGATIVE, MorphologicalTag.ACTOF, MorphologicalTag.WITHOUTHAVINGDONESO,
            MorphologicalTag.INFINITIVE, MorphologicalTag.WITHOUTHAVINGDONESO, MorphologicalTag.PROGRESSIVE2, MorphologicalTag.NECESSITY,
            MorphologicalTag.NOTABLESTATE, MorphologicalTag.NARRATIVE, MorphologicalTag.A2SG, MorphologicalTag.PASSIVE,
            MorphologicalTag.DATIVE, MorphologicalTag.EQUATIVE, MorphologicalTag.LOCATIVE, MorphologicalTag.ABLATIVE,
            MorphologicalTag.ACCUSATIVE, MorphologicalTag.GENITIVE, MorphologicalTag.A2PL, MorphologicalTag.A3PL,
            MorphologicalTag.DESIRE, MorphologicalTag.RELATED, MorphologicalTag.P3SG, MorphologicalTag.JUSTLIKE,
            MorphologicalTag.ALMOST, MorphologicalTag.A2SG, MorphologicalTag.A2PL, MorphologicalTag.A3PL,
            MorphologicalTag.WITHOUT, MorphologicalTag.DISTRIBUTIVE, MorphologicalTag.CAUSATIVE, MorphologicalTag.DATIVE,
            MorphologicalTag.OPTATIVE, MorphologicalTag.ABLE, MorphologicalTag.FUTUREPARTICIPLE, MorphologicalTag.FUTURE,
            MorphologicalTag.REPEAT, MorphologicalTag.EVERSINCE, MorphologicalTag.SINCEDOINGSO, MorphologicalTag.NOTABLESTATE,
            MorphologicalTag.WITHOUTBEINGABLETOHAVEDONESO, MorphologicalTag.PRESENTPARTICIPLE, MorphologicalTag.BYDOINGSO, MorphologicalTag.FEELLIKE,
            MorphologicalTag.PASTTENSE, MorphologicalTag.ACCUSATIVE, MorphologicalTag.AGENT, MorphologicalTag.A1SG,
            MorphologicalTag.A2PL, MorphologicalTag.WHEN, MorphologicalTag.AFTERDOINGSO, MorphologicalTag.INFINITIVE3,
            MorphologicalTag.HASTILY, MorphologicalTag.A1PL, MorphologicalTag.WHILE, MorphologicalTag.INSTRUMENTAL,
            MorphologicalTag.NARRATIVE, MorphologicalTag.CONDITIONAL, MorphologicalTag.A3SG, MorphologicalTag.A2SG,
            MorphologicalTag.A2PL, MorphologicalTag.A3PL, MorphologicalTag.STAY, MorphologicalTag.START,
            MorphologicalTag.REPEAT
    };

    private ArrayList<String> metaMorphemeList;
    private Word root;

    public static ArrayList<MorphologicalTag> getMetaMorphemeTag(String tag){
        ArrayList<MorphologicalTag> result = new ArrayList<MorphologicalTag>();
        String s = Character.toString(tag.charAt(0));
        if (Word.isPunctuation(s)){
            tag = tag.substring(1, tag.length());
        }
        for (int j = 0; j < metaMorphemes.length; j++) {
            if (tag.equalsIgnoreCase(metaMorphemes[j])) {
                result.add(morphotacticTags[j]);
            }
        }
        return result;
    }

    public static ArrayList<MorphologicalTag> getMetaMorphemeTag(MorphologicalParse parse, String tag){
        ArrayList<MorphologicalTag> result = new ArrayList<MorphologicalTag>();
        String s = Character.toString(tag.charAt(0));
        if (Word.isPunctuation(s)){
            tag = tag.substring(1, tag.length());
        }
        for (int j = 0; j < metaMorphemes.length; j++) {
            if (tag.equalsIgnoreCase(metaMorphemes[j]) && parse.containsTag(morphotacticTags[j])) {
                result.add(morphotacticTags[j]);
            }
        }
        return result;
    }

    public MetamorphicParse(){
    }

    public Word getWord(){
        return root;
    }

    public MetamorphicParse(String parse){
        int i;
        metaMorphemeList = new ArrayList<String>();
        if (parse.equals("+")){
            root = new Word("+");
        } else {
            String[] words = parse.split("\\+");
            root = new Word(words[0]);
            for (i = 1; i < words.length; i++)
                metaMorphemeList.add(words[i]);
        }
    }

    public int size(){
        return metaMorphemeList.size() + 1;
    }

    public void addMetaMorphemeList(String newTacticSet){
        String[] tactics = newTacticSet.split("\\+");
        Collections.addAll(metaMorphemeList, tactics);
    }

    public void removeMetaMorphemeFromIndex(int index){
        int i = index - 1;
        while (i < metaMorphemeList.size()){
            metaMorphemeList.remove(i);
        }
    }

    public String getMetaMorpheme(int index){
        if (index == 0){
            return root.getName();
        } else {
            return metaMorphemeList.get(index - 1);
        }
    }

    public String toString(){
        String result = root.getName();
        for (String metaMorpheme: metaMorphemeList){
            result = result + "+" + metaMorpheme;
        }
        return result;
    }

}
