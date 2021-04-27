package DaisyWorld;

import java.util.List;
import java.util.Random;

public class Patch {
    protected Daisy daisy;
    private double temperature;
    protected boolean seedingAbility;

    public Patch() {
        daisy = null;
        temperature = 0;
        seedingAbility = false;
    }

    public void updateSeedingAbility() {
        // TODO: Daisy increment age, update life property
        if (daisy != null) {
            double probability = 0.1457 * temperature -
                    0.0032 * temperature * temperature - 0.6443;
            if (new Random().nextDouble() <= probability) seedingAbility = true;
        } else seedingAbility = false;
    }

    public void updateTemperature() {
        double absorbed = 0, localHeating = 0;
        if (daisy == null) {
            absorbed = (1 - Params.ALBEDO_OF_GROUND) * Params.LUMINOSITY;
        } else {
            // TODO: Daisy colour?
        }
        if (absorbed > 0) {
            localHeating = 72 * Math.log(absorbed) + 80;
        } else localHeating = 80;
        temperature = (temperature + localHeating) / 2;
        temperature /= 2;
    }

    public void reproduce(List<Patch> neighbours) {
        if (seedingAbility) {
            // TODO: set new daisy's colour the same as this one
            neighbours.get(new Random().nextInt(neighbours.size())).daisy = new Daisy();
        }
    }
}
