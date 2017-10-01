import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class BurrowsWheeler {
    
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        String wholeText = BinaryStdIn.readString();
        CircularSuffixArray c = new CircularSuffixArray(wholeText);
        
        int first = findFirst(c);
        BinaryStdOut.write(first);
        
        for (int i = 0; i < c.length(); i++) {
            char writeChar = wholeText.charAt((c.index(i) - 1 + c.length()) % c.length());
            BinaryStdOut.write(writeChar);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }
    
    private static int findFirst(CircularSuffixArray c) {
        for (int i = 0; i < c.length(); i++) {
            if (c.index(i) == 0) {
                return i;
            }
        }
        throw new IllegalArgumentException("Couldn't find first");
    }
    
    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int firstChar = BinaryStdIn.readInt();
        char[] t = BinaryStdIn.readString().toCharArray();
        char[] firstCol = new char[t.length];
        int[] next = new int[t.length];
        
        int N = t.length;
        CharCount[] charCount = new CharCount[256];
        for (int i = 0; i < 256; i++) {
            charCount[i] = new CharCount();
        }
        for (int i = 0; i < N; i++) {
            charCount[t[i] + 1].times++;
            charCount[t[i] + 1].positions.add(i);
        }
        for (int r = 0; r < 255; r++) {
            charCount[r + 1].times += charCount[r].times;
        }
        for (int i = 0; i < N; i++) {
            firstCol[charCount[t[i]].times] = t[i];
            next[charCount[t[i]].times++] = charCount[t[i] + 1].positions.pop();
        }
        
        int reconstructIndex = firstChar;
        do {
            BinaryStdOut.write(firstCol[reconstructIndex]);
            reconstructIndex = next[reconstructIndex];
        } while (reconstructIndex != firstChar);
        BinaryStdIn.close();
        BinaryStdOut.close();
    }
    
    private static class CharCount {
        public int times;
        public LinkedList<Integer> positions;
        
        public CharCount() {
            times = 0;
            positions = new LinkedList<>();
        }
        
        public int getTimes() {
            return times;
        }
    }
    
    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}
