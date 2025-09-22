package prisoners.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Modern progress bar with smooth animations and stylish appearance.
 */
public final class ModernProgressBar extends JProgressBar {
    
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color DEFAULT_FOREGROUND = new Color(52, 152, 219);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    
    private float animatedValue = 0.0f;
    private Timer animationTimer;
    
    public ModernProgressBar() {
        setupProgressBar();
        setupAnimation();
    }
    
    private void setupProgressBar() {
        setOpaque(false);
        setBorderPainted(false);
        setStringPainted(true);
        
        // Modern typography
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setForeground(DEFAULT_FOREGROUND);
        
        // Padding
        setBorder(new EmptyBorder(4, 0, 4, 0));
        
        // Custom UI
        setUI(new ModernProgressBarUI());
    }
    
    private void setupAnimation() {
        animationTimer = new Timer(16, e -> {
            float target = getValue();
            float diff = target - animatedValue;
            if (Math.abs(diff) > 0.1f) {
                animatedValue += diff * 0.1f;
                repaint();
            } else {
                animatedValue = target;
                animationTimer.stop();
            }
        });
    }
    
    @Override
    public void setValue(int value) {
        super.setValue(value);
        if (!animationTimer.isRunning()) {
            animationTimer.start();
        }
    }
    
    private class ModernProgressBarUI extends javax.swing.plaf.basic.BasicProgressBarUI {
        @Override
        protected void paintDeterminate(Graphics g, JComponent c) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Insets insets = progressBar.getInsets();
            int width = progressBar.getWidth() - insets.left - insets.right;
            int height = progressBar.getHeight() - insets.top - insets.bottom;
            
            // Paint background
            g2d.setColor(BACKGROUND_COLOR);
            g2d.fillRoundRect(insets.left, insets.top, width, height, height/2, height/2);
            
            // Paint progress
            if (animatedValue > 0) {
                int progressWidth = (int) (width * (animatedValue / progressBar.getMaximum()));
                
                GradientPaint gradient = new GradientPaint(
                    insets.left, insets.top, progressBar.getForeground(),
                    insets.left, insets.top + height, progressBar.getForeground().darker()
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(insets.left, insets.top, progressWidth, height, height/2, height/2);
                
                // Add shine effect
                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.fillRoundRect(insets.left, insets.top, progressWidth, height/3, height/2, height/2);
            }
            
            g2d.dispose();
        }
        
        @Override
        protected void paintString(Graphics g, int x, int y, int width, int height, int amountFull, Insets b) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            String progressString = progressBar.getString();
            g2d.setFont(progressBar.getFont());
            g2d.setColor(TEXT_COLOR);
            
            FontMetrics fm = g2d.getFontMetrics();
            int stringWidth = fm.stringWidth(progressString);
            int stringHeight = fm.getHeight();
            
            g2d.drawString(progressString, 
                x + (width - stringWidth) / 2,
                y + (height + stringHeight / 2) / 2);
        }
    }
}