package ueb08;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


public class TweetSammlungImp implements TweetSammlung {

    Map<String, Integer> tw = new HashMap<>();
    List<String> original = new LinkedList<>();
    Set<String> stopwords = new HashSet<>();

    public static TweetSammlung create() {
        return new TweetSammlungImp();
    }

    @Override
    public void setStopwords(File file) throws FileNotFoundException {
        FileReader fr = new FileReader(file);
        Scanner sc = new Scanner(fr);

        while(sc.hasNext()){
            stopwords.add(sc.next());
        }
    }

    @Override
    public void ingest(String tweet) {
        List<String> l = TweetSammlung.tokenize(tweet);

        original.add(tweet);
        for (String s : l) {
            if(stopwords.contains(s)){
                continue;
            }
            int anzahl = 0;
            if (tw.containsKey(s)) {
                anzahl = tw.get(s) + 1;
                tw.put(s, anzahl);
            } else {
                anzahl = 1;
                tw.put(s, anzahl);
            }
        }
    }


    @Override
    public Iterator<String> vocabIterator() {
        Map<String, Integer> tw = new HashMap<>();
        List<String> woerter = new LinkedList<>();

        woerter.addAll(tw.keySet());

        woerter.sort(new Comparator<String>() {
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        return woerter.iterator();
    }

    public Iterator <Pair> topHashTags(){

        List <Pair> hashtags = new LinkedList<>();

        for( String s: tw.keySet()) {
            char c = s.charAt(0);
            if (c == '#') {
                hashtags.add(new Pair(s, tw.get(s)));
            }
        }

        hashtags.sort(new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                return Integer.compare(o1.getValue(), o2.getValue());
            }
        });

        return hashtags.iterator();
    }

    @Override
    public Iterator<String> topWords() {
        List <String> topw = new LinkedList<>();
        for(String s: tw.keySet()){
            if(Character.isAlphabetic(s.charAt(0)) == true){
                topw.add(s);
            }
        }

        topw.sort(new Comparator <String> (){
            @Override
            public int compare(String s1, String s2){
                return s1.compareTo(s2);
            }
        });
        return topw.iterator();
    }

    @Override
    public Iterator <Pair<String, Integer>> topTweets() {
        List <Pair<String,Integer>> paare = new LinkedList<>();
        for(String s: original) {
            int wert = 0;
            for (String t : TweetSammlung.tokenize(s)) {
                if (stopwords.contains(t)) {
                    continue;
                    wert += tw.get(t);
                }
            }

            paare.add(new Pair(s,wert));
        }


        paare.sort(new Comparator <Pair<String,Integer>> (){
            @Override
            public int compare(Pair p1, Pair p2){
                return Integer.compare(p1.getValue(),p2.getValue())* (-1);
            }
        });
        return paare.iterator();
    }
}


