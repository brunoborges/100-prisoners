package prisoners;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Modern Java tests for the FreedomExperiment class.
 */
public class TextFreedomExperiment {

    @Test
    public void testInvalidNumberOfPrisoners() {
        // Test with zero - using modern text block error messages
        var zeroException = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new FreedomExperiment(0);
        });
        Assertions.assertTrue(zeroException.getMessage().contains("at least 2"));

        // Test with 1
        var oneException = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new FreedomExperiment(1);
        });
        Assertions.assertTrue(oneException.getMessage().contains("at least 2"));

        // Test with odd number greater than 2
        var oddException = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new FreedomExperiment(3);
        });
        Assertions.assertTrue(oddException.getMessage().contains("must be even"));

        // Test with negative number  
        var negativeException = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new FreedomExperiment(-1);
        });
        Assertions.assertTrue(negativeException.getMessage().contains("at least 2"));
    }

    @Test
    public void testExperimentWithTwoPrisoners() {
        var experiment = new FreedomExperiment(2);
        Assertions.assertDoesNotThrow(() -> {
            experiment.run();
        });
    }
    
    @Test
    public void testModernExperimentStats() {
        var experiment = new FreedomExperiment(4);
        var result = experiment.run();
        var stats = experiment.getStats();
        
        // Test record properties
        Assertions.assertEquals(4, stats.totalPrisoners());
        Assertions.assertEquals(result, stats.allEscaped());
        Assertions.assertTrue(stats.freedPrisoners() >= 0);
        Assertions.assertTrue(stats.freedPrisoners() <= 4);
        
        // Test computed properties
        var expectedRate = (double) stats.freedPrisoners() / 4 * 100.0;
        Assertions.assertEquals(expectedRate, stats.successRate(), 0.001);
        
        // Test toString (text block formatting)
        var statsString = stats.toString();
        Assertions.assertTrue(statsString.contains("Total prisoners: 4"));
        Assertions.assertTrue(statsString.contains("Success rate:"));
    }
    
    @Test
    public void testBoxAccess() {
        var experiment = new FreedomExperiment(6);
        
        // Test that we can access boxes
        for (int i = 1; i <= 6; i++) {
            var box = experiment.getBox(i);
            Assertions.assertNotNull(box);
            Assertions.assertEquals(i, box.label());
        }
    }
    
    @Test
    public void testExperimentWithStepListener() {
        var experiment = new FreedomExperiment(4);
        var stepCount = new int[1]; // Use array for closure
        
        var result = experiment.run((prisoner, box) -> {
            stepCount[0]++;
            Assertions.assertNotNull(prisoner);
            Assertions.assertNotNull(box);
            Assertions.assertTrue(prisoner.number() >= 1);
            Assertions.assertTrue(prisoner.number() <= 4);
        });
        
        // Should have at least some steps
        Assertions.assertTrue(stepCount[0] > 0);
        
        // Verify consistent result
        var stats = experiment.getStats();
        Assertions.assertEquals(result, stats.allEscaped());
    }
}
