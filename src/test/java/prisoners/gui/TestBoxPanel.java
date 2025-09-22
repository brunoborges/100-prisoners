package prisoners.gui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import javax.swing.*;

public class TestBoxPanel {

    @Test
    public void testBoxPanelCreation() {
        BoxPanel boxPanel = new BoxPanel(1);
        Assertions.assertEquals(1, boxPanel.getBoxNumber());
        Assertions.assertEquals(-1, boxPanel.getHiddenNumber());
    }

    @Test
    public void testSetHiddenNumber() {
        BoxPanel boxPanel = new BoxPanel(5);
        boxPanel.setHiddenNumber(42);
        Assertions.assertEquals(42, boxPanel.getHiddenNumber());
    }

    @Test
    public void testReset() {
        BoxPanel boxPanel = new BoxPanel(10);
        boxPanel.setHiddenNumber(25);
        boxPanel.setFoundTarget(true);
        
        boxPanel.reset();
        
        Assertions.assertEquals(-1, boxPanel.getHiddenNumber());
        // Visual state should be reset (can't directly test private fields, but no exception should occur)
    }
    
    @Test
    public void testVisualizationAppInstantiation() {
        // This test runs in headless mode, so we just test that the class can be instantiated
        // without throwing exceptions during initialization
        Assertions.assertDoesNotThrow(() -> {
            System.setProperty("java.awt.headless", "true");
            // Just test that the class loads without errors
            Class.forName("prisoners.gui.PrisonersVisualizationApp");
        });
    }
}