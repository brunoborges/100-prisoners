package prisoners.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern, visually appealing box component with smooth animations and hover effects.
 */
public final class ModernBoxPanel extends JPanel {
    
    // Visual state record for type safety and immutability
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
    private boolean isHovered = false;
    private float animationProgress = 0.0f;
    
    // Modern color palette with better contrast and accessibility
    private static final Color NORMAL_COLOR = new Color(255, 255, 255);        // Pure White
    private static final Color HOVER_COLOR = new Color(245, 247, 249);         // Light Gray
    private static final Color CURRENT_COLOR = new Color(52, 152, 219);        // Vibrant Blue
    private static final Color PATH_COLOR = new Color(174, 213, 255);          // Light Blue
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);        // Emerald Green
    private static final Color BORDER_COLOR = new Color(220, 221, 225);        // Soft Gray
    private static final Color TEXT_COLOR = new Color(44, 62, 80);             // Dark Slate
    private static final Color ACCENT_COLOR = new Color(142, 68, 173);         // Purple Accent
    
    // Modern typography
    private static final Font BOX_FONT = new Font("SF Pro Display", Font.BOLD, 11);
    private static final Font HIDDEN_FONT = new Font("SF Pro Display", Font.BOLD, 14);
    
    public ModernBoxPanel(int boxNumber) {
        this.boxNumber = boxNumber;
        setupComponent();
        setupMouseInteractions();
        updateToolTip();
    }
    
    private void setupComponent() {
        setPreferredSize(new Dimension(70, 70));
        setMinimumSize(new Dimension(60, 60));
        setOpaque(false); // For custom painting
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add subtle border with rounded corners effect
        setBorder(new EmptyBorder(2, 2, 2, 2));
    }
    
    private void setupMouseInteractions() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!state.isCurrentlyOpened()) {
                    isHovered = true;
                    repaint();
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }
    
    public void setHiddenNumber(int hiddenNumber) {
        this.hiddenNumber = hiddenNumber;
        updateToolTip();
        repaint();
    }
    
    public void setCurrentlyOpened(boolean currentlyOpened) {
        state = new VisualState(currentlyOpened, state.isInPath(), state.isFoundTarget());
        isHovered = false; // Reset hover state when opened
        animateStateChange();
        updateAppearance();
    }
    
    public void setInPath(boolean inPath) {
        state = new VisualState(state.isCurrentlyOpened(), inPath, state.isFoundTarget());
        updateAppearance();
    }
    
    public void setFoundTarget(boolean foundTarget) {
        state = new VisualState(state.isCurrentlyOpened(), state.isInPath(), foundTarget);
        if (foundTarget) {
            animateSuccess();
        }
        updateAppearance();
    }
    
    public void clearPath() {
        state = new VisualState(false, false, state.isFoundTarget());
        isHovered = false;
        updateAppearance();
    }
    
    public void reset() {
        state = VisualState.NORMAL;
        hiddenNumber = -1;
        isHovered = false;
        animationProgress = 0.0f;
        updateAppearance();
        updateToolTip();
    }
    
    private void updateAppearance() {
        repaint();
    }
    
    private void updateToolTip() {
        var tooltipText = hiddenNumber > 0 ? 
            String.format("<html><b>Box %d</b><br/>Contains: <span style='color: #e74c3c;'>%d</span></html>", boxNumber, hiddenNumber) :
            String.format("<html><b>Box %d</b><br/>Hidden number unknown</html>", boxNumber);
        setToolTipText(tooltipText);
    }
    
    private void animateStateChange() {
        // Simple animation trigger - in a real app you'd use a Timer
        animationProgress = 1.0f;
        Timer fadeTimer = new Timer(50, e -> {
            animationProgress = Math.max(0.0f, animationProgress - 0.1f);
            repaint();
            if (animationProgress <= 0.0f) {
                ((Timer) e.getSource()).stop();
            }
        });
        fadeTimer.start();
    }
    
    private void animateSuccess() {
        // Success animation with bounce effect
        Timer bounceTimer = new Timer(30, null);
        final int[] step = {0};
        final int maxSteps = 20;
        
        bounceTimer.addActionListener(e -> {
            step[0]++;
            double progress = (double) step[0] / maxSteps;
            animationProgress = (float) (Math.sin(progress * Math.PI * 2) * 0.3 + 0.7);
            repaint();
            
            if (step[0] >= maxSteps) {
                animationProgress = 1.0f;
                bounceTimer.stop();
            }
        });
        bounceTimer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        // Calculate animation scaling
        float scale = 1.0f + (animationProgress * 0.1f);
        int scaledWidth = (int) (width * scale);
        int scaledHeight = (int) (height * scale);
        int offsetX = (width - scaledWidth) / 2;
        int offsetY = (height - scaledHeight) / 2;
        
        // Paint shadow first
        paintShadow(g2d, offsetX, offsetY, scaledWidth, scaledHeight);
        
        // Paint box background
        paintBackground(g2d, offsetX, offsetY, scaledWidth, scaledHeight);
        
        // Paint border
        paintBorder(g2d, offsetX, offsetY, scaledWidth, scaledHeight);
        
        // Paint content
        paintContent(g2d, width, height);
        
        // Paint state indicators
        paintStateIndicators(g2d, offsetX, offsetY, scaledWidth, scaledHeight);
        
        g2d.dispose();
    }
    
    private void paintShadow(Graphics2D g2d, int x, int y, int width, int height) {
        // Subtle drop shadow for depth
        g2d.setColor(new Color(0, 0, 0, isHovered ? 30 : 15));
        g2d.fillRoundRect(x + 2, y + 2, width, height, 8, 8);
    }
    
    private void paintBackground(Graphics2D g2d, int x, int y, int width, int height) {
        Color backgroundColor = getBackgroundColor();
        
        // Gradient background for depth
        GradientPaint gradient = new GradientPaint(
            x, y, backgroundColor,
            x, y + height, backgroundColor.darker()
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(x, y, width, height, 8, 8);
    }
    
    private Color getBackgroundColor() {
        if (state.isFoundTarget()) {
            return SUCCESS_COLOR;
        } else if (state.isCurrentlyOpened()) {
            return CURRENT_COLOR;
        } else if (state.isInPath()) {
            return PATH_COLOR;
        } else if (isHovered) {
            return HOVER_COLOR;
        } else {
            return NORMAL_COLOR;
        }
    }
    
    private void paintBorder(Graphics2D g2d, int x, int y, int width, int height) {
        Color borderColor = state.isCurrentlyOpened() ? CURRENT_COLOR.darker() : BORDER_COLOR;
        int borderWidth = state.isCurrentlyOpened() ? 2 : 1;
        
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(borderWidth));
        g2d.drawRoundRect(x, y, width - 1, height - 1, 8, 8);
    }
    
    private void paintContent(Graphics2D g2d, int width, int height) {
        // Box number (always visible)
        g2d.setColor(getTextColor());
        g2d.setFont(BOX_FONT);
        FontMetrics fm = g2d.getFontMetrics();
        
        String boxText = String.valueOf(boxNumber);
        int textWidth = fm.stringWidth(boxText);
        int textHeight = fm.getHeight();
        
        g2d.drawString(boxText, 
            (width - textWidth) / 2, 
            textHeight - 2);
        
        // Hidden number (revealed during experiment)
        if (shouldShowHidden()) {
            g2d.setFont(HIDDEN_FONT);
            g2d.setColor(getHiddenTextColor());
            
            String hiddenText = String.valueOf(hiddenNumber);
            FontMetrics hiddenFm = g2d.getFontMetrics();
            int hiddenWidth = hiddenFm.stringWidth(hiddenText);
            int hiddenHeight = hiddenFm.getHeight();
            
            g2d.drawString(hiddenText, 
                (width - hiddenWidth) / 2, 
                height / 2 + hiddenHeight / 4);
        }
    }
    
    private boolean shouldShowHidden() {
        return hiddenNumber > 0 && (state.isCurrentlyOpened() || state.isInPath() || state.isFoundTarget());
    }
    
    private Color getTextColor() {
        if (state.isFoundTarget()) {
            return Color.WHITE;
        } else if (state.isCurrentlyOpened() || state.isInPath()) {
            return Color.WHITE;
        } else {
            return TEXT_COLOR;
        }
    }
    
    private Color getHiddenTextColor() {
        if (state.isFoundTarget()) {
            return Color.WHITE;
        } else {
            return ACCENT_COLOR;
        }
    }
    
    private void paintStateIndicators(Graphics2D g2d, int x, int y, int width, int height) {
        if (state.isCurrentlyOpened()) {
            // Pulsing border effect
            g2d.setColor(new Color(CURRENT_COLOR.getRed(), CURRENT_COLOR.getGreen(), CURRENT_COLOR.getBlue(), 100));
            g2d.setStroke(new BasicStroke(4));
            g2d.drawRoundRect(x - 2, y - 2, width + 4, height + 4, 12, 12);
        }
        
        if (state.isFoundTarget()) {
            // Success checkmark with glow
            paintSuccessIndicator(g2d, x, y, width, height);
        }
    }
    
    private void paintSuccessIndicator(Graphics2D g2d, int x, int y, int width, int height) {
        // Glowing checkmark
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        int checkSize = Math.min(width, height) / 4;
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        
        // Draw modern checkmark
        g2d.drawLine(centerX - checkSize/2, centerY, 
                    centerX - checkSize/4, centerY + checkSize/2);
        g2d.drawLine(centerX - checkSize/4, centerY + checkSize/2, 
                    centerX + checkSize/2, centerY - checkSize/2);
        
        // Add glow effect
        g2d.setColor(new Color(255, 255, 255, 50));
        g2d.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
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