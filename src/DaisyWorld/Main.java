package DaisyWorld;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        for (Params.SCENARIO senario: Params.SCENARIO.values()) {
            if (senario != Params.SCENARIO.maintain) {
                Params.scenario = senario;
                // Only run 500 ticks if the scenario is not ramp-up-ramp-down
                Params.TRIALS_OF_SIMULATION = Params.scenario == Params.SCENARIO.ramp ? 1000 : 500;
                // Write to a test result file
                try (CSVWriter csvWriter = new CSVWriter(new FileWriter(senario + "TestResult.csv"))) {
                    csvWriter.writeNext(new String[]{ "White population",
                            "Black population", "Global temperature" });
                    // Initialize the ground
                    Ground ground = new Ground();
                    double[] result = ground.initialize();
                    csvWriter.writeNext(parseResult(result), false);
                    // Update the ground for the pre-defined times
                    for (int i = 0; i < Params.TRIALS_OF_SIMULATION; i++) {
                        result = ground.update();
                        csvWriter.writeNext(parseResult(result));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String[] parseResult(double[] result) {
        return new String[]{
                Integer.toString((int)result[2]),
                Integer.toString((int)result[1]),
                Double.toString(result[0])
        };
    }
}
