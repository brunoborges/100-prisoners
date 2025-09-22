package prisoners;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Modern Java implementation of the 100 Prisoners Problem CLI application.
 * Uses stable features from Java 21+ without any preview functionality.
 * 
 * <p>
 * The 100 prisoners problem is a mathematical problem in probability theory and
 * combinatorics where 100 numbered prisoners must find their own numbers in 100 boxes.
 * 
 * <p>
 * The optimal strategy achieves a remarkable ~31% success rate instead of virtually 0%.
 * This is achieved through the "chain following" strategy where each prisoner starts
 * with their own box number and follows the chain of hidden numbers.
 * 
 * @see <a href="https://en.wikipedia.org/wiki/100_prisoners_problem">Wikipedia Article</a>
 */
@Command(
    name = "mathward", 
    mixinStandardHelpOptions = true, 
    version = "mathward 3.0 (Java 21+ Stable Features)", 
    description = """
        Runs the 100 Prisoners Problem simulation using the optimal chain-following strategy.
        
        The problem: 100 prisoners must each find their number in 100 boxes, with each prisoner
        allowed to open only 50 boxes. If ALL prisoners succeed, they all go free.
        
        The solution: Each prisoner starts with their own box number and follows the chain
        of hidden numbers. This achieves ~31% success rate instead of virtually impossible odds.
        """,
    sortOptions = false
)
public final class App implements Callable<Integer> {

    private static final Logger logger = Logger.getLogger(App.class.getName());

    @Option(
        names = {"-p", "--prisoners"}, 
        description = "Number of prisoners (must be even, minimum 2).",
        defaultValue = "100", 
        paramLabel = "COUNT"
    )
    private int numberOfPrisoners = 100;

    @Option(
        names = {"-a", "--attempts"}, 
        description = "Number of simulation attempts to run.",
        defaultValue = "1000", 
        paramLabel = "COUNT"
    )
    private int attempts = 1000;

    @Option(
        names = {"-v", "--verbose"}, 
        description = "Enable verbose output with detailed statistics."
    )
    private boolean verbose = false;

    public static void main(String[] args) {
        var exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        // Validate inputs using modern switch expressions
        return switch (validateInputs()) {
            case null -> runExperiment();
            case String error -> {
                logger.severe(error);
                yield 1;
            }
        };
    }

    /**
     * Modern input validation using pattern matching and text blocks.
     */
    private String validateInputs() {
        if (numberOfPrisoners < 2) {
            return "Error: Number of prisoners must be at least 2";
        }
        if (numberOfPrisoners % 2 != 0) {
            return String.format("Error: Number of prisoners must be even (got: %d)", numberOfPrisoners);
        }
        if (numberOfPrisoners > 10000) {
            return "Error: Number of prisoners too large (maximum: 10000)";
        }
        if (attempts < 1) {
            return "Error: Number of attempts must be positive";
        }
        if (attempts > 100000) {
            return "Error: Too many attempts (maximum: 100000)";
        }
        return null; // All validations passed
    }

    private Integer runExperiment() {
        var experiment = new FreedomExperiment(numberOfPrisoners);

        // Modern formatted logging
        logger.info(String.format("""
            Starting 100 Prisoners Problem simulation:
            - Prisoners: %d
            - Attempts: %d
            - Strategy: Optimal chain-following
            """, numberOfPrisoners, attempts));

        int successes = 0;
        var progressBarTitle = String.format("Prison escape attempts (%d prisoners)", numberOfPrisoners);

        try (var progressBar = new ProgressBarBuilder()
                .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BAR)
                .setTaskName(progressBarTitle)
                .setInitialMax(attempts)
                .build()) {
            
            for (int i = 0; i < attempts; i++) {
                if (experiment.run()) {
                    successes++;
                }
                progressBar.step();
            }
        }

        // Calculate and display results using modern features
        var results = new ExperimentResults(attempts, successes, numberOfPrisoners);
        displayResults(results);

        return 0;
    }

    /**
     * Record for experiment results with computed properties.
     */
    private record ExperimentResults(int totalAttempts, int successes, int prisoners) {
        public double successRate() {
            return totalAttempts > 0 ? (successes * 100.0) / totalAttempts : 0.0;
        }

        public double theoreticalRate() {
            // Theoretical success rate is approximately 1 - ln(2) ≈ 30.685%
            return 30.685;
        }

        public String summary() {
            return String.format("""
                
                🎯 SIMULATION RESULTS:
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                Prisoners:           %d
                Total attempts:      %d
                Successful escapes:  %d
                Success rate:        %.2f%%
                Theoretical rate:    %.2f%%
                Difference:          %.2f%%
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                """, 
                prisoners, totalAttempts, successes, successRate(), 
                theoreticalRate(), Math.abs(successRate() - theoreticalRate()));
        }
    }

    private void displayResults(ExperimentResults results) {
        if (verbose) {
            logger.info(results.summary());
            
            // Additional verbose statistics
            var theoreticalSuccesses = (int) (attempts * results.theoreticalRate() / 100.0);
            var difference = results.successes - theoreticalSuccesses;
            
            logger.info(String.format("""
                
                📊 DETAILED ANALYSIS:
                Expected successes:  %d
                Actual difference:   %s%d
                Statistical accuracy: %s
                """, 
                theoreticalSuccesses,
                difference > 0 ? "+" : "",
                difference,
                Math.abs(difference) < attempts * 0.05 ? "Good" : "Check sample size"));
        } else {
            // Concise output for normal mode
            logger.info(String.format("Results: %d/%d successes (%.1f%%)", 
                results.successes, results.totalAttempts, results.successRate()));
        }
    }

    static {
        // Configure logging format using text blocks
        System.setProperty("java.util.logging.SimpleFormatter.format",
                """
                %1$tF %1$tT [%4$s] %2$s: %5$s%6$s%n
                """);
    }
}
