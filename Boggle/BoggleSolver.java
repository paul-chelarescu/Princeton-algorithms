import edu.princeton.cs.algs4.Stack;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    
    private int rows;
    private int cols;
    private BoggleBoard board;
    private static boolean[][] visited;
    private TrieSET trie;
    
    public BoggleSolver(String[] dictionary) {
        trie = new TrieSET();
        for (String word : dictionary) {
            trie.add(word);
        }
    }
    
    // Returns the set of all valid words in the given BoggleSolver board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.board = board;
        rows = board.rows();
        cols = board.cols();
        visited = new boolean[rows][cols];
        Set<String> validWords = new HashSet<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                visit(new Point(row, col), toDictWord("", board.getLetter(row, col)), validWords);
            }
        }
        return validWords;
    }
    
    private void visit(Point p, String pre, Set<String> validWords) {
        visited[p.x][p.y] = true;
        for (Point nextP : getNeighbours(p)) {
            char nextLetter = board.getLetter(nextP.x, nextP.y);
            String dictWord = toDictWord(pre, nextLetter);
            if (!visited[nextP.x][nextP.y] && trie.hasPrefix(dictWord)) {
                if (dictWord.length() > 2 && trie.contains(dictWord)) {
                    validWords.add(dictWord);
                }
                visit(nextP, dictWord, validWords);
            }
        }
        visited[p.x][p.y] = false;
    }
    
    private String toDictWord(String boggleWord, char nextLetter) {
        if (nextLetter == 'Q') {
            return boggleWord + "QU";
        } else {
            return boggleWord + nextLetter;
        }
    }
    
    private Iterable<Point> getNeighbours(Point p) {
        Stack<Point> neighbours = new Stack<>();
        
        if (p.x > 0 && p.x < rows - 1 && p.y > 0 && p.y < cols - 1) {
            neighbours.push(new Point(p.x - 1, p.y + 1));
            neighbours.push(new Point(p.x - 1, p.y));
            neighbours.push(new Point(p.x - 1, p.y - 1));
    
            neighbours.push(new Point(p.x, p.y - 1));
            
            neighbours.push(new Point(p.x + 1, p.y - 1));
            neighbours.push(new Point(p.x + 1, p.y));
            neighbours.push(new Point(p.x + 1, p.y + 1));
    
            neighbours.push(new Point(p.x, p.y + 1));
            
            return neighbours;
        } else if (p.x == 0 && p.y > 0 && p.y < cols - 1) {
            neighbours.push(new Point(p.x, p.y - 1));
            neighbours.push(new Point(p.x + 1, p.y - 1));
            neighbours.push(new Point(p.x + 1, p.y));
            neighbours.push(new Point(p.x + 1, p.y + 1));
    
            neighbours.push(new Point(p.x, p.y + 1));
            return neighbours;
        } else if (p.x == 0 && p.y == 0) {
            neighbours.push(new Point(p.x + 1, p.y));
            neighbours.push(new Point(p.x + 1, p.y + 1));
            neighbours.push(new Point(p.x, p.y + 1));
            return neighbours;
    
        } else if (p.x > 0 && p.x < rows - 1 && p.y == 0) {
            neighbours.push(new Point(p.x - 1, p.y + 1));
            neighbours.push(new Point(p.x - 1, p.y));
            neighbours.push(new Point(p.x + 1, p.y));
            neighbours.push(new Point(p.x + 1, p.y + 1));
            neighbours.push(new Point(p.x, p.y + 1));
            return neighbours;
        } else if (p.x == rows - 1 && p.y == 0) {
            neighbours.push(new Point(p.x - 1, p.y + 1));
            neighbours.push(new Point(p.x - 1, p.y));
            neighbours.push(new Point(p.x, p.y + 1));
            return neighbours;
        } else if (p.x == rows - 1 && p.y > 0 && p.y < cols - 1) {
            neighbours.push(new Point(p.x - 1, p.y + 1));
            neighbours.push(new Point(p.x - 1, p.y));
            neighbours.push(new Point(p.x - 1, p.y - 1));
            neighbours.push(new Point(p.x, p.y - 1));
            neighbours.push(new Point(p.x, p.y + 1));
            return neighbours;
        } else if (p.x == rows - 1 && p.y == cols - 1) {
            neighbours.push(new Point(p.x - 1, p.y));
            neighbours.push(new Point(p.x - 1, p.y - 1));
            neighbours.push(new Point(p.x, p.y - 1));
            return neighbours;
        } else if (p.x > 0 && p.x < rows - 1 && p.y == cols - 1) {
            neighbours.push(new Point(p.x - 1, p.y));
            neighbours.push(new Point(p.x - 1, p.y - 1));
            neighbours.push(new Point(p.x, p.y - 1));
            neighbours.push(new Point(p.x + 1, p.y - 1));
            neighbours.push(new Point(p.x + 1, p.y));
            return neighbours;
        } else if (p.x == 0 && p.y == cols - 1) {
            neighbours.push(new Point(p.x, p.y - 1));
            neighbours.push(new Point(p.x + 1, p.y - 1));
            neighbours.push(new Point(p.x + 1, p.y));
            return neighbours;
        } else {
            throw new IllegalArgumentException("Coords outside canvas " + p.x + ", " + p.y);
        }
    }
    
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word.length() >= 3 && word.length() <= 4) {
            return 1;
        } else if (word.length() == 5) {
            return 2;
        } else if (word.length() == 6) {
            return 3;
        } else if (word.length() == 7) {
            return 5;
        } else if (word.length() >= 8) {
            return 11;
        } else {
            throw new IllegalArgumentException(word + " should not be queried");
        }
    }
    
    private class Point {
        public int x;
        public int y;
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    private class TrieSET {
        private static final int R = 26;        // Large letters
        private Node root;      // root of trie
        private int n;          // number of keys in trie
    
        // R-way trie node
        private class Node {
            private Node[] next = new Node[R];
            private boolean isString;
        }
        
        public TrieSET() {
        }
    
        public boolean contains(String key) {
            Node x = get(root, key, 0);
            if (x == null) return false;
            return x.isString;
        }
    
        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            int c =  (key.charAt(d) - 65);
            return get(x.next[c], key, d + 1);
        }
        
        public void add(String key) {
            root = add(root, key, 0);
        }
    
        private Node add(Node x, String key, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                if (!x.isString) n++;
                x.isString = true;
            }
            else {
                int c = (key.charAt(d) - 65);
                x.next[c] = add(x.next[c], key, d + 1);
            }
            return x;
        }
        
        public boolean hasPrefix(String prefix) {
            Node x = get(root, prefix, 0);
            return x != null;
        }
    }
    
}
