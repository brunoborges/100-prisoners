package prisoners.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Animated statistics panel with modern design and smooth value transitions.
 */
public final class AnimatedStatsPanel extends JPanel {
    
    private final String title;
    private String currentValue = "0";
    private final Color accentColor;
    
    private JLabel titleLabel;
    private JLabel valueLabel;
    
    // Animation properties
    private Timer pulseTimer;
    private float pulsePhase = 0.0f;
    private boolean isHighlighted = false;
    
    public AnimatedStatsPanel(String title, String initialValue, Color accentColor) {
        this.title = title;
        this.currentValue = initialValue;
        this.accentColor = accentColor;
        
        setupPanel();
        createLabels();
        setupAnimation();
    }
    
    private void setupPanel() {
        setLayout(new BorderLayout(0, 8));
        setOpaque(false);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Add subtle background
        setBackground(new Color(255, 255, 255, 50));
    }
    
    private void createLabels() {
        titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(127, 140, 141));
        
        valueLabel = new JLabel(currentValue, SwingConstants.CENTER);
        valueLabel.setFont(new Font("SF Mono", Font.BOLD, 20));
        valueLabel.setForeground(accentColor);
        
        add(titleLabel, BorderLayout.NORTH);
        add(valueLabel, BorderLayout.CENTER);
    }
    
    private void setupAnimation() {
        pulseTimer = new Timer(50, e -> {
            pulsePhase += 0.2f;
            if (pulsePhase > Math.PI * 2) {
                pulsePhase = 0.0f;
                if (!isHighlighted) {
                    pulseTimer.stop();
                }
            }
            repaint();
        });
    }
    
    public void updateValue(String newValue) {
        if (!currentValue.equals(newValue)) {
            currentValue = newValue;
            valueLabel.setText(newValue);
            
            // Trigger highlight animation
            highlightUpdate();
        }
    }
    
    private void highlightUpdate() {
        isHighlighted = true;
        pulsePhase = 0.0f;
        pulseTimer.start();
        
        // Stop highlighting after a few pulses
        Timer stopTimer = new Timer(1500, e -> {
            isHighlighted = false;
            ((Timer) e.getSource()).stop();
        });
        stopTimer.setRepeats(false);
        stopTimer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Paint background with subtle gradient
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(255, 255, 255, 20),
            0, height, new Color(255, 255, 255, 5)
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, width, height, 8, 8);
        
        // Paint pulse effect during updates
        if (pulseTimer.isRunning()) {
            float alpha = (float) Math.abs(Math.sin(pulsePhase)) * 0.3f;
            g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), (int)(alpha * 255)));
            g2d.fillRoundRect(0, 0, width, height, 8, 8);
        }
        
        // Paint border
        g2d.setColor(new Color(220, 221, 225));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(0, 0, width - 1, height - 1, 8, 8);
        
        g2d.dispose();
        super.paintComponent(g);
    }
}