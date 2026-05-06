package prisoners.gui;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.*;

/**
 * Transparent overlay panel that animates a prisoner icon moving between boxes.
 * Uses a glass pane approach to render the prisoner on top of the box grid.
 */
public final class PrisonerAnimationLayer extends JPanel {

    private static final int PRISONER_SIZE = 40;
    private static final int TIMER_INTERVAL_MS = 10; // ~100fps for smooth animation

    private final Image prisonerImage;
    private boolean animationEnabled = true;
    private int animationDurationMs = 300;
    private boolean visible3D = false;

    // Current position of the prisoner (in parent coordinates)
    private double currentX = -100;
    private double currentY = -100;
    private double targetX = -100;
    private double targetY = -100;
    private int animationStep = 0;
    private int currentTotalSteps = 15;
    private boolean animating = false;
    private Timer animationTimer;

    // Reference to the box grid for coordinate mapping
    private JPanel boxGridPanel;
    private ModernBoxPanel[] boxPanels;

    public PrisonerAnimationLayer() {
        setOpaque(false);
        prisonerImage = loadPrisonerImage();
    }

    public void setBoxGrid(JPanel boxGridPanel, ModernBoxPanel[] boxPanels) {
        this.boxGridPanel = boxGridPanel;
        this.boxPanels = boxPanels;
    }

    public void setAnimationEnabled(boolean enabled) {
        this.animationEnabled = enabled;
        if (!enabled) {
            currentX = -100;
            currentY = -100;
            repaint();
        }
    }

    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    /**
     * Sets the animation duration for prisoner movement.
     * This is dynamically read so speed changes apply immediately.
     */
    public void setAnimationDurationMs(int durationMs) {
        this.animationDurationMs = Math.max(30, durationMs);
    }

    /**
     * Moves the prisoner to the specified box with smooth animation.
     * Calls the callback when the animation completes.
     */
    public void moveTo(int boxIndex, Runnable onComplete) {
        if (!animationEnabled || boxPanels == null || boxGridPanel == null) {
            if (onComplete != null) onComplete.run();
            return;
        }

        if (boxIndex < 0 || boxIndex >= boxPanels.length) {
            if (onComplete != null) onComplete.run();
            return;
        }

        // Get the target box position in this layer's coordinate space
        var boxPanel = boxPanels[boxIndex];
        Point boxLocation = SwingUtilities.convertPoint(
            boxPanel, boxPanel.getWidth() / 2, 0, this
        );

        targetX = boxLocation.x - PRISONER_SIZE / 2.0;
        targetY = boxLocation.y - PRISONER_SIZE - 5;

        // If prisoner is off-screen (first move), teleport
        if (currentX < -50) {
            currentX = targetX;
            currentY = targetY;
            repaint();
            if (onComplete != null) onComplete.run();
            return;
        }

        // Animate movement
        animationStep = 0;
        animating = true;
        double startX = currentX;
        double startY = currentY;
        int totalSteps = Math.max(2, animationDurationMs / TIMER_INTERVAL_MS);
        currentTotalSteps = totalSteps;

        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        animationTimer = new Timer(TIMER_INTERVAL_MS, e -> {
            animationStep++;
            // Ease-in-out interpolation
            double t = (double) animationStep / totalSteps;
            double ease = t < 0.5 ? 2 * t * t : 1 - Math.pow(-2 * t + 2, 2) / 2;

            currentX = startX + (targetX - startX) * ease;
            currentY = startY + (targetY - startY) * ease;

            repaint();

            if (animationStep >= totalSteps) {
                animating = false;
                ((Timer) e.getSource()).stop();
                currentX = targetX;
                currentY = targetY;
                repaint();
                if (onComplete != null) onComplete.run();
            }
        });
        animationTimer.start();
    }

    /**
     * Resets the prisoner position (hides the icon).
     */
    public void resetPosition() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        currentX = -100;
        currentY = -100;
        animating = false;
        repaint();
    }

    /**
     * Immediately finishes any in-progress animation, snapping to the end position.
     */
    public void finishImmediately() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        if (animating) {
            currentX = targetX;
            currentY = targetY;
            animating = false;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!animationEnabled || currentX < -50) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int drawX = (int) currentX;
        int drawY = (int) currentY;

        // Add a subtle bounce when moving
        if (animating) {
            double bounceOffset = Math.sin((double) animationStep / currentTotalSteps * Math.PI) * 5;
            drawY -= (int) bounceOffset;
        }

        // Draw prisoner shadow
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillOval(drawX + 5, drawY + PRISONER_SIZE - 5, PRISONER_SIZE - 10, 8);

        // Draw prisoner image
        if (prisonerImage != null) {
            g2d.drawImage(prisonerImage, drawX, drawY, PRISONER_SIZE, PRISONER_SIZE, null);
        } else {
            // Fallback: draw a simple prisoner figure
            drawFallbackPrisoner(g2d, drawX, drawY);
        }

        g2d.dispose();
    }

    private void drawFallbackPrisoner(Graphics2D g2d, int x, int y) {
        int size = PRISONER_SIZE;
        // Head
        g2d.setColor(new Color(232, 184, 138));
        g2d.fillOval(x + size / 4, y, size / 2, size / 2);
        // Body with stripes
        g2d.setColor(new Color(44, 62, 80));
        g2d.fillRoundRect(x + size / 6, y + size / 2, size * 2 / 3, size / 2, 4, 4);
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < 3; i++) {
            g2d.fillRect(x + size / 6, y + size / 2 + 4 + i * 6, size * 2 / 3, 2);
        }
    }

    private Image loadPrisonerImage() {
        try {
            // Load SVG as a rendered BufferedImage
            InputStream svgStream = getClass().getResourceAsStream("/icons/prisoner.svg");
            if (svgStream == null) {
                return null;
            }
            svgStream.close();

            // Since Java doesn't natively render SVG, we'll use the fallback drawn version
            // but render it to a BufferedImage for better performance
            BufferedImage img = new BufferedImage(PRISONER_SIZE, PRISONER_SIZE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawCartoonPrisoner(g2d, 0, 0, PRISONER_SIZE);
            g2d.dispose();
            return img;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Draws a cartoon prisoner programmatically (since Java can't render SVG natively).
     */
    private void drawCartoonPrisoner(Graphics2D g2d, int x, int y, int size) {
        double scale = size / 64.0;
        AffineTransform oldTransform = g2d.getTransform();
        g2d.translate(x, y);
        g2d.scale(scale, scale);

        // Legs
        g2d.setColor(new Color(44, 62, 80));
        g2d.fillRoundRect(22, 54, 8, 8, 2, 2);
        g2d.fillRoundRect(34, 54, 8, 8, 2, 2);

        // Shoes
        g2d.setColor(new Color(74, 74, 74));
        g2d.fillRoundRect(21, 60, 10, 4, 2, 2);
        g2d.fillRoundRect(33, 60, 10, 4, 2, 2);

        // Body
        g2d.setColor(new Color(245, 245, 245));
        g2d.fillRoundRect(18, 30, 28, 24, 8, 8);

        // Stripes
        g2d.setColor(new Color(44, 62, 80));
        g2d.fillRect(18, 34, 28, 4);
        g2d.fillRect(18, 42, 28, 4);
        g2d.fillRect(18, 50, 28, 4);

        // Arms
        g2d.setColor(new Color(245, 245, 245));
        g2d.fillRoundRect(12, 32, 8, 18, 4, 4);
        g2d.fillRoundRect(44, 32, 8, 18, 4, 4);
        g2d.setColor(new Color(44, 62, 80));
        g2d.fillRect(12, 36, 8, 4);
        g2d.fillRect(12, 44, 8, 4);
        g2d.fillRect(44, 36, 8, 4);
        g2d.fillRect(44, 44, 8, 4);

        // Hands
        g2d.setColor(new Color(232, 184, 138));
        g2d.fillOval(13, 49, 6, 6);
        g2d.fillOval(45, 49, 6, 6);

        // Head
        g2d.setColor(new Color(232, 184, 138));
        g2d.fillOval(20, 6, 24, 24);

        // Cap
        g2d.setColor(new Color(44, 62, 80));
        g2d.fillArc(20, 4, 24, 16, 0, 180);

        // Eyes
        g2d.setColor(new Color(51, 51, 51));
        g2d.fillOval(26, 14, 4, 4);
        g2d.fillOval(34, 14, 4, 4);

        // Mouth
        g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawArc(27, 19, 10, 6, 200, 140);

        g2d.setTransform(oldTransform);
    }
}
