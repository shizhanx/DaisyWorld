package GreyDaisyExtension;

public class Params {
    public static int TRIALS_OF_SIMULATION = 1000;
    // How much light energy is deflected from the ground
    public static double ALBEDO_OF_WHITE = 0.75;
    public static double ALBEDO_OF_BLACK = 0.25;
    public static double ALBEDO_OF_GREY = (ALBEDO_OF_BLACK + ALBEDO_OF_WHITE) / 2;
    public static double ALBEDO_OF_GROUND = 0.4;

    // Suitable growth temperature
    public static int WHITE_LOWER = 10;
    public static int WHITE_HIGHER = 45;
    public static int BLACK_LOWER = 0;
    public static int BLACK_HIGHER = 35;
    public static int GREY_LOWER = (WHITE_LOWER + BLACK_LOWER) / 2;
    public static int GREY_HIGHER = (WHITE_HIGHER + BLACK_HIGHER) / 2;

    // Initial luminosity of the sun, 1.0 is the default
    public static double LUMINOSITY = 1.0;
    // Starting percentage of each kind of daisies. No more than 50%
    public static double START_WHITE = 0.2;
    public static double START_BLACK = 0.2;
    public static SCENARIO scenario = SCENARIO.maintain;

    public enum DAISY_COLOUR { black, white, grey }
    public enum SCENARIO { ramp, maintain, low, our, high }
}
