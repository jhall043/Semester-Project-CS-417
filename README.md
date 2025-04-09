# Requirements
* Java Development Kit (JDK) 21.0.4
* Gradle 8.12.1

## Dependencies
For development, the following dependencies were used:

gradle-8.12.1-bin.zip (Installed)
You can check the installed Gradle version using:
gradle --version

# Compile
To compile the project, use the provided Gradle wrapper or the `gradle build` command.

# Run
To run the application, use the following command:
gradle run --args="FILE_PATH.txt" <-- Note that for testing the following was used: gradle run --args="TestData.txt"
This will start the application.

## Project Overview
This project processes CPU core temperature data from a file, providing:
- **Piecewise Linear Interpolation**: Estimates temperatures between time steps.
- **Global Linear Least Squares Approximation**: Fits a linear model (y = mx + b) to each core's data using a pre-solved A|b⃗ method and predicts temperatures.