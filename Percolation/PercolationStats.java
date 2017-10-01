import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
public class PercolationStats {
    // Perform trials independent experiments on an n-by-n grid
    private double[] thresholds;
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Arguments must be positive");
        thresholds = new double[trials];
        int trial = 0;
        while (trial < trials) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                p.open(StdRandom.uniform(1, n + 1), 
                       StdRandom.uniform(1, n + 1));
            }
            thresholds[trial] = p.numberOfOpenSites() / (double) (n * n);
            trial++;
        }
    }
    // Sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }
    // Sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // Low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((1.96 * stddev())/ Math.sqrt(thresholds.length));
    }
    // High endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(thresholds.length));
    }
    
    // Test client (described below)
    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]),
                                                   Integer.parseInt(args[1]));
        System.out.println(String.format("%-25s%s%f", "mean", "= ",  ps.mean()));
        System.out.println(String.format("%-25s%s%f", "stddev", "= ", ps.stddev()));
        System.out.println(String.format("%-25s%s%f%s%f%s",
                             "95% confidence interval", "= [", ps.confidenceLo(),
                             ", ", ps.confidenceHi(), "]"));
    }
}

