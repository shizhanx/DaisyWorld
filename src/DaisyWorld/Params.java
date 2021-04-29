package DaisyWorld;

public class Params {
    // How much light energy is deflected from the ground
    public static double ALBEDO_OF_WHITE = 0.75;
    public static double ALBEDO_OF_BLACK = 0.25;
    public static double ALBEDO_OF_GROUND = 0.4;
    // Initial luminosity of the sun, 1.0 is the default
    public static double LUMINOSITY = 1.0;
    // Starting percentage of each kind of daisies. No more than 50%
    public static double START_WHITE = 0.2;
    public static double START_BLACK = 0.2;
    public static SCENARIO scenario;

    public enum DAISY_COLOUR { black, white }
    public enum SCENARIO {ramp, maintain, low, our, high }
}
