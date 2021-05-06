package DaisyWorld;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // Confirm the number of arguments is correct
        if (args.length != Params.NUM_OF_CONST) {
            System.err.println("Wrong number of arguments");
            System.exit(1);
        }
        // Try to assign the corresponding parameters
        try {
            Params.ALBEDO_OF_WHITE = Double.parseDouble(args[0]);
            Params.ALBEDO_OF_BLACK = Double.parseDouble(args[1]);
            Params.ALBEDO_OF_GROUND = Double.parseDouble(args[2]);
            Params.LUMINOSITY = Double.parseDouble(args[3]);
            Params.START_WHITE = Double.parseDouble(args[4]);
            Params.START_BLACK = Double.parseDouble(args[5]);
            Params.scenario = Params.SCENARIO.valueOf(args[6]);
        } catch (NumberFormatException e) {
            System.err.println("Wrong argument format.");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println("Wrong scenario name.");
            System.exit(1);
        }
        // Only run 500 ticks if the scenario is not ramp-up-ramp-down
        Params.TRIALS_OF_SIMULATION = Params.scenario == Params.SCENARIO.ramp ? 1000 : 500;
        // Write to a test result file
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter("testResult.csv"))) {
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

    private static String[] parseResult(double[] result) {
        return new String[]{
                Integer.toString((int)result[2]),
                Integer.toString((int)result[1]),
                Double.toString(result[0])
        };
    }
}
