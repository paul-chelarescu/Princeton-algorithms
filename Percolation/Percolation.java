import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private enum State { CLOSED, OPEN };
    
    private State[] state;
    private WeightedQuickUnionUF site;
    private int numberOfOpenSites = 0;
    private int n;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("N must by positive");
        // n * n and a top and a bottom
        site = new WeightedQuickUnionUF(n * n + 2);
        state = new State[n * n + 2];
        for (int i = 1; i <= n * n; i++) {
            state[i] = State.CLOSED;
        }
        state[0] = State.OPEN;
        state[n * n + 1] = State.OPEN;
        this.n = n;
    }

    private void validateIndicies(int x, int y) {
        if (x < 1 || y < 1 || x > n || y > n) 
            throw new IndexOutOfBoundsException("For aguments: "
                    + x + " " + y);
        if (x <= 0 || y <= 0) 
            throw new IllegalArgumentException("For arguments: "
                    + x + " " + y);
    }

    private int xyTo1D(int x, int y) {
        validateIndicies(x, y);
        return (x - 1) * n + y;
    }

    // Returns an array of the position of neighbours in 1D form
    private int[] neighbours(int row, int col) {
        validateIndicies(row, col);
        if (row == 1 && col == 1) {
            return new int[]{xyTo1D(row + 1, col), xyTo1D(row, col + 1)};
        } 
        else if (row == 1 && col != n) {
            return new int[]{xyTo1D(row, col - 1), xyTo1D(row + 1, col),
                           xyTo1D(row, col + 1)};
        } 
        else if (row == 1 && col == n) {
            return new int[]{xyTo1D(row, col - 1), xyTo1D(row + 1, col)};
        } 
        else if (row != n && col == 1) {
            return new int[]{xyTo1D(row - 1, col), xyTo1D(row, col + 1),
                           xyTo1D(row + 1, col)};
        } 
        else if (row != n && col == n) {
            return new int[]{xyTo1D(row, col - 1), xyTo1D(row - 1, col),
                           xyTo1D(row + 1, col)};
        } 
        else if (row == n && col == 1) {
            return new int[]{xyTo1D(row - 1, col), xyTo1D(row, col + 1)};
        } 
        else if (row == n && col != n) {
            return new int[]{xyTo1D(row, col - 1), xyTo1D(row - 1, col),
                           xyTo1D(row, col + 1)};
        } 
        else if (row == n && col == n) {
            return new int[]{xyTo1D(row, col - 1), xyTo1D(row - 1, col)};
        } 
        else {
            return new int[]{xyTo1D(row, col - 1), xyTo1D(row - 1, col),
                           xyTo1D(row, col + 1), xyTo1D(row + 1, col)};
        }
    }
    
    public void open(int row, int col) {
        if (state[xyTo1D(row, col)] == State.CLOSED) {
            int[] positionsToOpen = neighbours(row, col);
            for (int pos : positionsToOpen) {
                if (state[pos] == State.OPEN) {
                    site.union(xyTo1D(row, col), pos);
                }
            }
            if (row == 1) {
                site.union(0, xyTo1D(row, col));
            }
            if (row == n) {
                site.union(n * n + 1, xyTo1D(row, col));
            }
            state[xyTo1D(row, col)] = State.OPEN;
            numberOfOpenSites++;
        }
    }

    public boolean isOpen(int row, int col) {
        return state[xyTo1D(row, col)] == State.OPEN;
    }

    public boolean isFull(int row, int col) {
        return site.connected(0, xyTo1D(row, col));
    }
    
    public int numberOfOpenSites() { return numberOfOpenSites; }

    public boolean percolates() {
        return site.connected(0, n * n + 1);
    }
}
