package prisoners.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import prisoners.Box;
import prisoners.FreedomExperiment;
import prisoners.Prisoner;
import prisoners.StepListener;

/**
 * Modern, visually appealing Swing application for visualizing the 100 Prisoners Problem.
 * Features a flat, modern design with smooth animations and engaging visual elements.
 */
public final class PrisonersVisualizationApp extends JFrame {
    
    // Modern UI State record for better state management
    public record UIState(
        boolean isRunning,
        int currentPrisonerNumber,
        int successfulAttempts,
        int totalAttempts,
        int animationDelay
    ) {
        public double successRate() {
            return totalAttempts > 0 ? (successfulAttempts * 100.0) / totalAttempts : 0.0;
        }
    }
    
    // Modern Color Palette
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);      // Vibrant Blue
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);      // Emerald Green
    private static final Color WARNING_COLOR = new Color(241, 196, 15);      // Sunny Yellow
    private static final Color DANGER_COLOR = new Color(231, 76, 60);        // Bright Red
    private static final Color DARK_GRAY = new Color(44, 62, 80);           // Dark Slate
    private static final Color LIGHT_GRAY = new Color(236, 240, 241);       // Light Silver
    private static final Color CARD_COLOR = Color.WHITE;                     // Clean White
    private static final Color BACKGROUND_COLOR = new Color(250, 251, 252);  // Off White
    
    // Modern Typography
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    
    // UI Components
    private final JPanel boxGridPanel;
    private final JPanel controlPanel;
    private final JPanel statsPanel;
    private final ModernButton startButton;
    private final ModernButton stopButton;
    private final ModernButton resetButton;
    private final JSpinner prisonersSpinner;
    private final JSpinner delaySpinner;
    private final JLabel statusLabel;
    private final JLabel currentPrisonerLabel;
    private final AnimatedStatsPanel successStatsPanel;
    private final AnimatedStatsPanel rateStatsPanel;
    private final ModernProgressBar experimentProgress;
    
    // Experiment state
    private ModernBoxPanel[] boxPanels;
    private FreedomExperiment experiment;
    private final ExecutorService executorService;
    private Future<?> experimentTask;
    private UIState currentState;
    private int numberOfPrisoners = 100;

    public PrisonersVisualizationApp() {
        // Initialize UI state
        currentState = new UIState(false, 0, 0, 0, 200);
        
        // Set modern look and feel
        setupModernLookAndFeel();
        
        // Initialize components with modern styling
        boxGridPanel = createModernBoxGrid();
        controlPanel = createModernControlPanel();
        statsPanel = createModernStatsPanel();
        startButton = new ModernButton("▶ Start Experiment", PRIMARY_COLOR);
        stopButton = new ModernButton("⏸ Stop", DANGER_COLOR);
        resetButton = new ModernButton("⟲ Reset", DARK_GRAY);
        
        prisonersSpinner = createModernSpinner(new SpinnerNumberModel(100, 4, 1000, 2));
        delaySpinner = createModernSpinner(new SpinnerNumberModel(200, 5, 2000, 5));
        
        statusLabel = createCenteredLabel("Ready to start experiment", TITLE_FONT, DARK_GRAY);
        currentPrisonerLabel = createCenteredLabel("No prisoner selected", BODY_FONT, DARK_GRAY);
        
        successStatsPanel = new AnimatedStatsPanel("Success Count", "0/0", SUCCESS_COLOR);
        rateStatsPanel = new AnimatedStatsPanel("Success Rate", "0.0%", PRIMARY_COLOR);
        
        experimentProgress = new ModernProgressBar();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        resetExperiment();
        
        setupWindow();
        
        executorService = Executors.newVirtualThreadPerTaskExecutor();
    }
    
    private void setupModernLookAndFeel() {
        try {
            // Try to set system look and feel with fallback approach
            for (var info : UIManager.getInstalledLookAndFeels()) {
                if (info.getName().toLowerCase().contains("system") || 
                    info.getName().toLowerCase().contains("windows") ||
                    info.getName().toLowerCase().contains("aqua")) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            
            // Customize UI defaults for modern appearance
            UIManager.put("Panel.background", BACKGROUND_COLOR);
            UIManager.put("Label.foreground", DARK_GRAY);
            UIManager.put("Button.font", BUTTON_FONT);
            UIManager.put("Label.font", BODY_FONT);
            
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
    }
    
    private JPanel createModernBoxGrid() {
        var panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Subtle gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, BACKGROUND_COLOR,
                    0, getHeight(), LIGHT_GRAY
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBackground(BACKGROUND_COLOR);
        return panel;
    }
    
    private JPanel createModernControlPanel() {
        var panel = createModernCard();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        return panel;
    }
    
    private JPanel createModernStatsPanel() {
        var panel = createModernCard();
        panel.setLayout(new GridLayout(2, 2, 20, 15));
        panel.setBorder(new CompoundBorder(
            new LineBorder(LIGHT_GRAY, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }
    
    private JPanel createModernCard() {
        var panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card shadow effect
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 12, 12);
                
                // Card background
                g2d.setColor(CARD_COLOR);
                g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 12, 12);
            }
        };
        panel.setOpaque(false);
        return panel;
    }
    
    private JSpinner createModernSpinner(SpinnerNumberModel model) {
        var spinner = new JSpinner(model);
        spinner.setFont(BODY_FONT);
        spinner.setBorder(new CompoundBorder(
            new LineBorder(LIGHT_GRAY, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        // Style the spinner editor
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setFont(BODY_FONT);
            ((JSpinner.DefaultEditor) editor).getTextField().setHorizontalAlignment(JTextField.CENTER);
        }
        
        return spinner;
    }
    
    private JLabel createModernLabel(String text, Font font, Color color) {
        var label = new JLabel(text, SwingConstants.LEFT);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }
    
    private JLabel createCenteredLabel(String text, Font font, Color color) {
        var label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }
    
    private void setupWindow() {
        setTitle("🔐 100 Prisoners Problem - Modern Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);
        
        // Custom window icon if available
        try {
            var icon = new ImageIcon(getClass().getResource("/icon.png"));
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                setIconImage(icon.getImage());
            }
        } catch (Exception ignored) {
            // Icon not available, continue without it
        }
    }
    
    private void initializeComponents() {
        stopButton.setEnabled(false);
        experimentProgress.setStringPainted(true);
        experimentProgress.setString("Ready to start");
        updateBoxGrid();
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Header panel
        var headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
        
        // Title section
        var titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        
        var titleLabel = new JLabel("🔐 100 Prisoners Problem Visualization", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(DARK_GRAY);
        
        var subtitleLabel = new JLabel(
            "Watch prisoners follow the optimal chain strategy in real-time", 
            SwingConstants.CENTER
        );
        subtitleLabel.setFont(BODY_FONT);
        subtitleLabel.setForeground(new Color(127, 140, 141));
        
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content area
        var mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(0, 30, 20, 30));
        
        // Left sidebar with controls and stats
        var sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(BACKGROUND_COLOR);
        sidebarPanel.setPreferredSize(new Dimension(350, 0));
        
        // Control panel
        setupControlPanel();
        var controlCard = createCardSection("🎮 Experiment Controls", controlPanel);
        
        // Stats panel  
        setupStatsPanel();
        var statsCard = createCardSection("📊 Live Statistics", statsPanel);
        
        // Status panel
        var statusCard = createStatusPanel();
        
        sidebarPanel.add(controlCard);
        sidebarPanel.add(javax.swing.Box.createVerticalStrut(20));
        sidebarPanel.add(statsCard);
        sidebarPanel.add(javax.swing.Box.createVerticalStrut(20));
        sidebarPanel.add(statusCard);
        sidebarPanel.add(javax.swing.Box.createVerticalGlue());
        
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        
        // Center box grid
        var gridCard = createBoxGridSection();
        mainPanel.add(gridCard, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createCardSection(String title, JPanel content) {
        var cardPanel = new JPanel(new BorderLayout());
        cardPanel.setOpaque(false);
        
        var headerLabel = new JLabel(title);
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(DARK_GRAY);
        headerLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        var card = createModernCard();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 20, 20, 20)); // Add padding inside the card
        card.add(headerLabel, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        
        cardPanel.add(card);
        return cardPanel;
    }
    
    private void setupControlPanel() {
        controlPanel.removeAll();
        controlPanel.setLayout(new GridBagLayout());
        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Prisoners control
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.4; // Give labels some weight
        gbc.anchor = GridBagConstraints.WEST;
        controlPanel.add(createModernLabel("Prisoners:", BODY_FONT, DARK_GRAY), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.6; // Give spinners more weight
        controlPanel.add(prisonersSpinner, gbc);
        
        // Speed control
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.4;
        gbc.anchor = GridBagConstraints.WEST;
        controlPanel.add(createModernLabel("Animation Speed (ms):", BODY_FONT, DARK_GRAY), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        controlPanel.add(delaySpinner, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 8, 8, 8); // More space above buttons
        
        var buttonPanel = new JPanel(new GridLayout(1, 3, 8, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(resetButton);
        
        controlPanel.add(buttonPanel, gbc);
    }
    
    private void setupStatsPanel() {
        statsPanel.removeAll();
        statsPanel.add(successStatsPanel);
        statsPanel.add(rateStatsPanel);
        
        var progressPanel = new JPanel(new BorderLayout(0, 10));
        progressPanel.setOpaque(false);
        progressPanel.add(createCenteredLabel("Progress", BODY_FONT, DARK_GRAY), BorderLayout.NORTH);
        progressPanel.add(experimentProgress, BorderLayout.CENTER);
        
        statsPanel.add(progressPanel);
        
        // Add current prisoner indicator
        var prisonerPanel = new JPanel(new BorderLayout());
        prisonerPanel.setOpaque(false);
        prisonerPanel.add(createCenteredLabel("Current Prisoner", BODY_FONT, DARK_GRAY), BorderLayout.NORTH);
        prisonerPanel.add(currentPrisonerLabel, BorderLayout.CENTER);
        
        statsPanel.add(prisonerPanel);
    }
    
    private JPanel createStatusPanel() {
        var statusPanel = new JPanel(new BorderLayout());
        statusPanel.setOpaque(false);
        
        var card = createModernCard();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        var headerLabel = new JLabel("🔄 Status");
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(DARK_GRAY);
        
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        card.add(headerLabel, BorderLayout.NORTH);
        card.add(statusLabel, BorderLayout.CENTER);
        
        statusPanel.add(card);
        return statusPanel;
    }
    
    private JPanel createBoxGridSection() {
        var gridPanel = new JPanel(new BorderLayout());
        gridPanel.setOpaque(false);
        
        var card = createModernCard();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        var headerLabel = new JLabel("🎯 Prison Boxes");
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(DARK_GRAY);
        headerLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        var scrollPane = new JScrollPane(boxGridPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        card.add(headerLabel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        
        gridPanel.add(card);
        return gridPanel;
    }
    
    private void updateBoxGrid() {
        boxGridPanel.removeAll();
        numberOfPrisoners = (Integer) prisonersSpinner.getValue();
        
        // Calculate optimal grid dimensions
        int cols = (int) Math.ceil(Math.sqrt(numberOfPrisoners));
        int rows = (int) Math.ceil((double) numberOfPrisoners / cols);
        
        boxGridPanel.setLayout(new GridLayout(rows, cols, 8, 8));
        boxGridPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        boxPanels = IntStream.range(0, numberOfPrisoners)
                .mapToObj(i -> new ModernBoxPanel(i + 1))
                .toArray(ModernBoxPanel[]::new);
        
        for (var boxPanel : boxPanels) {
            boxGridPanel.add(boxPanel);
        }
        
        boxGridPanel.revalidate();
        boxGridPanel.repaint();
    }
    
    private void setupEventHandlers() {
        startButton.addActionListener(e -> startExperiment());
        stopButton.addActionListener(e -> stopExperiment());
        resetButton.addActionListener(e -> resetExperiment());
        
        prisonersSpinner.addChangeListener(e -> {
            if (!currentState.isRunning()) {
                updateBoxGrid();
                resetExperiment();
            }
        });
        
        delaySpinner.addChangeListener(e -> 
            currentState = new UIState(
                currentState.isRunning(),
                currentState.currentPrisonerNumber(),
                currentState.successfulAttempts(),
                currentState.totalAttempts(),
                (Integer) delaySpinner.getValue()
            )
        );
    }
    
    private void startExperiment() {
        if (currentState.isRunning()) return;
        
        updateUIState(new UIState(true, 0, currentState.successfulAttempts(), 
                                 currentState.totalAttempts(), currentState.animationDelay()));
        
        // Animate button states
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        resetButton.setEnabled(false);
        prisonersSpinner.setEnabled(false);
        
        statusLabel.setText("🚀 Starting experiment...");
        statusLabel.setForeground(PRIMARY_COLOR);
        
        experimentTask = executorService.submit(this::runExperimentLoop);
    }
    
    private void stopExperiment() {
        if (!currentState.isRunning()) return;
        
        updateUIState(new UIState(false, currentState.currentPrisonerNumber(), 
                                 currentState.successfulAttempts(), currentState.totalAttempts(), 
                                 currentState.animationDelay()));
        
        if (experimentTask != null) {
            experimentTask.cancel(true);
        }
        
        SwingUtilities.invokeLater(() -> {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            resetButton.setEnabled(true);
            prisonersSpinner.setEnabled(true);
            statusLabel.setText("⏸ Experiment stopped");
            statusLabel.setForeground(WARNING_COLOR);
        });
    }
    
    private void resetExperiment() {
        stopExperiment();
        
        currentState = new UIState(false, 0, 0, 0, currentState.animationDelay());
        
        SwingUtilities.invokeLater(() -> {
            // Reset all box visuals with animation
            if (boxPanels != null) {
                for (var boxPanel : boxPanels) {
                    boxPanel.reset();
                }
            }
            
            statusLabel.setText("✅ Ready to start experiment");
            statusLabel.setForeground(SUCCESS_COLOR);
            currentPrisonerLabel.setText("No prisoner selected");
            currentPrisonerLabel.setForeground(DARK_GRAY);
            updateStatistics();
            experimentProgress.setValue(0);
            experimentProgress.setString("Ready to start");
        });
    }
    
    private void updateUIState(UIState newState) {
        currentState = newState;
    }
    
    private void runExperimentLoop() {
        try {
            while (currentState.isRunning() && !Thread.currentThread().isInterrupted()) {
                experiment = new FreedomExperiment(numberOfPrisoners);
                
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("🔄 Running experiment...");
                    statusLabel.setForeground(PRIMARY_COLOR);
                    for (var boxPanel : boxPanels) {
                        boxPanel.reset();
                    }
                });
                
                // Initialize box contents for visualization
                IntStream.range(0, numberOfPrisoners).forEach(i -> {
                    var box = experiment.getBox(i + 1);
                    SwingUtilities.invokeLater(() -> {
                        if (box != null && box.hiddenNumber() > 0) {
                            boxPanels[i].setHiddenNumber(box.hiddenNumber());
                        }
                    });
                });
                
                // Run experiment with modern step listener
                boolean success = experiment.run(new ModernStepListener());
                
                var newState = new UIState(
                    currentState.isRunning(),
                    currentState.currentPrisonerNumber(),
                    currentState.successfulAttempts() + (success ? 1 : 0),
                    currentState.totalAttempts() + 1,
                    currentState.animationDelay()
                );
                updateUIState(newState);
                
                SwingUtilities.invokeLater(() -> {
                    updateStatistics();
                    if (success) {
                        statusLabel.setText("🎉 SUCCESS! All prisoners escaped!");
                        statusLabel.setForeground(SUCCESS_COLOR);
                    } else {
                        statusLabel.setText("💥 FAILED! Some prisoners remain trapped.");
                        statusLabel.setForeground(DANGER_COLOR);
                    }
                });
                
                // Pause between experiments
                Thread.sleep(3000); // Longer pause to appreciate results
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText(String.format("❌ Error: %s", e.getMessage()));
                statusLabel.setForeground(DANGER_COLOR);
                stopExperiment();
            });
        }
    }
    
    private void updateStatistics() {
        var successRate = currentState.successRate();
        
        successStatsPanel.updateValue(String.format("%d/%d", 
            currentState.successfulAttempts(), currentState.totalAttempts()));
        rateStatsPanel.updateValue(String.format("%.1f%%", successRate));
        
        if (currentState.totalAttempts() > 0) {
            experimentProgress.setValue(Math.min(100, (int)(successRate)));
            experimentProgress.setString(String.format("%.1f%% Success Rate", successRate));
            
            // Color code the progress bar based on performance
            if (successRate >= 25 && successRate <= 35) {
                experimentProgress.setForeground(SUCCESS_COLOR);
            } else if (successRate >= 20) {
                experimentProgress.setForeground(WARNING_COLOR);
            } else {
                experimentProgress.setForeground(DANGER_COLOR);
            }
        }
    }
    
    /**
     * Modern step listener with enhanced visual feedback.
     */
    private final class ModernStepListener implements StepListener {
        @Override
        public void onStep(Prisoner prisoner, Box box) {
            SwingUtilities.invokeLater(() -> {
                if (currentState.currentPrisonerNumber() != prisoner.number()) {
                    // New prisoner started - update state with animation
                    updateUIState(new UIState(
                        currentState.isRunning(),
                        prisoner.number(),
                        currentState.successfulAttempts(),
                        currentState.totalAttempts(),
                        currentState.animationDelay()
                    ));
                    
                    currentPrisonerLabel.setText(String.format("Prisoner #%d", prisoner.number()));
                    currentPrisonerLabel.setForeground(PRIMARY_COLOR);
                    
                    // Reset previous prisoner's path with fade effect
                    for (var boxPanel : boxPanels) {
                        boxPanel.clearPath();
                    }
                }
                
                // Highlight current box with animation
                int boxIndex = box.label() - 1;
                if (boxIndex >= 0 && boxIndex < boxPanels.length) {
                    var boxPanel = boxPanels[boxIndex];
                    boxPanel.setCurrentlyOpened(true);
                    
                    // Check success with visual feedback
                    if (box.hiddenNumber() == prisoner.number()) {
                        boxPanel.setFoundTarget(true);
                    } else {
                        boxPanel.setInPath(true);
                    }
                }
            });
            
            try {
                Thread.sleep(currentState.animationDelay());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Enable modern rendering hints
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
            System.setProperty("swing.plaf.metal.controlFont", "Segoe UI");
            System.setProperty("swing.plaf.metal.userFont", "Segoe UI");
            
            var app = new PrisonersVisualizationApp();
            app.setVisible(true);
        });
    }
}