package MorphologicalAnalysis;

import java.io.*;
import java.util.*;
import Corpus.*;
import Dictionary.*;
import DataStructure.CounterHashMap;
import Ngram.NGram;

public class TestMorphologicalAnalysis {

    public static void testWord(FsmMorphologicalAnalyzer fsm, String word){
        FsmParseList fsmParses = fsm.morphologicalAnalysis(word);
        for (int i = 0; i < fsmParses.size(); i++){
            System.out.println(fsmParses.getFsmParse(i).transitionList());
        }
    }

    public static void testSentence(FsmMorphologicalAnalyzer fsm, String s){
        Sentence sentence = new Sentence(s);
        FsmParseList[] fsmParses = fsm.morphologicalAnalysis(sentence);
        for (FsmParseList fsmParse : fsmParses) {
            for (int j = 0; j < fsmParse.size(); j++) {
                System.out.println(fsmParse.getFsmParse(j).transitionList());
            }
        }
    }

    public static void testWordForLemma(FsmMorphologicalAnalyzer fsm, String word, String pos){
        FsmParseList fsmParses = fsm.morphologicalAnalysis(word);
        for (int i = 0; i < fsmParses.size(); i++){
            System.out.println(fsmParses.getFsmParse(i).getLastLemmaWithTag(pos));
        }
    }

    public static void analyzeSentence(){
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer();
        Scanner sc = new Scanner(System.in);
        String sentence = "ali topu at";
        while (sentence.length() > 1){
            System.out.print("->");
            sentence = sc.nextLine();
            testSentence(fsm, sentence);
        }
    }

    public static void analyze(){
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer();
        Scanner sc = new Scanner(System.in);
        String word = "ali topu at";
        while (word.length() > 1){
            System.out.print("->");
            word = sc.next();
            testWord(fsm, word);
        }
    }

    public static void generateAllParses(FsmMorphologicalAnalyzer fsm, TxtWord txtWord){
        ArrayList<FsmParse> fsmParses = fsm.generateAllParses(txtWord, txtWord.charCount() + 7);
        for (FsmParse fsmParse : fsmParses){
            System.out.println(fsmParse.getSurfaceForm());
        }
    }

    public static void allParses(){
        TxtDictionary dictionary = new TxtDictionary();
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer();
        Scanner sc = new Scanner(System.in);
        String word = "ali topu at";
        while (word.length() > 1){
            System.out.print("->");
            word = sc.next();
            TxtWord txtWord = (TxtWord) dictionary.getWord(word);
            generateAllParses(fsm, txtWord);
        }
    }

    public static void checkSpeed(){
        int k = 0;
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer(0);
        long previous = Calendar.getInstance().getTimeInMillis();
        String line;
        try {
            FileReader fr = new FileReader("deneme.txt");
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            while (line != null){
                Sentence sentence = new Sentence(line);
                for (int j = 0; j < sentence.wordCount(); j++){
                    fsm.morphologicalAnalysis(sentence.getWord(j).getName());
                    k++;
                    if (k % 1000 == 0){
                        System.out.println(k);
                    }
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateDistinctWordWithPos(){
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer(1000000);
        String line;
        CounterHashMap<String> hashMap = new CounterHashMap<>();
        try {
            FileReader fr = new FileReader("./Data/Corpus/gazete.txt");
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            while (line != null){
                Sentence sentence = new Sentence(line);
                for (int j = 0; j < sentence.wordCount(); j++){
                    FsmParseList parseList = fsm.morphologicalAnalysis(sentence.getWord(j).getName());
                    ArrayList<FsmParseList> parseLists = parseList.constructParseListForDifferentRootWithPos();
                    if (parseLists.size() > 1){
                        String key = "";
                        for (FsmParseList fsmParseList : parseLists){
                            if (key.length() != 0){
                                key += "$" + fsmParseList.getFsmParse(0).getWordWithPos().toString();
                            } else {
                                key = fsmParseList.getFsmParse(0).getWordWithPos().toString();
                            }
                        }
                        hashMap.put(key);
                    }
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String key : hashMap.keySet()){
            System.out.println(key + ":" + hashMap.get(key));
        }
    }

    public static void generateDistinctSubAnalyses(){
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer(1000000);
        String line;
        CounterHashMap<String> hashMap = new CounterHashMap<>();
        try {
            FileReader fr = new FileReader("./Data/Corpus/gazete.txt");
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            while (line != null){
                Sentence sentence = new Sentence(line);
                for (int j = 0; j < sentence.wordCount(); j++){
                    FsmParseList parseList = fsm.morphologicalAnalysis(sentence.getWord(j).getName());
                    ArrayList<FsmParseList> parseLists = parseList.constructParseListForDifferentRootWithPos();
                    for (FsmParseList fsmParseList : parseLists){
                        if (fsmParseList.size() > 1){
                            String key = fsmParseList.parsesWithoutPrefixAndSuffix();
                            if (!hashMap.containsKey(key)){
                                System.out.println(sentence.getWord(j).getName() + "->" + key);
                            }
                            hashMap.put(key);
                        }
                    }
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String key : hashMap.keySet()){
            System.out.println(key + ":" + hashMap.get(key));
        }
    }

    public static void generateParsed(){
        HashSet<String> parsed = new HashSet<>();
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer(1000000);
        String line;
        try {
            PrintWriter output = new PrintWriter(new File("gazete-parsed1.txt"));
            FileReader fr = new FileReader("./Data/Corpus/gazete.txt");
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            while (line != null){
                Sentence sentence = new Sentence(line);
                for (int j = 0; j < sentence.wordCount(); j++){
                    String word = sentence.getWord(j).getName();
                    if (!parsed.contains(word)){
                        parsed.add(word);
                        FsmParseList parseList = fsm.morphologicalAnalysis(word);
                        if (parseList.size() > 0){
                            output.println(word + " " + parseList.size());
                            output.print(parseList.toString());
                        }
                    }
                }
                line = br.readLine();
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkSpeedSameWord(){
        Scanner s = new Scanner(System.in);
        String word;
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer(0);
        System.out.println("->");
        word = s.next();
        for (int i = 0; i < 100000; i++){
            if (i % 1000 == 0){
                System.out.println(i);
            }
            FsmParseList fsmParses = fsm.morphologicalAnalysis(word);
        }
    }

    public static void checkCorrectness(){
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer();
        Scanner s;
        try {
            s = new Scanner(new File("gazete-parsed.txt"));
            while (s.hasNext()){
                String word = s.next();
                int parseCount = s.nextInt();
                boolean ok = true;
                FsmParseList parseList = fsm.morphologicalAnalysis(word);
                if (parseList.size() != parseCount){
                    ok = false;
                    System.out.println(word);
                    System.out.println("---------------------");
                    System.out.print(parseList);
                    System.out.println("---------------------");
                }
                for (int i = 0; i < parseCount; i++){
                    if (!ok){
                        System.out.println(s.next());
                    } else {
                        s.next();
                    }
                }
                if (!ok){
                    System.out.println("*********************");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void createSurfaceNGram() throws FileNotFoundException{
        Scanner input = new Scanner(new File("/Users/olcay/Downloads/title.txt"));
        NGram<String> root = new NGram<String>(2);
        int k = 0;
        while (input.hasNextLine()){
            String line = input.nextLine();
            String[] countAndSentence = line.split(",");
            if (countAndSentence.length == 2 && countAndSentence[0].matches("\\d+")){
                int count = Integer.parseInt(countAndSentence[0]);
                String[] items = countAndSentence[1].split(" ");
                root.addNGramSentence(items, count);
                k++;
                if (k % 100000 == 0){
                    System.out.println(k);
                }
            }
        }
        input.close();
        root.saveAsText("surface.txt");
    }

    public static void createRootNGram() throws FileNotFoundException{
        Object[] tmpArray;
        String[] sentence;
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer(new TxtDictionary("gittigidiyor_dictionary.txt", new TurkishWordComparator(), "gittigidiyor_misspellings.txt"));
        Scanner input = new Scanner(new File("sorted.txt"));
        NGram<String> root = new NGram<String>(2);
        int k = 0;
        while (input.hasNextLine()){
            String lineRead = input.nextLine();
            String[] readItems = lineRead.split(",");
            if (readItems.length != 2){
                continue;
            }
            String line = readItems[0];
            int count;
            try{
                count = Integer.parseInt(readItems[1]);
            }catch (NumberFormatException e){
                count = 0;
            }
            String[] items = line.split(" ");
            ArrayList<String> analyzed = new ArrayList<String>();
            for (int i = 0; i < items.length; i++){
                String item = items[i];
                if (fsm.getDictionary().getCorrectForm(item) != null){
                    item = fsm.getDictionary().getCorrectForm(item);
                }
                FsmParseList parseList = fsm.morphologicalAnalysis(item);
                if (parseList.size() == 0 && item.length() > 0){
                    String item1 = (item.charAt(0) + "").toUpperCase(new Locale("tr")) + item.substring(1);
                    parseList = fsm.morphologicalAnalysis(item1);
                }
                String longestRoot = "";
                for (int j = 0; j < parseList.size(); j++){
                    if (parseList.getFsmParse(j).getWord().getName().length() > longestRoot.length()){
                        longestRoot = parseList.getFsmParse(j).getWord().getName();
                    }
                }
                if (longestRoot.length() > 0){
                    analyzed.add(longestRoot);
                } else {
                    if (analyzed.size() > 1){
                        tmpArray = analyzed.toArray();
                        sentence = Arrays.copyOf(tmpArray, tmpArray.length, String[].class);
                        root.addNGramSentence(sentence, count);
                    }
                    analyzed = new ArrayList<String>();
                }
            }
            if (analyzed.size() > 1){
                tmpArray = analyzed.toArray();
                sentence = Arrays.copyOf(tmpArray, tmpArray.length, String[].class);
                root.addNGramSentence(sentence, count);
            }
            k++;
            if (k % 10000 == 0){
                System.out.println(k);
            }
        }
        input.close();
        root.saveAsText("root-ngram.txt");
    }

    public static void main(String[] args) throws FileNotFoundException{
        //analyzeAll();
        //allParses();
        analyze();
        //analyzeSentence();
        //checkSpeed();
        //checkSpeedSameWord();
        //createNGram();
        //createRootNGram();
    }

}
