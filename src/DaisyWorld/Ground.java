package DaisyWorld;

import java.util.*;

public class Ground {
    private final int ROW = 29;
    private final int COLUMN = 29;
    // percentage of temperature diffused to neighbours after each time step
    private final double DIFFUSE_PERCENTAGE = 0.5;

    // The time indication of the current run
    private int tick;

    private Patch[][] patches;
    // Record the at most 8 neighbours of each patch to save time
    private Map<Patch, List<Patch>> neighbours;

    /**
     * Create a new ground of patches and the mapping of each patch to
     * its neighbours.
     */
    public Ground() {
        patches = new Patch[ROW][COLUMN];
        neighbours = new HashMap<>();
        tick = 0;
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
     * Update all the patches after one time step.
     */
    public void update() {
        // Increment the tick
        tick++;
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
                // Then check if an offspring is produced
                Daisy offspring = patch.checkSurvivability();
                if (offspring != null) {
                    // Find a random possible neighbour to plant the offspring
                    // or do nothing if no neighbour is available
                    List<Patch> possiblePlaces = new LinkedList<>();
                    for (Patch neighbour: neighbours.get(patch)) {
                        if (neighbour.daisy == null)
                            possiblePlaces.add(neighbour);
                    }
                    if (!possiblePlaces.isEmpty())
                        possiblePlaces
                                .get(new Random().nextInt(possiblePlaces.size()))
                                .daisy = offspring;
                }
            }
        }
        // Finally update the luminosity according to the current scenario
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
                // Defined by NetLogo.
                if (tick > 200 && tick <= 400) {
                    Params.LUMINOSITY += 0.005;
                } else if (tick > 600 && tick <= 850) {
                    Params.LUMINOSITY -= 0.0025;
                }
                break;
            case maintain:
                break;
        }
    }
}
