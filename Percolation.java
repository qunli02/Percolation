import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private final boolean[] isSiteOpen; // holds values of sites being open/closed
    private int numberOfOpenSites;
    private final WeightedQuickUnionUF percolation; // weighted QU graph to keep track of percolation
    private final WeightedQuickUnionUF fullness; // weighted QU graph to keep track of percolation
    private final int arrSize;
    private final int virtualTopIndex;
    private final int virtualBottomIndex;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Size of grid is invalid");
        }
        arrSize = n * n + 2;
        numberOfOpenSites = 0;
        virtualTopIndex = 0;
        virtualBottomIndex = arrSize - 1;
        this.n = n;
        isSiteOpen = new boolean[arrSize];
        percolation = new WeightedQuickUnionUF(arrSize);
        fullness = new WeightedQuickUnionUF(arrSize - 1);
        for (int i = 0; i < arrSize; i++) {
            isSiteOpen[i] = false;
            if (i < n + 1 && i > 0) {
                percolation.union(virtualTopIndex, i);
                fullness.union(virtualTopIndex, i);
            } else if (i >= arrSize - n - 1) {
                percolation.union(virtualBottomIndex, i);
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        throwIfIllegal(row, col);
        if (!isOpen(row, col)) {
            numberOfOpenSites++;
            int siteName = getIndex(row, col);
            isSiteOpen[siteName] = true;

            if (row - 2 >= 0 && isOpen(row - 1, col)) {// top
                percolation.union(percolation.find(getIndex(row - 1, col)), percolation.find(siteName));
                fullness.union(fullness.find(getIndex(row - 1, col)), fullness.find(siteName));
            }
            if (row + 1 <= n && isOpen(row + 1, col)) { // bottom
                percolation.union(percolation.find(getIndex(row + 1, col)), percolation.find(siteName));
                fullness.union(fullness.find(getIndex(row + 1, col)), fullness.find(siteName));
            }
            if (col - 1 > 0 && isOpen(row, col - 1)) { // left
                percolation.union(percolation.find(getIndex(row, col - 1)), percolation.find(siteName));
                fullness.union(fullness.find(getIndex(row, col - 1)), fullness.find(siteName));
            }
            if (col + 1 <= n && isOpen(row, col + 1)) { // right
                percolation.union(percolation.find(getIndex(row, col + 1)), percolation.find(siteName));
                fullness.union(fullness.find(getIndex(row, col + 1)), fullness.find(siteName));
            }
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return isSiteOpen[getIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && fullness.find(getIndex(row, col)) == fullness.find(virtualTopIndex);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (n == 1) {
            return isOpen(1, 1);
        }
        return percolation.find(virtualTopIndex) == percolation.find(virtualBottomIndex);
    }

    private int getIndex(int row, int col) {
        throwIfIllegal(row, col);
        return ((row - 1) * n) + col;
    }

    private boolean isOutOfGrid(int num) {
        return num < 1 || num > n;
    }

    private void throwIfIllegal(int row, int col) {
        if (isOutOfGrid(row) || isOutOfGrid(col)) {
            throw new IllegalArgumentException("Parameter is not valid");
        }
    }

}
