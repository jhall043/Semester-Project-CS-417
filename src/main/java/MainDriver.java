import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
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

            // Piecewise Linear Interpolation
            System.out.printf("\nCore %d:%n", coreIdx);
            try {
                double interpolatedTemp = PiecewiseLinearInterpolation.interpolate(coreData, queryTime);
                System.out.printf("  Interpolated Temp: %.2f°C%n", interpolatedTemp);
            } catch (IllegalArgumentException e) {
                System.out.printf("  Interpolated Temp: Error: %s%n", e.getMessage());
            }

            // Linear Least Squares Approximation
            LinearLeastSquaresApprox approx = new LinearLeastSquaresApprox();
            try {
                approx.fit(times, temps);
                double approxTemp = approx.evaluate(queryTime);
                System.out.printf("  Least Squares Fit: y = %.2fx + %.2f%n", approx.getSlope(), approx.getIntercept());
                System.out.printf("  Approximated Temp: %.2f°C%n", approxTemp);
            } catch (Exception e) {
                System.out.printf("  Least Squares Fit: Error: %s%n", e.getMessage());
            }
        }

        scanner.close();
    }
}