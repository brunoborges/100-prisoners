package prisoners.gui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import javax.swing.*;

/**
 * Modern Java tests for GUI components with enhanced modern UI.
 */
public class TestBoxPanel {

    @Test
    public void testModernBoxPanelCreation() {
        var boxPanel = new ModernBoxPanel(1);
        Assertions.assertEquals(1, boxPanel.getBoxNumber());
        Assertions.assertEquals(-1, boxPanel.getHiddenNumber());
        Assertions.assertEquals(ModernBoxPanel.VisualState.NORMAL, boxPanel.getState());
    }

    @Test
    public void testSetHiddenNumber() {
        var boxPanel = new ModernBoxPanel(5);
        boxPanel.setHiddenNumber(42);
        Assertions.assertEquals(42, boxPanel.getHiddenNumber());
    }

    @Test
    public void testModernVisualStateTransitions() {
        var boxPanel = new ModernBoxPanel(10);
        
        // Test state changes using modern assertions
        boxPanel.setFoundTarget(true);
        Assertions.assertTrue(boxPanel.getState().isFoundTarget());
        
        boxPanel.setInPath(true);
        Assertions.assertTrue(boxPanel.getState().isInPath());
        Assertions.assertTrue(boxPanel.getState().isFoundTarget()); // Should remain true
        
        boxPanel.clearPath();
        Assertions.assertFalse(boxPanel.getState().isInPath());
        Assertions.assertTrue(boxPanel.getState().isFoundTarget()); // Should remain true
    }

    @Test
    public void testReset() {
        var boxPanel = new ModernBoxPanel(10);
        boxPanel.setHiddenNumber(25);
        boxPanel.setFoundTarget(true);
        
        boxPanel.reset();
        
        Assertions.assertEquals(-1, boxPanel.getHiddenNumber());
        Assertions.assertEquals(ModernBoxPanel.VisualState.NORMAL, boxPanel.getState());
    }
    
    @Test
    public void testModernVisualizationAppInstantiation() {
        // Test enhanced for modern Java stable features
        Assertions.assertDoesNotThrow(() -> {
            System.setProperty("java.awt.headless", "true");
            // Test modern class loading and record usage
            var className = "prisoners.gui.PrisonersVisualizationApp";
            var clazz = Class.forName(className);
            Assertions.assertNotNull(clazz);
            
            // Test that UIState record exists and works
            var uiStateClass = Class.forName("prisoners.gui.PrisonersVisualizationApp$UIState");
            Assertions.assertNotNull(uiStateClass);
        });
    }
    
    @Test
    public void testModernVisualStateRecord() {
        // Test the modern record implementation
        var normalState = ModernBoxPanel.VisualState.NORMAL;
        var activeState = new ModernBoxPanel.VisualState(true, false, false);
        var successState = new ModernBoxPanel.VisualState(false, false, true);
        
        // Test record equality and properties
        Assertions.assertFalse(normalState.isCurrentlyOpened());
        Assertions.assertTrue(activeState.isCurrentlyOpened());
        Assertions.assertTrue(successState.isFoundTarget());
        
        // Test record immutability
        Assertions.assertNotEquals(normalState, activeState);
        Assertions.assertNotEquals(activeState, successState);
    }
    
    @Test
    public void testModernButtonCreation() {
        // Test modern button component
        Assertions.assertDoesNotThrow(() -> {
            System.setProperty("java.awt.headless", "true");
            var button = new ModernButton("Test Button", java.awt.Color.BLUE);
            Assertions.assertNotNull(button);
            Assertions.assertEquals("Test Button", button.getText());
        });
    }
    
    @Test
    public void testAnimatedStatsPanel() {
        // Test animated stats panel
        Assertions.assertDoesNotThrow(() -> {
            System.setProperty("java.awt.headless", "true");
            var statsPanel = new AnimatedStatsPanel("Test Stat", "100%", java.awt.Color.GREEN);
            Assertions.assertNotNull(statsPanel);
            
            // Test value updates
            statsPanel.updateValue("75%");
            // Should not throw exceptions during animation setup
        });
    }
}