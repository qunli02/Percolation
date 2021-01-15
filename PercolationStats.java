import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] openSites;

    private final static double CONFIDENCE_95 = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException("parameters are invalid");
        }
        openSites = new double[trials];
        Percolation current;
        for (int i = 0; i < trials; i++) {
            current = new Percolation(n);
            while (!current.percolates()) {
                current.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }
            openSites[i] = (double) current.numberOfOpenSites() / (double) (n * n);
        }

    }

    // sample mean of percolation threshold
    public double mean() {

        return StdStats.mean(openSites);
    }


    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(openSites);
    }


    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev()) / Math.sqrt(openSites.length);
    }


    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev()) / Math.sqrt(openSites.length);
    }


    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats a = new PercolationStats(n, t);
        StdOut.printf("%-24s= %f\n", "mean", a.mean());
        StdOut.printf("%-24s= %f\n", "stddev", a.stddev());
        StdOut.printf("%-24s= [%f, %f]\n", "95% confidence interval", a.confidenceLo(), a.confidenceHi());
    }
}
