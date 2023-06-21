package prisoners;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TextFreedomExperiment {

    @Test
    public void testInvalidNumberOfPrisoners() {
        // Test with zero
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new FreedomExperiment(0);
        });

        // Test with 1
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new FreedomExperiment(1);
        });

        // Test with odd number greater than 2
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new FreedomExperiment(3);
        });

        // Test with negative number
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new FreedomExperiment(-1);
        });
    }

    @Test
    public void testExperimentWithTwoPrisoners() {
        FreedomExperiment experiment = new FreedomExperiment(2);
        Assertions.assertDoesNotThrow(() -> {
            experiment.run();
        });
    }

}
