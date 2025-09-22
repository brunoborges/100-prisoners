package prisoners.gui;

import javax.swing.*;
import java.awt.*;

/**
 * A custom JPanel representing a box in the 100 Prisoners Problem visualization.
 * Shows the box number, hidden number, and visual states during the experiment.
 */
public class BoxPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    // Visual states
    private boolean isCurrentlyOpened = false;
    private boolean isInPath = false;
    private boolean isFoundTarget = false;
    
    // Box properties
    private final int boxNumber;
    private int hiddenNumber = -1;
    
    // Colors
    private static final Color NORMAL_COLOR = Color.LIGHT_GRAY;
    private static final Color CURRENT_COLOR = Color.BLUE;
    private static final Color PATH_COLOR = new Color(173, 216, 230); // Light blue
    private static final Color SUCCESS_COLOR = Color.GREEN;
    private static final Color BORDER_COLOR = Color.DARK_GRAY;
    
    public BoxPanel(int boxNumber) {
        this.boxNumber = boxNumber;
        setPreferredSize(new Dimension(60, 60));
        setMinimumSize(new Dimension(50, 50));
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        setOpaque(true);
        setBackground(NORMAL_COLOR);
        
        // Add tooltip
        setToolTip();
    }
    
    public void setHiddenNumber(int hiddenNumber) {
        this.hiddenNumber = hiddenNumber;
        setToolTip();
        repaint();
    }
    
    public void setCurrentlyOpened(boolean currentlyOpened) {
        this.isCurrentlyOpened = currentlyOpened;
        updateAppearance();
    }
    
    public void setInPath(boolean inPath) {
        this.isInPath = inPath;
        updateAppearance();
    }
    
    public void setFoundTarget(boolean foundTarget) {
        this.isFoundTarget = foundTarget;
        updateAppearance();
    }
    
    public void clearPath() {
        this.isCurrentlyOpened = false;
        this.isInPath = false;
        // Keep isFoundTarget to show successful boxes
        updateAppearance();
    }
    
    public void reset() {
        this.isCurrentlyOpened = false;
        this.isInPath = false;
        this.isFoundTarget = false;
        this.hiddenNumber = -1;
        updateAppearance();
        setToolTip();
    }
    
    private void updateAppearance() {
        Color backgroundColor;
        
        if (isFoundTarget) {
            backgroundColor = SUCCESS_COLOR;
        } else if (isCurrentlyOpened) {
            backgroundColor = CURRENT_COLOR;
        } else if (isInPath) {
            backgroundColor = PATH_COLOR;
        } else {
            backgroundColor = NORMAL_COLOR;
        }
        
        setBackground(backgroundColor);
        repaint();
    }
    
    private void setToolTip() {
        if (hiddenNumber > 0) {
            setToolTipText(String.format("Box %d contains number %d", boxNumber, hiddenNumber));
        } else {
            setToolTipText(String.format("Box %d", boxNumber));
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw box number (always visible)
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        FontMetrics fm = g2d.getFontMetrics();
        
        String boxText = String.valueOf(boxNumber);
        int textWidth = fm.stringWidth(boxText);
        int textHeight = fm.getHeight();
        
        g2d.drawString(boxText, 
            (width - textWidth) / 2, 
            textHeight - 2);
        
        // Draw hidden number if revealed (during experiment)
        if (hiddenNumber > 0 && (isCurrentlyOpened || isInPath || isFoundTarget)) {
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.setColor(isFoundTarget ? Color.WHITE : Color.RED);
            
            String hiddenText = String.valueOf(hiddenNumber);
            FontMetrics hiddenFm = g2d.getFontMetrics();
            int hiddenWidth = hiddenFm.stringWidth(hiddenText);
            int hiddenHeight = hiddenFm.getHeight();
            
            g2d.drawString(hiddenText, 
                (width - hiddenWidth) / 2, 
                height / 2 + hiddenHeight / 4);
        }
        
        // Draw special indicators
        if (isCurrentlyOpened) {
            // Draw a pulsing border for currently opened box
            g2d.setColor(CURRENT_COLOR);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(2, 2, width - 4, height - 4);
        }
        
        if (isFoundTarget) {
            // Draw a checkmark for successful find
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            
            int checkSize = Math.min(width, height) / 4;
            int centerX = width / 2;
            int centerY = height / 2;
            
            // Draw checkmark
            g2d.drawLine(centerX - checkSize/2, centerY, centerX - checkSize/4, centerY + checkSize/2);
            g2d.drawLine(centerX - checkSize/4, centerY + checkSize/2, centerX + checkSize/2, centerY - checkSize/2);
        }
        
        g2d.dispose();
    }
    
    public int getBoxNumber() {
        return boxNumber;
    }
    
    public int getHiddenNumber() {
        return hiddenNumber;
    }
}