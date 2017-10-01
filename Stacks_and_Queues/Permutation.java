import edu.princeton.cs.algs4.StdIn;
import java.util.Iterator;
public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        String[] words = StdIn.readAllStrings();
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        for (String s : words) q.enqueue(s);
        Iterator<String> itr = q.iterator();
        for (int i = 0; i < k; i++) {
            System.out.println(itr.next());
        }
    }
}
