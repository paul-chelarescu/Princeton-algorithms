import edu.princeton.cs.algs4.Digraph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordNet  {
    private Digraph hypernyms;
    public ArrayList<SynsetEntry> synsetEntries;
    private String lastCommonWord;
    private Map<String, List<Integer>> wordPositions;
    
    // Constructor takes the name of the two input files
    public WordNet(String synsetsCsv, String hypernymsCsv) {
        synsetEntries = new ArrayList<>();
        wordPositions = new HashMap<>();
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(synsetsCsv));
            while ((line = br.readLine()) != null) {
                String[] row = line.split(cvsSplitBy);
                synsetEntries.add(new SynsetEntry(Integer.parseInt(row[0]), row[1], row[2]));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        hypernyms = new Digraph(synsetEntries.size());
        try {
            br = new BufferedReader(new FileReader(hypernymsCsv));
            while ((line = br.readLine()) != null) {
                String[] row = line.split(cvsSplitBy);
                for (int i = 1; i < row.length; i++) {
                    hypernyms.addEdge(Integer.parseInt(row[0]), Integer.parseInt(row[i]));
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    // Returns all WordNet nouns
    public Iterable<String> nouns() {
        Set<String> words = new HashSet<>();
        for (SynsetEntry sn : synsetEntries) {
            words.addAll(sn.nouns);
        }
        return words;
    }
    
    // Is the word a WordNet noun?
    public boolean isNoun(String word) {
        return getPosition(word) != null;
    }
    
    public List<Integer> getPosition(String word) {
        if (wordPositions.get(word) != null) return wordPositions.get(word);
        else return null;
    }
    
    // public int getPosition(String word) {
    //     int left = 0, right = synsetEntries.size() - 1, mid;
    //     while (left <= right) {
    //         mid = (left + right) / 2;
    //         if (synsetEntries.get(mid).nouns.contains(word)) {
    //             return mid;
    //         }
    //         else if (word.compareTo(synsetEntries.get(mid).nouns.getFirst()) > 0) {
    //             left = mid + 1;
    //         }
    //         else {
    //             right = mid - 1;
    //         }
    //     }
    //     return -1;
    // }
    
    // Distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        // BFS starting in nounA and nounB, mark distance from origin, add the values when I hit
        // a visited one
        for (int posA : getPosition(nounA)) {
            for (int posB : getPosition(nounB)) {
                if (posA == posB) {
                    lastCommonWord = nounA;
                    return 0;
                }
            }
        }
        // if (getPosition(nounA) == getPosition(nounB)) {
        //     lastCommonWord = nounA;
        //     return 0;
        // }
        
        int[] distance = new int[synsetEntries.size()];
        
        boolean[] visitedA = new boolean[synsetEntries.size()];
        boolean[] visitedB = new boolean[synsetEntries.size()];
        
        for (int pos : getPosition(nounA)) {
            visitedA[pos] = true;
        }
        for (int pos : getPosition(nounB)) {
            visitedB[pos] = true;
        }
        
        Queue<Integer> nounAQ = new LinkedList<>();
        Queue<Integer> nounBQ = new LinkedList<>();
        for (int pos : getPosition(nounA)) {
            nounAQ.add(pos);
        }
        for (int pos : getPosition(nounB)) {
            nounBQ.add(pos);
        }
        
        int distanceBetween = -1;
        Integer currentA, currentB;
        
        boolean foundCommon = false;
        boolean changedThisRound = true;
        int lastCommonDistance = Integer.MAX_VALUE;
        int changedOnce = 1;
        while (!foundCommon || changedThisRound) {
            currentA = nounAQ.poll();
            currentB = nounBQ.poll();
            if (foundCommon) {
                if (distanceBetween < lastCommonDistance) {
                    lastCommonDistance = distanceBetween;
                    changedThisRound = true;
                }
                else {
                    changedThisRound = false;
                    distanceBetween = lastCommonDistance;
                }
                if (changedOnce == 0) {
                    changedThisRound = false;
                }
                changedOnce -= 1;
            }
            if (currentA != null) {
                for (int neighbourA : hypernyms.adj(currentA)) {
                    if (visitedA[neighbourA]) { continue; }
                    if (visitedB[neighbourA]) {
                        foundCommon = true;
                        lastCommonWord = synsetEntries.get(neighbourA).nouns.peekFirst();
                        distanceBetween = distance[neighbourA] + distance[currentA] + 1;
                    }
                    visitedA[neighbourA] = true;
                    distance[neighbourA] = distance[currentA] + 1;
                    nounAQ.add(neighbourA);
                }
            }
            if (currentB != null) {
                for (int neighbourB : hypernyms.adj(currentB)) {
                    if (visitedB[neighbourB]) { continue; }
                    if (visitedA[neighbourB]) {
                        foundCommon = true;
                        lastCommonWord = synsetEntries.get(neighbourB).nouns.peekFirst();
                        distanceBetween = distance[neighbourB] + distance[currentB] + 1;
                    }
                    visitedB[neighbourB] = true;
                    distance[neighbourB] = distance[currentB] + 1;
                    nounBQ.add(neighbourB);
                }
            }
        }
        return lastCommonDistance;
    }
    
    // A synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        distance(nounA, nounB);
        return lastCommonWord;
    }
    
    public class SynsetEntry {
        public int number;
        public LinkedList<String> nouns;
        public String description;
        
        public SynsetEntry(int number, String nouns, String description) {
            this.number = number;
            this.nouns = new LinkedList<>();
            String[] intermidiateNouns = nouns.split(" ");
            for (int i = 0; i < intermidiateNouns.length; i++) {
                if (wordPositions.get(intermidiateNouns[i]) == null) {
                    wordPositions.put(intermidiateNouns[i], new LinkedList<>());
                    wordPositions.get(intermidiateNouns[i]).add(number);
                }
                else {
                    wordPositions.get(intermidiateNouns[i]).add(number);
                }
                this.nouns.add(intermidiateNouns[i]);
            }
            this.description = description;
        }
        
        public String toString() {
            return number + " " + nouns + " " + description;
        }
    }
}
