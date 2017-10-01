import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;

/**
 * Created by paul on 22.03.2017.
 */
public class Solver {
    // Wrapper class
    private class SearchNode implements Comparable<SearchNode> {
        private SearchNode previous;
        private Board board;
        private int moves;
        
        public SearchNode(SearchNode previous, Board board, int n) {
            this.previous = previous;
            this.board = board;
            this.moves = n;
        }
        
        public int compareTo(SearchNode b) {
            SearchNode a = this;
            return a.board.manhattan() + a.moves - b.board.manhattan() - b.moves;
        }
    }
    
    private Stack<Board> sequences;
    private SearchNode endGoal;
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException("No solver with null board");
        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> pqTwin = new MinPQ<>();
        pq.insert(new SearchNode(null, initial, 0));
        pqTwin.insert(new SearchNode(null, initial.twin(), 0));
        sequences = new Stack<>();
        SearchNode current = null;
        SearchNode currentTwin;
        boolean lockstep = true;
        while (true) {
            if (lockstep) {
                current = pq.delMin();
                if (current.board.isGoal()) { break; }
                for (Board neighbour : current.board.neighbors()) {
                    if (current.previous == null) {
                        pq.insert(new SearchNode(current, neighbour, 1));
                    }
                    else if (!current.previous.board.equals(neighbour)) {
                        pq.insert(new SearchNode(current, neighbour, current.moves + 1));
                    }
                }
            }
            else {
                currentTwin = pqTwin.delMin();
                if (currentTwin.board.isGoal()) { break; }
                for (Board neighbour : currentTwin.board.neighbors()) {
                    if (currentTwin.previous == null) {
                        pqTwin.insert(new SearchNode(currentTwin, neighbour, 1));
                    }
                    else if (!currentTwin.previous.board.equals(neighbour)) {
                        pqTwin.insert(new SearchNode(currentTwin, neighbour, currentTwin.moves + 1));
                    }
                }
            }
            lockstep = !lockstep;
        }
        if (lockstep) { endGoal = current; }
        else { endGoal = null; }
    }
    
    // is the initial board solvable?
    public boolean isSolvable() {
        return endGoal != null;
    }
    
    public int moves() {
        if (isSolvable()) { return endGoal.moves; }
        return -1;
    }
    
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) {
            SearchNode current = endGoal;
            while (current != null) {
                sequences.push(current.board);
                current = current.previous;
            }
            return sequences;
        }
        return null;
    }
    
    // solve a slider puzzle (given below)
//    public static void main(String[] args)
    
}
