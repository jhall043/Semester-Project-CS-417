package edu.odu.cs.cs417;

public class LinearLeastSquaresApprox {
    private double slope;
    private double intercept;

    /**
     * Creates a new instance with no data fitted yet.
     */
    public LinearLeastSquaresApprox() {
        this.slope = 0.0;
        this.intercept = 0.0;
    }

    /**
     * Fits a line to the given data points using the
     * pre-solved least squares formula.
     *
     * @param x array of x-values
     * @param y array of y-values
     * @throws IllegalArgumentException if input arrays are null, empty, or lengths don’t match
     * @throws ArithmeticException if the slope calculation would divide by zero
     */
    public void fit(double[] x, double[] y) {
        if (x == null || y == null || x.length == 0 || x.length != y.length)
            throw new IllegalArgumentException("Invalid input");
    
        int n = x.length;
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            sumX += x[i];
            sumY += y[i];
            sumXY += x[i] * y[i];
            sumX2 += x[i] * x[i];
        }

        double denominator = n * sumX2 - sumX * sumX;
        if (denominator == 0) 
            throw new ArithmeticException("Fails, denominator = 0");

        slope = (n * sumXY - sumX * sumY) / denominator;
        intercept = (sumY - slope * sumX) / n;
    }

    /**
     * Returns the y-value predicted for x.
     */
    public double evaluate(double x) {
        return slope * x + intercept;
    }

    /**
     * Returns the slope of the line.
     */
    public double getSlope() {
        return slope;
    }

    /**
     * Returns the y-intercept of the line.
     */
    public double getIntercept() {
        return intercept;
    }
}
