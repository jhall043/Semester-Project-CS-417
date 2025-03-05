import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static edu.odu.cs.cs417.TemperatureParser.CoreTempReading;
import static edu.odu.cs.cs417.TemperatureParser.parseRawTemps;
import static edu.odu.cs.cs417.PiecewiseLinearInterpolation.interpolate;  // Import interpolation

/**
 * The MainDriver class is responsible for reading temperature data from a file,
 * parsing it, and performing interpolation on the temperature readings.
 */
public class MainDriver {

    /**
     * The main function that drives the temperature parsing and interpolation.
     *
     * <ul>
     *   <li> Reads a temperature file provided as a command-line argument. </li>
     *   <li> Parses the raw temperature readings at fixed time intervals. </li>
     *   <li> Prints the parsed temperature data. </li>
     *   <li> Prompts the user to enter a time step for interpolation. </li>
     *   <li> Computes and prints interpolated temperature values for each CPU core. </li>
     * </ul>
     *
     * @param args Command-line arguments. The first argument should be the filename.
     */
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
        System.out.print("\nEnter a time step to interpolate: ");
        int queryTime = scanner.nextInt();

        // Interpolate for each core
        System.out.println("\nInterpolated Temperatures:");
        int numCores = allTheTemps.get(0).readings.length;
        for (int coreIdx = 0; coreIdx < numCores; coreIdx++) {
            double interpolatedTemp = interpolate(allTheTemps, queryTime, coreIdx);
            System.out.printf("Core %d -> %.2f°C%n", coreIdx, interpolatedTemp);
        }

        scanner.close();
    }
}
