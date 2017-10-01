public class Outcast {
    private WordNet wordNet;
    
    // Constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }
    
    // Given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int dt;
        int maxDist = 0;
        int outcastPosition = 0;
        for (int i = 0; i < nouns.length; i++) {
            dt = 0;
            for (int j = 0; j < nouns.length; j++) {
                dt += wordNet.distance(nouns[i], nouns[j]);
            }
            if (maxDist < dt) {
                maxDist = dt;
                outcastPosition = i;
            }
        }
        return nouns[outcastPosition];
    }
}
