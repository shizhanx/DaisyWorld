package GreyDaisyExtension;

import java.util.Arrays;
import java.util.Random;

/**
 * A daisy in the daisy world model.
 * The Albedo of a daisy is decided by its colour. The corresponding Albedo
 * data is defined in the Params.
 */
public class Daisy {

    private final int DYING_AGE = 25;

    private int currentAge;
    // Colour is represented by the genes
    private final int[] genes = new int[4];

    /**
     * Construct a new Daisy with the specified colour and random age and genes
     * according to its colour
     * @param colour colour of this daisy
     */
    public Daisy(Params.DAISY_COLOUR colour) {
        Random random = new Random();
        currentAge = random.nextInt(DYING_AGE);
        // Randomly generate the genes
        int offset = 0;
        if (colour == Params.DAISY_COLOUR.black) {
            offset = 2;
        }
        if (random.nextBoolean()) {
            genes[offset] = 1;
            genes[offset + 1] = 1;
        } else {
            genes[offset + random.nextInt(2)] = 1;
        }
    }

    /**
     * Construct a new Daisy according to its parents
     * @param parent1 the first parent
     * @param parent2 the second parent
     */
    public Daisy(Daisy parent1, Daisy parent2) {
        currentAge = 0;
        genes[0] = parent1.getRandomGene(true);
        genes[1] = parent2.getRandomGene(true);
        genes[2] = parent1.getRandomGene(false);
        genes[3] = parent2.getRandomGene(false);
    }

    /**
     * Increment the age of this daisy and check its survivability.
     * Daisies outside of the suited temperature will age 3 times faster.
     * @return whether the daisy is dead or not
     */
    protected boolean isDead(double temperature) {
        switch (getColour()) {
            case grey:
                if (temperature < Params.GREY_LOWER && temperature > Params.GREY_HIGHER)
                    currentAge += 3;
                break;
            case black:
                if (temperature < Params.BLACK_LOWER && temperature > Params.BLACK_HIGHER)
                    currentAge += 3;
                break;
            case white:
                if (temperature < Params.WHITE_LOWER && temperature > Params.WHITE_HIGHER)
                    currentAge += 3;
                break;
        }
        currentAge++;
        return currentAge >= DYING_AGE;
    }

    /**
     * Get a random gene from the specified pair
     * @param firstPair whether from the first or second pair of genes
     * @return one random gene
     */
    protected int getRandomGene(boolean firstPair) {
        int pair = firstPair ? 0 : 1;
        return genes[pair * 2 + new Random().nextInt(2)];
    }

    /**
     * Get the colour of this daisy from its genes
     * @return the colour, or null if it has no dominant gene.
     */
    protected Params.DAISY_COLOUR getColour() {
        int firstSum = genes[0] + genes[1], secondSum = genes[2] + genes[3];
        if (firstSum > 0 && secondSum > 0)
            return Params.DAISY_COLOUR.grey;
        if (firstSum > 0)
            return Params.DAISY_COLOUR.white;
        if (secondSum > 0)
            return Params.DAISY_COLOUR.black;
        return null;
    }
}
