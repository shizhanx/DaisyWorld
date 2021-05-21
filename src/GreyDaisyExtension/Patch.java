package GreyDaisyExtension;

import java.util.Random;

/**
 * A patch in this model's 2D grid world. Each patch has its own temperature and
 * a daisy might be living on this patch.
 */
public class Patch {
    protected Daisy daisy;

    // If a patch receives a new daisy from a neighbour, it's not directly
    // put into the daisy slot to avoid one daily creating more than one
    // new daisies in one time tick.
    protected Daisy newDaisy;

    private double temperature;
    private double receivedDiffuse;

    /**
     * Create a patch with no daisy and an temperature of 0.
     */
    public Patch() {
        daisy = null;
        newDaisy = null;
        temperature = 0;
        receivedDiffuse = 0;
    }

    /**
     * Update the temperature of this specific patch by balancing its current
     * temperature with the newly absorbed energy
     */
    public void updateTemperature() {
        double absorbed, localHeating;
        if (daisy == null) {
            absorbed = (1 - Params.ALBEDO_OF_GROUND) * Params.LUMINOSITY;
        } else {
            if (daisy.colour == Params.DAISY_COLOUR.black)
                absorbed = (1 - Params.ALBEDO_OF_BLACK) * Params.LUMINOSITY;
            else
                absorbed = (1 - Params.ALBEDO_OF_WHITE) * Params.LUMINOSITY;
        }
        if (absorbed > 0) {
            localHeating = 72 * Math.log(absorbed) + 80;
        } else localHeating = 80;
        temperature = (temperature + localHeating) / 2;
    }

    /**
     * diffuse the temperature of this patch by the given percentage and
     * return the amount of temperature each of its neighbours should receive
     * @param percentage the percentage of temperature diffused to neighbours
     * @return the share of temperature each neighbour should receive
     */
    public double diffuse(double percentage) {
        double oldTemp = temperature;
        temperature *= percentage;
        return (oldTemp - temperature) / 8;
    }

    /**
     * Receive a share of diffused temperature from one of its neighbours
     * and store this to its received diffuse.
     * @param receivedTemperature the amount of received temperature.
     */
    public void receiveDiffuse(double receivedTemperature) {
        receivedDiffuse += receivedTemperature;
    }

    /**
     * Finalize updating temperature by adding the received diffuse temperature
     * to its local temperature.
     */
    public void updateTemperatureAfterDiffuse() {
        temperature += receivedDiffuse;
        receivedDiffuse = 0;
    }

    /**
     * Check if the daisy on this patch is still alive and decide whether to
     * give birth to a new daisy. The new daisy might not be put on a patch
     * and disappear at last.
     * @return the new daisy it has given birth to.
     */
    public Daisy checkSurvivability() {
        if (daisy != null) {
            if (daisy.isDead())
                daisy = null;
            if (daisy != null) {
                double probability = 0.1457 * temperature -
                        0.0032 * temperature * temperature - 0.6443;
                if (new Random().nextDouble() <= probability) {
                    return new Daisy(daisy.colour);
                }
            }
        }
        return null;
    }

    /**
     * Update the daisy of this patch after all the reproductions have
     * taken place.
     */
    public void updateDaisyAfterReproducing() {
        if (newDaisy != null) {
            daisy = newDaisy;
            newDaisy = null;
        }
    }

    public double getTemperature() { return temperature; }
}
