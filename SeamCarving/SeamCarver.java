import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

public class SeamCarver {
    
    private static final int RED = 0;
    private static final int GREEN = 1;
    private static final int BLUE = 2;
    
    private static final int UP_LEFT = 0;
    private static final int UP_UP = 1;
    private static final int UP_RIGHT = 2;
    
    private static final int STOP = 3;
    
    private Picture picture;
    private double [][] en;
    
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;
        renewEn();
    }
    
    private void renewEn() {
        en = new double[height()][width()];
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                en[row][col] = energy(col, row);
            }
        }
    }
    
    // current picture
    public Picture picture() {
        return picture;
    }
    
    // width of current picture
    public int width() {
        return picture.width();
    }
    
    // height of current picture
    public int height() {
        return picture.height();
    }
    
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (isBorder(x, y)) { return 1000; }
        
        double delta_x_2 = Math.pow(diff_x(RED, x, y), 2) + Math.pow(diff_x(GREEN, x, y), 2) + Math
            .pow(diff_x(BLUE, x, y), 2);
        
        double delta_y_2 = Math.pow(diff_y(RED, x, y), 2) + Math.pow(diff_y(GREEN, x, y), 2) + Math
            .pow(diff_y(BLUE, x, y), 2);
        
        return Math.sqrt(delta_x_2 + delta_y_2);
    }
    
    private int diff_x(int color, int x, int y) {
        switch (color) {
            case RED:
                return picture.get(x + 1, y).getRed() - picture.get(x - 1, y).getRed();
            case GREEN:
                return picture.get(x + 1, y).getGreen() - picture.get(x - 1, y).getGreen();
            case BLUE:
                return picture.get(x + 1, y).getBlue() - picture.get(x - 1, y).getBlue();
            default:
                throw new IllegalArgumentException("Wrong color");
        }
    }
    
    private int diff_y(int color, int x, int y) {
        switch (color) {
            case RED:
                return picture.get(x, y + 1).getRed() - picture.get(x, y - 1).getRed();
            case GREEN:
                return picture.get(x, y + 1).getGreen() - picture.get(x, y - 1).getGreen();
            case BLUE:
                return picture.get(x, y + 1).getBlue() - picture.get(x, y - 1).getBlue();
            default:
                throw new IllegalArgumentException("Wrong color");
        }
    }
    
    private boolean isBorder(int x, int y) {
        return x == 0 || x == (en[0].length - 1) || y == 0 || y == (en.length - 1);
    }
    
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double dist[][] = new double[en.length][en[0].length];
        init_dist(dist);
        int prev[][] = new int[en.length][en[0].length];
        init_prev(prev);
        boolean visited[][] = new boolean[en.length][en[0].length];
        init_visit(visited);
    
        Stack<Point> stack = new Stack<>();
        
        for (int i = 0; i < en[0].length; i++) {
            topoSortUtil(new Point(i, 0), visited, stack);
        }
        
        while (!stack.isEmpty()) {
            Point currentP = stack.pop();
            
            for (Point downP : getNextPoints(currentP)) {
                if (dist[downP.row][downP.col] >
                    dist[currentP.row][currentP.col] + en[downP.row][downP.col]) {
                    dist[downP.row][downP.col] =
                    dist[currentP.row][currentP.col] + en[downP.row][downP.col];
                    prev[downP.row][downP.col] = getPrevForPoint(currentP, downP);
                }
            }
        }
        
        int currCol = getMinCol(prev);
        Stack<Integer> vSeam = new Stack<>();
        
        for (int row = en.length - 1; row >= 0; row--) {
            vSeam.push(currCol);
            currCol = getNextMinCol(currCol, row,  prev);
        }
        
        int[] vSeamArr = new int[en.length];
        
        for (int i = 0; i < en.length; i++) {
            vSeamArr[i] = vSeam.pop();
        }
        
        return vSeamArr;
    }
    
    private int getNextMinCol(int currCol, int row, int[][] prev) {
        int direction = prev[row][currCol];
        if (direction == UP_LEFT) {
            return currCol - 1;
        }
        if (direction == UP_UP || direction == STOP) {
            return currCol;
        }
        if (direction == UP_RIGHT) {
            return currCol + 1;
        }
        throw new RuntimeException("Wrong direction reconstruction");
    }
    
    private int getMinCol(int[][] prev) {
        int min = 0;
        int minVal = Integer.MAX_VALUE;
        
        for (int col = 0; col < en[0].length; col++) {
            if (minVal > prev[en.length - 1][col]) {
                minVal = prev[en.length - 1][col];
                min = col;
            }
        }
        return min;
    }
    
    private int getPrevForPoint(Point above, Point below) {
        if (above.col - below.col == -1) return UP_LEFT;
        if (above.col - below.col == 0) return UP_UP;
        if (above.col - below.col == 1) return UP_RIGHT;
        throw new IllegalArgumentException("You've taken the wrong diagonal");
    }
    
    private void topoSortUtil(Point p, boolean[][] visited, Stack stack) {
        visited[p.row][p.col] = true;
    
        Iterable<Point> it = getNextPoints(p);
        for (Point downP : it) {
            if (!visited[downP.row][downP.col]) {
                topoSortUtil(downP, visited, stack);
            }
        }
        stack.push(p);
    }
    
    private Iterable<Point> getNextPoints(Point p) {
        if (p.row == en.length - 1) return new Stack<>();
        if (p.col == 0) {
            Stack<Point> stack = new Stack<>();
            stack.push(new Point(p.col + 1, p.row + 1));
            stack.push(new Point(p.col, p.row + 1));
            return stack;
        }
        if (p.col == en[0].length - 1) {
            Stack<Point> stack = new Stack<>();
            stack.push(new Point(p.col, p.row + 1));
            stack.push(new Point(p.col - 1, p.row + 1));
            return stack;
        }
        else {
            Stack<Point> stack = new Stack<>();
            stack.push(new Point(p.col + 1, p.row + 1));
            stack.push(new Point(p.col, p.row + 1));
            stack.push(new Point(p.col - 1, p.row + 1));
            return stack;
        }
    }
    
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        picture = transposePicture(picture);
        renewEn();
        int[] hSeam = findVerticalSeam();
        picture = transposePicture(picture);
        renewEn();
        return hSeam;
    }
    
    private double[][] transpose(double[][] m) {
        double[][] transposedM = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                transposedM[j][i] = m[i][j];
            }
        }
        return transposedM;
    }
    
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        Picture newP = new Picture(width() - 1, height());
        for (int row = 0; row < height(); row++) {
            int oldCol = 0;
            for (int col = 0; col < width() - 1; col++) {
                if (col == seam[row]) oldCol++;
                newP.set(col, row, picture.get(oldCol, row));
                oldCol++;
            }
        }
        picture = newP;
        renewEn();
    }
    
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        picture = transposePicture(picture);
        renewEn();
        removeVerticalSeam(seam);
        picture = transposePicture(picture);
        renewEn();
    }
    
    private Picture transposePicture(Picture p) {
        Picture transposedP = new Picture(p.height(),p.width());
        for (int row = 0; row < p.height(); row++) {
            for (int col = 0; col < p.width(); col++) {
                transposedP.set(row, col, p.get(col, row));
            }
        }
        return transposedP;
    }
    
    public class Point {
        int col;
        int row;
        public Point (int col, int row) {
            this.col = col;
            this.row = row;
        }
        @Override
        public String toString() {
            return "[" + col + "," + row + "]";
        }
    }
    
    private void init_dist(double[][] dist) {
        for (int row = 1; row < dist.length; row++) {
            for (int col = 0; col < dist[0].length; col++) {
                dist[row][col] = Double.POSITIVE_INFINITY;
            }
        }
        for (int col = 0; col < dist[0].length; col++) {
            dist[0][col] = 1000.0;
        }
    }
    
    private void init_prev(int[][] prev) {
        for (int row = 1; row < prev.length; row++) {
            for (int col = 0; col < prev[0].length; col++) {
                prev[row][col] = -1;
            }
        }
        
        for(int col = 0; col < prev[0].length; col++) {
            prev[0][col] = STOP;
        }
    }
    
    private void init_visit(boolean[][] visit) {
        for (int row = 0; row < visit.length; row++) {
            for (int col = 0; col < visit[0].length; col++) {
                visit[row][col] = false;
            }
        }
    }
    
}
