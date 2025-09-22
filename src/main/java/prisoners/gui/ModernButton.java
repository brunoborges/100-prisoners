package prisoners.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern, stylish button component with hover effects and smooth animations.
 */
public final class ModernButton extends JButton {
    
    private final Color baseColor;
    private final Color hoverColor;
    private final Color pressedColor;
    private boolean isHovered = false;
    private boolean isPressed = false;
    
    // Modern color variations
    private static final Color WHITE_TEXT = Color.WHITE;
    private static final Color DARK_TEXT = new Color(44, 62, 80);
    
    public ModernButton(String text, Color baseColor) {
        super(text);
        this.baseColor = baseColor;
        this.hoverColor = brighten(baseColor, 20);
        this.pressedColor = darken(baseColor, 20);
        
        setupButton();
        setupMouseInteractions();
    }
    
    private void setupButton() {
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        
        // Modern typography
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(isLightColor(baseColor) ? DARK_TEXT : WHITE_TEXT);
        
        // Padding for better touch targets
        setBorder(new EmptyBorder(12, 24, 12, 24));
        
        // Cursor feedback
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void setupMouseInteractions() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = true;
                    repaint();
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    isPressed = true;
                    repaint();
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Paint shadow
        if (isEnabled() && !isPressed) {
            g2d.setColor(new Color(0, 0, 0, isHovered ? 40 : 20));
            g2d.fillRoundRect(2, 2, width - 2, height - 2, 8, 8);
        }
        
        // Paint background
        Color bgColor = getBackgroundColor();
        GradientPaint gradient = new GradientPaint(
            0, 0, bgColor,
            0, height, bgColor.darker()
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, width - (isPressed ? 0 : 2), height - (isPressed ? 0 : 2), 8, 8);
        
        // Paint text
        super.paintComponent(g);
        
        g2d.dispose();
    }
    
    private Color getBackgroundColor() {
        if (!isEnabled()) {
            return new Color(189, 195, 199);
        } else if (isPressed) {
            return pressedColor;
        } else if (isHovered) {
            return hoverColor;
        } else {
            return baseColor;
        }
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setForeground(enabled ? 
            (isLightColor(baseColor) ? DARK_TEXT : WHITE_TEXT) : 
            new Color(127, 140, 141));
        repaint();
    }
    
    private static Color brighten(Color color, int amount) {
        return new Color(
            Math.min(255, color.getRed() + amount),
            Math.min(255, color.getGreen() + amount),
            Math.min(255, color.getBlue() + amount)
        );
    }
    
    private static Color darken(Color color, int amount) {
        return new Color(
            Math.max(0, color.getRed() - amount),
            Math.max(0, color.getGreen() - amount),
            Math.max(0, color.getBlue() - amount)
        );
    }
    
    private static boolean isLightColor(Color color) {
        double luminance = 0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue();
        return luminance > 128;
    }
}