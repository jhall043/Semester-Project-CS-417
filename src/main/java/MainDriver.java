import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import static edu.odu.cs.cs417.TemperatureParser.CoreTempReading;
import static edu.odu.cs.cs417.TemperatureParser.parseRawTemps;
import edu.odu.cs.cs417.PiecewiseLinearInterpolation;
import edu.odu.cs.cs417.LinearLeastSquaresApprox;

public class MainDriver {
    public static void main(String[] args) {
        BufferedReader tFileStream = null;

        // Handle file input
        try {
            tFileStream = new BufferedReader(new FileReader(new File(args[0])));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: No file provided.");
            return;
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found.");
            return;
        }

        // Parse the raw temperature data
        List<CoreTempReading> allTheTemps = parseRawTemps(tFileStream);

        // Print raw temperatures
        System.out.println("\nRaw Temperature Data:");
        for (CoreTempReading reading : allTheTemps) {
            System.out.println(reading);
        }

        // Ask user for time input
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter a time step to interpolate and approximate: ");
        double queryTime = scanner.nextDouble();

        // Process each core
        int numCores = allTheTemps.get(0).readings.length;
        for (int coreIdx = 0; coreIdx < numCores; coreIdx++) {
            // Extract data points for this core
            List<PiecewiseLinearInterpolation.Point> coreData = new ArrayList<>();
            double[] times = new double[allTheTemps.size()];
            double[] temps = new double[allTheTemps.size()];
            for (int i = 0; i < allTheTemps.size(); i++) {
                CoreTempReading reading = allTheTemps.get(i);
                double time = (double) reading.step;
                double temp = reading.readings[coreIdx];
                coreData.add(new PiecewiseLinearInterpolation.Point(time, temp));
                times[i] = time;
                temps[i] = temp;
            }

            // Create output file for this core
            String inputFilename = args[0];
            String baseName = new File(inputFilename).getName();
            String outputFilename = String.format("%s-core-%02d.txt", baseName.replace(".txt", ""), coreIdx);
            File outFile = new File(outputFilename);
            PrintWriter writer;
            try {
                writer = new PrintWriter(outFile);
            } catch (FileNotFoundException e) {
                System.err.println("Error: Cannot write to output file: " + outputFilename);
                continue;
            }

            // Piecewise Linear Interpolation: write segments to file
            for (int i = 0; i < coreData.size() - 1; i++) {
                var p1 = coreData.get(i);
                var p2 = coreData.get(i + 1);

                double slope = (p2.y - p1.y) / (p2.x - p1.x);
                double intercept = p1.y - slope * p1.x;

                writer.printf("%10.0f <= x <= %10.0f ; y = %10.4f + %10.4f x ; interpolation%n",
                        p1.x, p2.x, intercept, slope);
            }

            // Linear Least Squares Approximation: add final line
            LinearLeastSquaresApprox approx = new LinearLeastSquaresApprox();
            try {
                approx.fit(times, temps);
                double lsSlope = approx.getSlope();
                double lsIntercept = approx.getIntercept();
                double minX = times[0];
                double maxX = times[times.length - 1];

                writer.printf("%10.0f <= x <= %10.0f ; y = %10.4f + %10.4f x ; least-squares%n",
                        minX, maxX, lsIntercept, lsSlope);
            } catch (Exception e) {
                writer.printf("Least Squares Fit: Error: %s%n", e.getMessage());
            }

            writer.close();
        }

        scanner.close();
    }
}
