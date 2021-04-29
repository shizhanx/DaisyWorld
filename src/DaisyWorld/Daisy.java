package DaisyWorld;

/**
 * A daisy in the daisy world model.
 * The Albedo of a daisy is decided by its colour. The corresponding Albedo
 * data is defined in the Params.
 */
public class Daisy {

    private final int DYING_AGE = 25;

    protected Params.DAISY_COLOUR colour;
    private int currentAge;

    /**
     * Construct a new Daisy with the specified colour and age of 0
     * @param colour colour of this daisy
     */
    public Daisy(Params.DAISY_COLOUR colour) {
        this.colour = colour;
        currentAge = 0;
    }

    /**
     * Increment the age of this daisy and check its survivability
     * @return whether the daisy is dead or not
     */
    protected boolean isDead() {
        currentAge++;
        return currentAge >= DYING_AGE;
    }
}
