package prisoners.gui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import javax.swing.*;

/**
 * Modern Java tests for GUI components.
 */
public class TestBoxPanel {

    @Test
    public void testBoxPanelCreation() {
        var boxPanel = new BoxPanel(1);
        Assertions.assertEquals(1, boxPanel.getBoxNumber());
        Assertions.assertEquals(-1, boxPanel.getHiddenNumber());
        Assertions.assertEquals(BoxPanel.VisualState.NORMAL, boxPanel.getState());
    }

    @Test
    public void testSetHiddenNumber() {
        var boxPanel = new BoxPanel(5);
        boxPanel.setHiddenNumber(42);
        Assertions.assertEquals(42, boxPanel.getHiddenNumber());
    }

    @Test
    public void testVisualStateTransitions() {
        var boxPanel = new BoxPanel(10);
        
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
        var boxPanel = new BoxPanel(10);
        boxPanel.setHiddenNumber(25);
        boxPanel.setFoundTarget(true);
        
        boxPanel.reset();
        
        Assertions.assertEquals(-1, boxPanel.getHiddenNumber());
        Assertions.assertEquals(BoxPanel.VisualState.NORMAL, boxPanel.getState());
    }
    
    @Test
    public void testVisualizationAppInstantiation() {
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
    public void testVisualStateRecord() {
        // Test the modern record implementation
        var normalState = BoxPanel.VisualState.NORMAL;
        var activeState = new BoxPanel.VisualState(true, false, false);
        var successState = new BoxPanel.VisualState(false, false, true);
        
        // Test record equality and properties
        Assertions.assertFalse(normalState.isCurrentlyOpened());
        Assertions.assertTrue(activeState.isCurrentlyOpened());
        Assertions.assertTrue(successState.isFoundTarget());
        
        // Test record immutability
        Assertions.assertNotEquals(normalState, activeState);
        Assertions.assertNotEquals(activeState, successState);
    }
}