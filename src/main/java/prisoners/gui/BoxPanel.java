package prisoners.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Modern Java implementation of a box visualization component.
 * Enhanced with records, modern syntax, and stable language features.
 */
public final class BoxPanel extends JPanel {
    
    // Visual state record for type safety
    public record VisualState(
        boolean isCurrentlyOpened,
        boolean isInPath,
        boolean isFoundTarget
    ) {
        public static final VisualState NORMAL = new VisualState(false, false, false);
    }
    
    // Box properties
    private final int boxNumber;
    private int hiddenNumber = -1;
    private VisualState state = VisualState.NORMAL;
    
    // Modern color palette with better accessibility
    private static final Color NORMAL_COLOR = new Color(240, 240, 240);
    private static final Color CURRENT_COLOR = new Color(52, 152, 219);  // Modern blue
    private static final Color PATH_COLOR = new Color(174, 213, 255);     // Light blue
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);   // Modern green
    private static final Color BORDER_COLOR = new Color(149, 165, 166);   // Soft gray
    
    public BoxPanel(int boxNumber) {
        this.boxNumber = boxNumber;
        setPreferredSize(new Dimension(60, 60));
        setMinimumSize(new Dimension(50, 50));
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        setOpaque(true);
        setBackground(NORMAL_COLOR);
        updateToolTip();
    }
    
    public void setHiddenNumber(int hiddenNumber) {
        this.hiddenNumber = hiddenNumber;
        updateToolTip();
        repaint();
    }
    
    public void setCurrentlyOpened(boolean currentlyOpened) {
        state = new VisualState(currentlyOpened, state.isInPath(), state.isFoundTarget());
        updateAppearance();
    }
    
    public void setInPath(boolean inPath) {
        state = new VisualState(state.isCurrentlyOpened(), inPath, state.isFoundTarget());
        updateAppearance();
    }
    
    public void setFoundTarget(boolean foundTarget) {
        state = new VisualState(state.isCurrentlyOpened(), state.isInPath(), foundTarget);
        updateAppearance();
    }
    
    public void clearPath() {
        state = new VisualState(false, false, state.isFoundTarget());
        updateAppearance();
    }
    
    public void reset() {
        state = VisualState.NORMAL;
        hiddenNumber = -1;
        updateAppearance();
        updateToolTip();
    }
    
    /**
     * Modern color selection using enhanced logic.
     */
    private void updateAppearance() {
        Color backgroundColor;
        if (state.isFoundTarget()) {
            backgroundColor = SUCCESS_COLOR;
        } else if (state.isCurrentlyOpened()) {
            backgroundColor = CURRENT_COLOR;
        } else if (state.isInPath()) {
            backgroundColor = PATH_COLOR;
        } else {
            backgroundColor = NORMAL_COLOR;
        }
        
        setBackground(backgroundColor);
        repaint();
    }
    
    private void updateToolTip() {
        var tooltipText = hiddenNumber > 0 ? 
            String.format("Box %d contains number %d", boxNumber, hiddenNumber) :
            String.format("Box %d", boxNumber);
        setToolTipText(tooltipText);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (g instanceof Graphics2D g2d) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            paintBoxContents(g2d);
            paintStateIndicators(g2d);
        }
    }
    
    /**
     * Paint box contents using modern drawing techniques.
     */
    private void paintBoxContents(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        
        // Draw box number (always visible)
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
        var fm = g2d.getFontMetrics();
        
        var boxText = String.valueOf(boxNumber);
        int textWidth = fm.stringWidth(boxText);
        int textHeight = fm.getHeight();
        
        g2d.drawString(boxText, (width - textWidth) / 2, textHeight - 2);
        
        // Draw hidden number if revealed
        boolean shouldShowHidden = (state.isCurrentlyOpened() || state.isInPath() || state.isFoundTarget()) && hiddenNumber > 0;
        
        if (shouldShowHidden) {
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
            var hiddenColor = state.isFoundTarget() ? Color.WHITE : new Color(231, 76, 60); // Modern red
            g2d.setColor(hiddenColor);
            
            var hiddenText = String.valueOf(hiddenNumber);
            var hiddenFm = g2d.getFontMetrics();
            int hiddenWidth = hiddenFm.stringWidth(hiddenText);
            int hiddenHeight = hiddenFm.getHeight();
            
            g2d.drawString(hiddenText, (width - hiddenWidth) / 2, height / 2 + hiddenHeight / 4);
        }
    }
    
    /**
     * Paint visual state indicators using pattern matching.
     */
    private void paintStateIndicators(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        
        // Draw indicators based on state
        if (state.isCurrentlyOpened()) {
            // Animated border for currently opened box
            g2d.setColor(CURRENT_COLOR);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(2, 2, width - 4, height - 4);
        } else if (state.isFoundTarget()) {
            // Modern checkmark for success
            drawCheckmark(g2d, width, height);
        }
    }
    
    /**
     * Draw a modern checkmark using enhanced graphics.
     */
    private void drawCheckmark(Graphics2D g2d, int width, int height) {
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        int checkSize = Math.min(width, height) / 4;
        int centerX = width / 2;
        int centerY = height / 2;
        
        // Draw modern checkmark with smooth curves
        g2d.drawLine(centerX - checkSize/2, centerY, 
                    centerX - checkSize/4, centerY + checkSize/2);
        g2d.drawLine(centerX - checkSize/4, centerY + checkSize/2, 
                    centerX + checkSize/2, centerY - checkSize/2);
    }
    
    public int getBoxNumber() {
        return boxNumber;
    }
    
    public int getHiddenNumber() {
        return hiddenNumber;
    }
    
    public VisualState getState() {
        return state;
    }
}