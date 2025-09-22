package prisoners;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Modern Java tests for the main application.
 */
public class TestApp {

    @Test
    public void testAppExecution() throws Exception {
        var app = new App();
        var result = app.call();
        Assertions.assertEquals(0, result, "App should return 0 for successful execution");
    }
    
    @Test
    public void testModernValidation() {
        var app = new App();
        
        // Test that the app can handle the default parameters
        Assertions.assertDoesNotThrow(() -> {
            app.call();
        }, "App should run successfully with default parameters");
    }
    
    @Test 
    public void testExperimentResultsRecord() {
        // Test that we can create and use the results record from the modernized experiment
        var experiment = new FreedomExperiment(10);
        var success = experiment.run();
        var stats = experiment.getStats();
        
        Assertions.assertNotNull(stats);
        Assertions.assertEquals(10, stats.totalPrisoners());
        Assertions.assertEquals(success, stats.allEscaped());
        
        if (success) {
            Assertions.assertEquals(10, stats.freedPrisoners());
            Assertions.assertEquals(100.0, stats.successRate());
        }
    }
}
