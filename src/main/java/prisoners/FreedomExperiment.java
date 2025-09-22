package prisoners;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * This class represents the 100 prisoners problem.
 * Modernized for Java 25 with stable features only.
 * 
 * @see https://en.wikipedia.org/wiki/100_prisoners_problem
 */
public final class FreedomExperiment {

    private static final Logger logger = Logger.getLogger(FreedomExperiment.class.getName());

    private final int numberOfPrisoners;
    private final List<Prisoner> prisoners;
    private final List<Prisoner> freedPrisoners;
    private final Map<Integer, Box> boxes;
    private final List<Integer> numbers;

    public FreedomExperiment(int numberOfPrisoners) {
        this.numberOfPrisoners = validateNumberOfPrisoners(numberOfPrisoners);
        
        // Use sized collections for better performance
        this.prisoners = new ArrayList<>(numberOfPrisoners);
        this.freedPrisoners = new ArrayList<>(numberOfPrisoners);
        this.numbers = new ArrayList<>(numberOfPrisoners);
        this.boxes = new HashMap<>(numberOfPrisoners);
    }

    /**
     * Runs the experiment with optional step listener using modern switch expressions.
     */
    private boolean runInternal(Optional<StepListener> stepListener) {
        prepareDatastructures();
        shuffleNumbersInsideBoxes();
        freedPrisoners.clear();

        final int maxSearches = numberOfPrisoners / 2;
        
        return prisoners.stream().allMatch(prisoner -> 
            findPrisonerNumber(prisoner, maxSearches, stepListener)
        );
    }

    /**
     * Modern implementation of prisoner search using enhanced pattern matching.
     */
    private boolean findPrisonerNumber(Prisoner prisoner, int maxSearches, Optional<StepListener> stepListener) {
        Box currentBox = boxes.get(prisoner.number());
        int searches = 0;
        
        while (searches < maxSearches) {
            // Capture the current box for the lambda
            final Box boxToReport = currentBox;
            stepListener.ifPresent(listener -> listener.onStep(prisoner, boxToReport));
            
            if (currentBox.hiddenNumber() == prisoner.number()) {
                freedPrisoners.add(prisoner);
                return true; // Found their number!
            }
            
            // Continue following the chain
            currentBox = boxes.get(currentBox.hiddenNumber());
            searches++;
        }
        
        return false; // Exceeded max searches
    }

    private void shuffleNumbersInsideBoxes() {
        Collections.shuffle(numbers);

        if (logger.isLoggable(java.util.logging.Level.FINE)) {
            logger.fine(String.format("Shuffled tags: %s", numbers));
        }

        // Modern indexed loop with range
        IntStream.range(0, numberOfPrisoners)
                .forEach(i -> {
                    int boxNumber = i + 1;
                    boxes.get(boxNumber).hideNumberInside(numbers.get(i));
                });
    }

    private void prepareDatastructures() {
        prisoners.clear();
        freedPrisoners.clear();
        boxes.clear();
        numbers.clear();
        
        // Generate sequential numbers and create objects
        IntStream.rangeClosed(1, numberOfPrisoners)
                .forEach(i -> {
                    numbers.add(i);
                    prisoners.add(new Prisoner(i));
                    boxes.put(i, new Box(i));
                });
    }

    /**
     * Modern validation using switch expressions and text blocks for error messages.
     */
    private static int validateNumberOfPrisoners(int numberOfPrisoners) {
        if (numberOfPrisoners < 2) {
            throw new IllegalArgumentException(
                """
                Number of prisoners must be at least 2.
                The problem requires multiple prisoners to demonstrate the strategy.
                """);
        }
        if (numberOfPrisoners % 2 != 0) {
            throw new IllegalArgumentException(String.format(
                """
                Number of prisoners must be even (got: %d).
                Each prisoner can open half the boxes, so an even count is required.
                """, numberOfPrisoners));
        }
        return numberOfPrisoners;
    }

    public Box getBox(int boxNumber) {
        return boxes.get(boxNumber);
    }

    public boolean run(StepListener stepListener) {
        return runInternal(Optional.ofNullable(stepListener));
    }

    public boolean run() {
        return runInternal(Optional.empty());
    }

    /**
     * Get experiment statistics using modern records.
     */
    public ExperimentStats getStats() {
        return new ExperimentStats(
            numberOfPrisoners,
            freedPrisoners.size(),
            freedPrisoners.size() == numberOfPrisoners
        );
    }
    
    /**
     * Record for experiment statistics (Java 14+ feature, enhanced in Java 21).
     */
    public record ExperimentStats(
        int totalPrisoners,
        int freedPrisoners, 
        boolean allEscaped
    ) {
        public double successRate() {
            return totalPrisoners > 0 ? (double) freedPrisoners / totalPrisoners * 100.0 : 0.0;
        }
        
        @Override
        public String toString() {
            return String.format("""
                Experiment Results:
                - Total prisoners: %d
                - Freed prisoners: %d
                - Success rate: %.1f%%
                - All escaped: %s
                """, totalPrisoners, freedPrisoners, successRate(), allEscaped);
        }
    }
}
