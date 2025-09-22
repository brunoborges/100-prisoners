package prisoners;

import java.util.Objects;

/**
 * This class represents a box with a label and a hidden number.
 * Modernized for Java 21 with improved validation and string handling.
 * 
 * @see https://en.wikipedia.org/wiki/100_prisoners_problem
 */
public final class Box {

    private final int label;
    private int hiddenNumber = -1;

    public Box(int label) {
        this.label = validatePositive(label, "Label");
    }

    public void hideNumberInside(int number) {
        this.hiddenNumber = validatePositive(number, "Number");
    }

    public int label() {
        return label;
    }

    public int hiddenNumber() {
        return hiddenNumber;
    }

    @Override
    public String toString() {
        return String.format("Box[label=%d, hiddenNumber=%d]", label, hiddenNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Box other) {
            return this.label == other.label && this.hiddenNumber == other.hiddenNumber;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, hiddenNumber);
    }

    /**
     * Validates that a number is positive using modern Java patterns.
     */
    private static int validatePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(String.format("%s must be positive, got: %d", fieldName, value));
        }
        return value;
    }
}
