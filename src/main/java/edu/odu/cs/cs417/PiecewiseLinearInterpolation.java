package edu.odu.cs.cs417;

import java.util.ArrayList;
import java.util.List;

public class PiecewiseLinearInterpolation {
    public static class Point {
        public final double x;
        public final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Line {
        double m;
        double b;
        double xStart;
        double xEnd;

        Line(Point p1, Point p2) {
            this.xStart = p1.x;
            this.xEnd = p2.x;
            if (p1.x == p2.x) 
                throw new IllegalArgumentException("Points have identical x-values: " + p1.x);

            this.m = (p2.y - p1.y) / (p2.x - p1.x);
            this.b = p1.y - m * p1.x;
        }
    }

    public static double interpolate(List<Point> data, double x) {
        if (data == null || data.size() < 2) {
            throw new IllegalArgumentException("At least two points are required for interpolation.");
        }

        List<Line> lines = new ArrayList<>();
        for (int k = 0; k < data.size() - 1; k++) {
            Point p1 = data.get(k);
            Point p2 = data.get(k + 1);
            lines.add(new Line(p1, p2));
        }

        for (Line line : lines) {
            if (x >= line.xStart && x <= line.xEnd) 
                return line.m * x + line.b;
        }

        double minX = data.get(0).x;
        double maxX = data.get(data.size() - 1).x;
        throw new IllegalArgumentException("Interpolation x-value " + x + " is out of range [" + minX + ", " + maxX + "].");
    }
}