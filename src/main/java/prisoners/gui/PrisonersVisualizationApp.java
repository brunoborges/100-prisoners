package prisoners.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import prisoners.FreedomExperiment;
import prisoners.Box;
import prisoners.Prisoner;
import prisoners.StepListener;

/**
 * Java Swing application for visualizing the 100 Prisoners Problem experiment.
 * Provides real-time visual feedback of prisoners navigating through boxes.
 */
public class PrisonersVisualizationApp extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    // UI Components
    private JPanel boxGridPanel;
    private JPanel controlPanel;
    private JPanel statusPanel;
    private JButton startButton;
    private JButton stopButton;
    private JButton resetButton;
    private JSpinner prisonersSpinner;
    private JSpinner delaySpinner;
    private JLabel statusLabel;
    private JLabel currentPrisonerLabel;
    private JLabel successCountLabel;
    private JLabel attemptCountLabel;
    private JProgressBar experimentProgress;
    
    // Experiment state
    private BoxPanel[] boxPanels;
    private FreedomExperiment experiment;
    private ExecutorService executorService;
    private Future<?> experimentTask;
    private boolean isRunning = false;
    private int numberOfPrisoners = 100;
    private int currentPrisonerNumber = 0;
    private int successfulAttempts = 0;
    private int totalAttempts = 0;
    private int animationDelay = 200; // milliseconds
    
    public PrisonersVisualizationApp() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        resetExperiment();
        
        setTitle("100 Prisoners Problem Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        executorService = Executors.newSingleThreadExecutor();
    }
    
    private void initializeComponents() {
        // Control panel components
        controlPanel = new JPanel(new FlowLayout());
        startButton = new JButton("Start Experiment");
        stopButton = new JButton("Stop");
        resetButton = new JButton("Reset");
        
        prisonersSpinner = new JSpinner(new SpinnerNumberModel(100, 4, 1000, 2));
        delaySpinner = new JSpinner(new SpinnerNumberModel(200, 50, 2000, 50));
        
        // Status panel components
        statusPanel = new JPanel(new GridLayout(2, 3, 10, 5));
        statusLabel = new JLabel("Ready to start", SwingConstants.CENTER);
        currentPrisonerLabel = new JLabel("Current Prisoner: None", SwingConstants.CENTER);
        successCountLabel = new JLabel("Successes: 0/0", SwingConstants.CENTER);
        attemptCountLabel = new JLabel("Success Rate: 0.0%", SwingConstants.CENTER);
        experimentProgress = new JProgressBar(0, 100);
        experimentProgress.setStringPainted(true);
        
        // Box grid panel
        boxGridPanel = new JPanel();
        updateBoxGrid();
        
        stopButton.setEnabled(false);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top control panel
        JPanel topPanel = new JPanel(new BorderLayout());
        
        controlPanel.setBorder(BorderFactory.createTitledBorder("Controls"));
        controlPanel.add(new JLabel("Prisoners:"));
        controlPanel.add(prisonersSpinner);
        controlPanel.add(new JLabel("Delay (ms):"));
        controlPanel.add(delaySpinner);
        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        controlPanel.add(resetButton);
        
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status"));
        statusPanel.add(statusLabel);
        statusPanel.add(currentPrisonerLabel);
        statusPanel.add(successCountLabel);
        statusPanel.add(attemptCountLabel);
        statusPanel.add(new JLabel("Progress:", SwingConstants.CENTER));
        statusPanel.add(experimentProgress);
        
        topPanel.add(controlPanel, BorderLayout.NORTH);
        topPanel.add(statusPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center box grid
        JScrollPane scrollPane = new JScrollPane(boxGridPanel);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom instructions
        JLabel instructionsLabel = new JLabel(
            "<html><center>" +
            "<b>Instructions:</b> Watch as each prisoner follows the chain strategy. " +
            "<span style='color: blue;'>Blue</span> = current prisoner's path, " +
            "<span style='color: green;'>Green</span> = found their number, " +
            "<span style='color: red;'>Red</span> = failed to find their number" +
            "</center></html>", 
            SwingConstants.CENTER
        );
        instructionsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(instructionsLabel, BorderLayout.SOUTH);
    }
    
    private void updateBoxGrid() {
        boxGridPanel.removeAll();
        
        numberOfPrisoners = (Integer) prisonersSpinner.getValue();
        
        // Calculate grid dimensions
        int cols = (int) Math.ceil(Math.sqrt(numberOfPrisoners));
        int rows = (int) Math.ceil((double) numberOfPrisoners / cols);
        
        boxGridPanel.setLayout(new GridLayout(rows, cols, 2, 2));
        boxPanels = new BoxPanel[numberOfPrisoners];
        
        for (int i = 0; i < numberOfPrisoners; i++) {
            boxPanels[i] = new BoxPanel(i + 1);
            boxGridPanel.add(boxPanels[i]);
        }
        
        boxGridPanel.revalidate();
        boxGridPanel.repaint();
    }
    
    private void setupEventHandlers() {
        startButton.addActionListener(e -> startExperiment());
        stopButton.addActionListener(e -> stopExperiment());
        resetButton.addActionListener(e -> resetExperiment());
        
        prisonersSpinner.addChangeListener(e -> {
            if (!isRunning) {
                updateBoxGrid();
                resetExperiment();
            }
        });
        
        delaySpinner.addChangeListener(e -> {
            animationDelay = (Integer) delaySpinner.getValue();
        });
    }
    
    private void startExperiment() {
        if (isRunning) return;
        
        isRunning = true;
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        resetButton.setEnabled(false);
        prisonersSpinner.setEnabled(false);
        
        animationDelay = (Integer) delaySpinner.getValue();
        
        experimentTask = executorService.submit(this::runExperimentLoop);
    }
    
    private void stopExperiment() {
        if (!isRunning) return;
        
        isRunning = false;
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
        
        currentPrisonerNumber = 0;
        successfulAttempts = 0;
        totalAttempts = 0;
        
        SwingUtilities.invokeLater(() -> {
            // Reset all box visuals
            if (boxPanels != null) {
                for (BoxPanel boxPanel : boxPanels) {
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
    
    private void runExperimentLoop() {
        try {
            while (isRunning && !Thread.currentThread().isInterrupted()) {
                experiment = new FreedomExperiment(numberOfPrisoners);
                
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Running experiment...");
                    for (BoxPanel boxPanel : boxPanels) {
                        boxPanel.reset();
                    }
                });
                
                // Initialize box contents for visualization
                for (int i = 0; i < numberOfPrisoners; i++) {
                    final Box box = experiment.getBox(i + 1);
                    final int boxIndex = i;
                    SwingUtilities.invokeLater(() -> {
                        if (box.hiddenNumber() > 0) {
                            boxPanels[boxIndex].setHiddenNumber(box.hiddenNumber());
                        }
                    });
                }
                
                // Run experiment with visualization
                boolean success = experiment.run(new VisualizationStepListener());
                
                totalAttempts++;
                if (success) {
                    successfulAttempts++;
                }
                
                SwingUtilities.invokeLater(() -> {
                    updateStatistics();
                    statusLabel.setText(success ? "SUCCESS! All prisoners escaped!" : "FAILED! Some prisoners remain trapped.");
                });
                
                // Pause between experiments
                Thread.sleep(2000);
                
                if (!isRunning) break;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Error: " + e.getMessage());
                stopExperiment();
            });
        }
    }
    
    private void updateStatistics() {
        double successRate = totalAttempts > 0 ? (successfulAttempts * 100.0) / totalAttempts : 0.0;
        successCountLabel.setText(String.format("Successes: %d/%d", successfulAttempts, totalAttempts));
        attemptCountLabel.setText(String.format("Success Rate: %.1f%%", successRate));
        
        if (totalAttempts > 0) {
            experimentProgress.setValue(Math.min(100, totalAttempts * 2)); // Scale for visual effect
            experimentProgress.setString(String.format("Attempt %d - %.1f%% success", totalAttempts, successRate));
        }
    }
    
    private class VisualizationStepListener implements StepListener {
        @Override
        public void onStep(Prisoner prisoner, Box box) {
            SwingUtilities.invokeLater(() -> {
                if (currentPrisonerNumber != prisoner.number()) {
                    // New prisoner started
                    currentPrisonerNumber = prisoner.number();
                    currentPrisonerLabel.setText("Current Prisoner: " + currentPrisonerNumber);
                    
                    // Reset previous prisoner's path
                    for (BoxPanel boxPanel : boxPanels) {
                        boxPanel.clearPath();
                    }
                }
                
                // Highlight current box being opened
                int boxIndex = box.label() - 1;
                if (boxIndex >= 0 && boxIndex < boxPanels.length) {
                    boxPanels[boxIndex].setCurrentlyOpened(true);
                    
                    // Check if prisoner found their number
                    if (box.hiddenNumber() == prisoner.number()) {
                        boxPanels[boxIndex].setFoundTarget(true);
                    } else {
                        boxPanels[boxIndex].setInPath(true);
                    }
                }
            });
            
            try {
                Thread.sleep(animationDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Try to set system look and feel
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("System".equals(info.getName()) || info.getName().contains("System")) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                // Use default look and feel if system one fails
                System.err.println("Could not set system look and feel, using default: " + e.getMessage());
            }
            
            new PrisonersVisualizationApp().setVisible(true);
        });
    }
}