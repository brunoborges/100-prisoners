package prisoners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * This class represents the 100 prisoners problem.
 * 
 * @see https://en.wikipedia.org/wiki/100_prisoners_problem
 */
public class FreedomExperiment {

    private static final Logger logger = Logger.getLogger(FreedomExperiment.class.getName());

    private int numberOfPrisoners;

    private List<Prisoner> prisoners;
    private List<Prisoner> freedPrisoners;
    private Map<Integer, Box> boxes;
    private List<Integer> numbers;

    public FreedomExperiment(int numberOfPrisoners) {
        this.numberOfPrisoners = numberOfPrisoners;
        validateNumberOfPrisoners(numberOfPrisoners);
        prepareDatastructures();
    }

    public boolean run() {
        shuffleNumbersInsideBoxes();
        freedPrisoners.clear();

        int maxSearches = numberOfPrisoners / 2;
        for (Prisoner p : prisoners) {
            int searches = 0;
            Box currentBox = boxes.get(p.number());
            boolean found = false;
            while (searches < maxSearches) {
                if (currentBox.hiddenNumber() == p.number()) {
                    found = true;
                    break;
                } else {
                    var nextBoxNumber = currentBox.hiddenNumber();
                    currentBox = boxes.get(nextBoxNumber);
                    searches++;
                }
            }

            if (found) {
                freedPrisoners.add(p);
            } else {
                break;
            }
        }

        return freedPrisoners.size() == numberOfPrisoners;
    }

    private void shuffleNumbersInsideBoxes() {
        Collections.shuffle(numbers);

        if (logger.isLoggable(java.util.logging.Level.FINE))
            logger.fine("Shuffled tags: " + numbers);

        for (int i = 0; i < numberOfPrisoners; i++) {
            int boxNumber = i + 1;
            boxes.get(boxNumber).hideNumberInside(numbers.get(i));
        }
    }

    private void prepareDatastructures() {
        prisoners = new ArrayList<>(numberOfPrisoners);
        freedPrisoners = new ArrayList<>(numberOfPrisoners);
        boxes = new HashMap<>(numberOfPrisoners);
        numbers = new ArrayList<>(IntStream.range(1, numberOfPrisoners + 1).boxed().toList());

        // Prepare sets
        numbers.forEach(i -> {
            prisoners.add(new Prisoner(i));
            boxes.put(i, new Box(i));
        });

    }

    private void validateNumberOfPrisoners(int numberOfPrisoners2) {
        if (numberOfPrisoners % 2 != 0)
            throw new IllegalArgumentException("Number of prisoners must be even.");

        if (numberOfPrisoners < 2)
            throw new IllegalArgumentException("Number of prisoners must be at least 2.");
    }

}
