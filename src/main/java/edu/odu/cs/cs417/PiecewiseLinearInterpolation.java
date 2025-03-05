import java.util.List;

/**
 * Implements Piecewise Linear Interpolation to estimate temperature values
 * between known data points.
 */
public class PiecewiseLinearInterpolation {

    /**
     * Represents a point in a 2D coordinate system.
     * <ul>
     *   <li>_x_ represents the time-step at which the reading was taken.</li>
     *   <li>_y_ represents the temperature value at that time-step.</li>
     * </ul>
     */
    public static class Point {
        /**
         * The x-coordinate (time-step).
         */
        public final double x;

        /**
         * The y-coordinate (temperature value).
         */
        public final double y;

        /**
         * Construct a point with the given time-step and temperature.
         *
         * @param x the time-step
         * @param y the temperature value
         */
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Computes the interpolated temperature at a given time-step using
     * piecewise linear interpolation.
     *
     * @param data a list of known data points (time-step, temperature)
     * @param x the time-step for which interpolation is required
     * @return the interpolated temperature value
     */
    public static double interpolate(List<Point> data, double x) {
        for (int k = 0; k < data.size() - 1; k++) {
            Point p1 = data.get(k);
            Point p2 = data.get(k + 1);

            if (x >= p1.x && x <= p2.x) {
                double m = (p2.y - p1.y) / (p2.x - p1.x);
                double b = p1.y - m * p1.x;
                return b + m * x;
            }
        }
        throw new IllegalArgumentException("Interpolation x-value is out of range.");
    }
}
