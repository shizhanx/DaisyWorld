package DaisyWorld;

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

        Ground ground = new Ground();
        ground.initialize();
        // Update the ground for the pre-defined times
        for (int i = 0; i < Params.TRIALS_OF_SIMULATION; i++) {
            ground.update();
        }
    }
}
