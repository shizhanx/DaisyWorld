package GreyDaisyExtension;

import java.util.*;

public class Ground {
    private final int ROW = 29;
    private final int COLUMN = 29;
    // percentage of temperature diffused to neighbours after each time step
    private final double DIFFUSE_PERCENTAGE = 0.5;

    // The time indication of the current run
    private int tick;

    private double globalTemp;
    private int blackPopulation;
    private int whitePopulation;
    private int greyPopulation;

    private final Patch[][] patches;
    // Record the at most 8 neighbours of each patch to save time
    private final Map<Patch, List<Patch>> neighbours;

    /**
     * Create a new ground of patches and the mapping of each patch to
     * its neighbours.
     */
    public Ground() {
        patches = new Patch[ROW][COLUMN];
        neighbours = new HashMap<>();
        tick = 0;
        globalTemp = 0;
        blackPopulation = 0;
        whitePopulation = 0;
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COLUMN; c++) {
                patches[r][c] = new Patch();
                neighbours.put(patches[r][c], new LinkedList<>());
            }
        }
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COLUMN; c++) {
                for (int nr = r - 1; nr <= r + 1; nr++) {
                    for (int nc = c - 1; nc <= c + 1; nc++) {
                        if (nr >= 0 && nr < ROW && nc >= 0 && nc < COLUMN)
                            neighbours.get(patches[r][c]).add(patches[nr][nc]);
                    }
                }
            }
        }
    }

    /**
     * initialize all the patches
     * @return the data output of global temperature, black and white populations.
     */
    public double[] initialize() {
        tick = 0;
        // Set luminosity according to the scenario
        switch (Params.scenario) {
            case low:
                Params.LUMINOSITY = 0.6;
                break;
            case our:
                Params.LUMINOSITY = 1.0;
                break;
            case high:
                Params.LUMINOSITY = 1.4;
                break;
            case ramp:
                Params.LUMINOSITY = 0.8;
                break;
            case maintain:
                break;
        }

        for (Patch[] rows: patches) {
            for (Patch patch: rows) {
                // Randomly create daisies according to the settings
                double random = new Random().nextDouble();
                if (random < Params.START_WHITE) {
                    patch.daisy = new Daisy(Params.DAISY_COLOUR.white);
                    whitePopulation++;
                } else if (random > 1 - Params.START_BLACK) {
                    patch.daisy = new Daisy(Params.DAISY_COLOUR.black);
                    blackPopulation++;
                }
                // Initialize the patch's temperature according to its daisy
                // No diffuse at this point
                patch.updateTemperature();
                globalTemp += patch.getTemperature();
            }
        }
        globalTemp /= ROW * COLUMN;
        // No grey daisy at the beginning
        return new double[]{ globalTemp, blackPopulation, whitePopulation, 0 };
    }

    /**
     * Update all the patches after one time step.
     * @return the data output of global temperature, black and white populations.
     */
    public double[] update() {
        // Increment the tick
        tick++;
        // Initialize the data output
        globalTemp = 0;
        blackPopulation = 0;
        whitePopulation = 0;
        greyPopulation = 0;
        // First update the temperature and record the received diffused
        // temperature from neighbours
        for (Patch[] row: patches) {
            for (Patch patch: row) {
                patch.updateTemperature();
                double diffuseShare = patch.diffuse(DIFFUSE_PERCENTAGE);
                for (Patch neighbour: neighbours.get(patch))
                    neighbour.receiveDiffuse(diffuseShare);
            }
        }
        // Then finalize the update to the temperature and decide whether
        // new daisies should appear on any patch
        for (Patch[] row: patches) {
            for (Patch patch : row) {
                // Add the received diffuse temperature to the patches
                patch.updateTemperatureAfterDiffuse();
                globalTemp += patch.getTemperature();
                // Then check if an offspring is produced
                if (patch.checkSurvivability()) {
                    List<Patch> emptyNeighbour = new LinkedList<>(), daisyNeighbour = new LinkedList<>();
                    for (Patch neighbour: neighbours.get(patch)) {
                        if (neighbour.daisy == null && neighbour.newDaisy == null)
                            emptyNeighbour.add(neighbour);
                        else if (neighbour.daisy != null)
                            daisyNeighbour.add(neighbour);
                    }
                    // Only reproduce when available patch and partner daisy exist
                    if (!emptyNeighbour.isEmpty() && !daisyNeighbour.isEmpty()) {
                        Daisy partner = daisyNeighbour
                                .get(new Random().nextInt(daisyNeighbour.size()))
                                .daisy;
                        Daisy offspring = new Daisy(patch.daisy, partner);
                        // Check if the offspring has at least one dominant gene to survive
                        // and if the object patch has the suitable temperature
                        if (offspring.getColour() != null) {
                            Patch object = emptyNeighbour.get(new Random().nextInt(emptyNeighbour.size()));
                            double objectTemperature = object.getTemperature();
                            if (offspring.getColour() == Params.DAISY_COLOUR.white
                                    && objectTemperature > Params.WHITE_LOWER
                                    && objectTemperature < Params.WHITE_HIGHER
                                    || offspring.getColour() == Params.DAISY_COLOUR.black
                                    && objectTemperature > Params.BLACK_LOWER
                                    && objectTemperature < Params.BLACK_HIGHER
                                    || offspring.getColour() == Params.DAISY_COLOUR.grey
                                    && objectTemperature > Params.GREY_LOWER
                                    && objectTemperature < Params.GREY_HIGHER ) {
                                object.newDaisy = offspring;
                            }
                        }
                    }
                }
            }
        }
        // Finally put the new-born daisies to daisy slots from newDaisy slots.
        for (Patch[] row: patches) {
            for (Patch patch : row) {
                patch.updateDaisyAfterReproducing();
            }
        }
        // Count the population
        for (Patch[] row: patches) {
            for (Patch patch: row) {
                if (patch.daisy != null) {
                    if (patch.daisy.getColour() == Params.DAISY_COLOUR.black)
                        blackPopulation++;
                    else if (patch.daisy.getColour() == Params.DAISY_COLOUR.white)
                        whitePopulation++;
                    else
                        greyPopulation++;
                }
            }
        }
        // Finally update the luminosity according to the current scenario
        if (Params.scenario == Params.SCENARIO.ramp) {
            // Defined by NetLogo.
            if (tick > 200 && tick <= 400) {
                Params.LUMINOSITY += 0.005;
            } else if (tick > 600 && tick <= 850) {
                Params.LUMINOSITY -= 0.0025;
            }
        }

        globalTemp /= ROW * COLUMN;
        return new double[]{ globalTemp, blackPopulation, whitePopulation, greyPopulation };
    }
}
