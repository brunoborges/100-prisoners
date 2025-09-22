package prisoners.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import prisoners.FreedomExperiment;
import prisoners.Box;
import prisoners.Prisoner;
import prisoners.StepListener;

/**
 * Modern Java Swing application for visualizing the 100 Prisoners Problem.
 * Features enhanced with text blocks and modern Java constructs (stable features only).
 */
public final class PrisonersVisualizationApp extends JFrame {
    
    // UI State record for better state management
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
    
    // UI Components
    private final JPanel boxGridPanel;
    private final JPanel controlPanel;
    private final JPanel statusPanel;
    private final JButton startButton;
    private final JButton stopButton;
    private final JButton resetButton;
    private final JSpinner prisonersSpinner;
    private final JSpinner delaySpinner;
    private final JLabel statusLabel;
    private final JLabel currentPrisonerLabel;
    private final JLabel successCountLabel;
    private final JLabel attemptCountLabel;
    private final JProgressBar experimentProgress;
    
    // Experiment state
    private BoxPanel[] boxPanels;
    private FreedomExperiment experiment;
    private final ExecutorService executorService;
    private Future<?> experimentTask;
    private UIState currentState;
    private int numberOfPrisoners = 100;

    public PrisonersVisualizationApp() {
        // Initialize UI state
        currentState = new UIState(false, 0, 0, 0, 200);
        
        // Initialize components with modern approach
        boxGridPanel = new JPanel();
        controlPanel = createControlPanel();
        statusPanel = createStatusPanel();
        startButton = createStyledButton("🚀 Start Experiment", this::startExperiment);
        stopButton = createStyledButton("⏹️ Stop", this::stopExperiment);
        resetButton = createStyledButton("🔄 Reset", this::resetExperiment);
        prisonersSpinner = new JSpinner(new SpinnerNumberModel(100, 4, 1000, 2));
        delaySpinner = new JSpinner(new SpinnerNumberModel(200, 50, 2000, 50));
        statusLabel = new JLabel("Ready to start", SwingConstants.CENTER);
        currentPrisonerLabel = new JLabel("Current Prisoner: None", SwingConstants.CENTER);
        successCountLabel = new JLabel("Successes: 0/0", SwingConstants.CENTER);
        attemptCountLabel = new JLabel("Success Rate: 0.0%", SwingConstants.CENTER);
        experimentProgress = new JProgressBar(0, 100);
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        resetExperiment();
        
        setTitle("100 Prisoners Problem Visualization - Modern Java Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        executorService = Executors.newVirtualThreadPerTaskExecutor(); // Virtual threads (stable in Java 21+)
    }
    
    /**
     * Creates a styled button with modern Java 21 features.
     */
    private JButton createStyledButton(String text, Runnable action) {
        var button = new JButton(text);
        button.addActionListener(e -> action.run()); // Modern lambda
        return button;
    }
    
    private JPanel createControlPanel() {
        var panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Controls"));
        return panel;
    }
    
    private JPanel createStatusPanel() {
        var panel = new JPanel(new GridLayout(2, 3, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Status"));
        return panel;
    }
    
    private void initializeComponents() {
        stopButton.setEnabled(false);
        experimentProgress.setStringPainted(true);
        updateBoxGrid();
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top control panel with modern layout
        var topPanel = new JPanel(new BorderLayout());
        
        controlPanel.add(new JLabel("Prisoners:"));
        controlPanel.add(prisonersSpinner);
        controlPanel.add(new JLabel("Delay (ms):"));
        controlPanel.add(delaySpinner);
        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        controlPanel.add(resetButton);
        
        statusPanel.add(statusLabel);
        statusPanel.add(currentPrisonerLabel);
        statusPanel.add(successCountLabel);
        statusPanel.add(attemptCountLabel);
        statusPanel.add(new JLabel("Progress:", SwingConstants.CENTER));
        statusPanel.add(experimentProgress);
        
        topPanel.add(controlPanel, BorderLayout.NORTH);
        topPanel.add(statusPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        
        // Center box grid with scroll pane
        var scrollPane = new JScrollPane(boxGridPanel);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom instructions with text blocks
        var instructionsLabel = new JLabel(String.format("""
            <html><center>
            <b>Modern Java Enhanced Visualization:</b> Watch prisoners follow the optimal chain strategy.<br/>
            <span style='color: blue;'>🔵 Current path</span> | 
            <span style='color: green;'>🟢 Success</span> | 
            <span style='color: red;'>🔴 Hidden numbers</span>
            </center></html>
            """), SwingConstants.CENTER);
        instructionsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(instructionsLabel, BorderLayout.SOUTH);
    }
    
    private void updateBoxGrid() {
        boxGridPanel.removeAll();
        numberOfPrisoners = (Integer) prisonersSpinner.getValue();
        
        // Calculate grid dimensions using modern math
        int cols = (int) Math.ceil(Math.sqrt(numberOfPrisoners));
        int rows = (int) Math.ceil((double) numberOfPrisoners / cols);
        
        boxGridPanel.setLayout(new GridLayout(rows, cols, 2, 2));
        boxPanels = IntStream.range(0, numberOfPrisoners)
                .mapToObj(i -> new BoxPanel(i + 1))
                .toArray(BoxPanel[]::new);
        
        for (var boxPanel : boxPanels) {
            boxGridPanel.add(boxPanel);
        }
        
        boxGridPanel.revalidate();
        boxGridPanel.repaint();
    }
    
    private void setupEventHandlers() {
        // Modern event handling with method references
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
        
        // Update UI components
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        resetButton.setEnabled(false);
        prisonersSpinner.setEnabled(false);
        
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
            statusLabel.setText("Experiment stopped");
        });
    }
    
    private void resetExperiment() {
        stopExperiment();
        
        currentState = new UIState(false, 0, 0, 0, currentState.animationDelay());
        
        SwingUtilities.invokeLater(() -> {
            // Reset all box visuals
            if (boxPanels != null) {
                for (var boxPanel : boxPanels) {
                    boxPanel.reset();
                }
            }
            
            statusLabel.setText("Ready to start");
            currentPrisonerLabel.setText("Current Prisoner: None");
            updateStatistics();
            experimentProgress.setValue(0);
            experimentProgress.setString("Ready");
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
                    statusLabel.setText("Running experiment...");
                    for (var boxPanel : boxPanels) {
                        boxPanel.reset();
                    }
                });
                
                // Initialize box contents for visualization
                IntStream.range(0, numberOfPrisoners).forEach(i -> {
                    var box = experiment.getBox(i + 1);
                    SwingUtilities.invokeLater(() -> {
                        if (box.hiddenNumber() > 0) {
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
                    var message = success ? "✅ SUCCESS! All prisoners escaped!" : "❌ FAILED! Some prisoners remain trapped.";
                    statusLabel.setText(message);
                });
                
                // Pause between experiments
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText(String.format("Error: %s", e.getMessage()));
                stopExperiment();
            });
        }
    }
    
    private void updateStatistics() {
        var successRate = currentState.successRate();
        successCountLabel.setText(String.format("Successes: %d/%d", currentState.successfulAttempts(), currentState.totalAttempts()));
        attemptCountLabel.setText(String.format("Success Rate: %.1f%%", successRate));
        
        if (currentState.totalAttempts() > 0) {
            experimentProgress.setValue(Math.min(100, currentState.totalAttempts() * 2));
            experimentProgress.setString(String.format("Attempt %d - %.1f%% success", currentState.totalAttempts(), successRate));
        }
    }
    
    /**
     * Modern step listener using records and pattern matching.
     */
    private final class ModernStepListener implements StepListener {
        @Override
        public void onStep(Prisoner prisoner, Box box) {
            SwingUtilities.invokeLater(() -> {
                if (currentState.currentPrisonerNumber() != prisoner.number()) {
                    // New prisoner started - update state
                    updateUIState(new UIState(
                        currentState.isRunning(),
                        prisoner.number(),
                        currentState.successfulAttempts(),
                        currentState.totalAttempts(),
                        currentState.animationDelay()
                    ));
                    
                    currentPrisonerLabel.setText(String.format("Current Prisoner: %d", prisoner.number()));
                    
                    // Reset previous prisoner's path
                    for (var boxPanel : boxPanels) {
                        boxPanel.clearPath();
                    }
                }
                
                // Highlight current box using pattern matching bounds check
                int boxIndex = box.label() - 1;
                if (boxIndex >= 0 && boxIndex < boxPanels.length) {
                    var boxPanel = boxPanels[boxIndex];
                    boxPanel.setCurrentlyOpened(true);
                    
                    // Check success using modern comparison
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
            try {
                // Try to set system look and feel with modern approach
                UIManager.getInstalledLookAndFeels();
                for (var info : UIManager.getInstalledLookAndFeels()) {
                    if (info.getName().toLowerCase().contains("system") || 
                        info.getName().toLowerCase().contains("windows") ||
                        info.getName().toLowerCase().contains("aqua")) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println(String.format("Could not set system look and feel: %s", e.getMessage()));
            }
            
            var app = new PrisonersVisualizationApp();
            app.setVisible(true);
        });
    }
}