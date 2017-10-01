import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * A board representing the game
 */
public class Board {
    private final int[][] blocks;
    
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.blocks = new int[blocks.length][blocks[0].length];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                this.blocks[i][j] = blocks[i][j];
            }
        }
    }
    
    // board dimension n
    public int dimension() {
        return this.blocks.length;
    }
    
    // number of blocks out of place
    public int hamming() {
        int hammingScore = 0;
        int currentNumber = 1;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                if (blocks[i][j] != currentNumber && blocks[i][j] != 0) {
                    hammingScore++;
                }
                currentNumber++;
            }
        }
        return hammingScore;
    }
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int n = blocks.length;
        int manhattanScore = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int currentNumber = blocks[i][j];
                if (currentNumber == 0) {
                    continue;
                }
                int goalI = (currentNumber - 1) / n;
                int goalJ = currentNumber - goalI * n - 1;
                manhattanScore += Math.abs(goalI - i) + Math.abs(goalJ - j);
            }
        }
        return manhattanScore;
    }
    
    // is this board the goal board?
    public boolean isGoal() {
        int current = 1;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                if (current != blocks[i][j] && blocks[i][j] != 0) { return false; }
                current++;
            }
        }
        return true;
    }
    
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int firstI = -1;
        int firstJ = -1;
        int secondI = -1;
        int secondJ = -1;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                if (blocks[i][j] != 0 && firstI == -1) {
                    firstI = i;
                    firstJ = j;
                    continue;
                }
                if (blocks[i][j] != 0) {
                    secondI = i;
                    secondJ = j;
                }
            }
        }
        int[][] twinBlocks = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                twinBlocks[i][j] = blocks[i][j];
            }
        }
        int swap = twinBlocks[firstI][firstJ];
        twinBlocks[firstI][firstJ] = twinBlocks[secondI][secondJ];
        twinBlocks[secondI][secondJ] = swap;
        return new Board(twinBlocks);
    }
    
    // does this board equal y?
    public boolean equals(Object y) {
        if (y instanceof Board) {
            Scanner thatBoard = new Scanner(y.toString());
            ArrayList<Integer> thatArr = new ArrayList<>();
            while (thatBoard.hasNext()) {
                thatArr.add(thatBoard.nextInt());
            }
            
            Scanner thisBoard = new Scanner(this.toString());
            ArrayList<Integer> thisArr = new ArrayList<>();
            while (thisBoard.hasNext()) {
                thisArr.add(thisBoard.nextInt());
            }
            
            return thatArr.equals(thisArr);
        }
        else {
            return super.equals(y);
        }
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors() {
        int n = blocks.length;
        Stack<Board> neightborBoards = new Stack<>();
        int zeroI = -1;
        int zeroJ = -1;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                if (blocks[i][j] == 0) {
                    zeroI = i;
                    zeroJ = j;
                }
            }
        }
        int[] rowAdds = new int[]{0, -1, 0, 1};
        int[] colAdds = new int[]{-1, 0, 1, 0};
        for (int i = 0; i < rowAdds.length; i++) {
            if (zeroI + rowAdds[i] >= 0 && zeroI + rowAdds[i] < n
                && zeroJ + colAdds[i] >= 0 && zeroJ + colAdds[i] < n) {
                int[][] newNeighboard = new int[n][n];
                for (int row = 0; row < n; row++) {
                    for (int col = 0; col < n; col++) {
                        newNeighboard[row][col] = blocks[row][col];
                    }
                }
                int swap = newNeighboard[zeroI + rowAdds[i]][zeroJ + colAdds[i]];
                newNeighboard[zeroI + rowAdds[i]][zeroJ + colAdds[i]] = 0;
                newNeighboard[zeroI][zeroJ] = swap;
                neightborBoards.push(new Board(newNeighboard));
            }
        }
        return neightborBoards;
    }
    
    // string representation of this board (in the output format specified below)
    public String toString() {
        int n = blocks.length;
        int numberLengths = (int) Math.log10(n * n);
        numberLengths = 2;
        StringBuilder sb = new StringBuilder();
        sb.append(dimension() + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j != 0) {
                    String thisNumber = String.format(" %" + numberLengths + "d", blocks[i][j]);
                    sb.append(thisNumber);
                }
                else {
                    String thisNumber = String.format("%" + numberLengths + "d", blocks[i][j]);
                    sb.append(thisNumber);
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
