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
 * <p>
 * This is a program that implements of the ideal solution to the 100 prisoners
 * problem.
 * 
 * <p>
 * The 100 prisoners problem is a mathematical problem in probability theory and
 * combinatorics.
 * 
 * <p>
 * In this problem, 100 numbered prisoners must find their own
 * numbers in one of 100 drawers in order to survive. The rules state that each
 * prisoner may open only 50 drawers and cannot communicate with other
 * prisoners. At first glance, the situation appears hopeless, because the
 * maximum number of prisoners that can be freed is 50. However, by using a
 * clever strategy, the prisoners can be freed with nearly 31% probability.
 * 
 * @see https://en.wikipedia.org/wiki/100_prisoners_problem
 */
@Command(name = "mathward", mixinStandardHelpOptions = true, version = "ward 1.0", description = "Runs the 100 Prisoners math problem.", sortOptions = false)
public class App implements Callable<Integer> {

    private static final Logger logger = Logger.getLogger(App.class.getName());

    @Option(names = "-p", description = "The number of prisoners to run the experiment with.", defaultValue = "100", paramLabel = "numberOfPrisoners", arity = "1", order = -2)
    private int numberOfPrisoners = 100;

    @Option(names = "-a", description = "The number of attepmts to try.", defaultValue = "1000", paramLabel = "attempts", arity = "1", order = -3)
    private int attempts = 1000;

    public static void main(String[] args) throws Exception {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        // Prepare experiment
        FreedomExperiment experiment = new FreedomExperiment(numberOfPrisoners);

        // Log info about exercise
        logger.info(() -> "Running exercise with " + numberOfPrisoners + " prisoners and " + attempts
                + " attempts. Please wait...");

        // Calculate success rate
        int successes = 0;

        try (ProgressBar bar = new ProgressBarBuilder()
                .setStyle(ProgressBarStyle.ASCII)
                .setTaskName("Prison exercise attempts")
                .setInitialMax(attempts)
                .build();) {
            for (int i = 0; i < attempts; i++) {
                if (experiment.run()) {
                    successes++;
                }

                bar.step();
            }
        }

        double chancesOfEscaping = ((double) successes / attempts) * 100;

        // Output results
        logger.info("Number of cycles tested: " + attempts);
        logger.info("How often did prisoners escape? " + chancesOfEscaping + "%");

        return 0;
    }

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
    }

}
