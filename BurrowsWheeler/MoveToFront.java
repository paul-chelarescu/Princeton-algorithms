import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] order = initOrder(new char[256]);
        
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char pos = findPos(order, c);
            BinaryStdOut.write(pos);
            moveToFront(order, pos);
        }
        BinaryStdOut.close();
        BinaryStdIn.close();
    }
    
    private static void moveToFront(char[] order, char pos) {
        char letterInFront = order[pos];
        for (char shiftIndex = pos; shiftIndex > 0; shiftIndex--) {
            order[shiftIndex] = order[shiftIndex - 1];
        }
        order[0] = letterInFront;
    }
    
    private static char findPos(char[] order, char c) {
        for (char i = 0; i < order.length; i++) {
            if (order[i] == c) return i;
        }
        throw new IllegalArgumentException("Char " + c + " not found in order");
    }
    
    private static char[] initOrder(char[] order) {
        for (char i = 0; i < order.length; i++) {
            order[i] = i;
        }
        return order;
    }
    
    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] order = initOrder(new char[256]);
    
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            BinaryStdOut.write(order[c]);
            moveToFront(order, c);
        }
        BinaryStdOut.close();
        BinaryStdIn.close();
    
    }
    
    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}

