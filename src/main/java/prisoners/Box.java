package prisoners;

/**
 * This class represents a box with a label and a hidden number.
 * 
 * @see https://en.wikipedia.org/wiki/100_prisoners_problem
 */
public class Box {

    private int label = -1;
    private int hiddenNumber = -1;

    public Box(int label) {
        if (label < 0) {
            throw new IllegalArgumentException("Label must be positive");
        }
        this.label = label;
    }

    public void hideNumberInside(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Number must be positive");
        }
        this.hiddenNumber = number;
    }

    public int label() {
        return label;
    }

    public int hiddenNumber() {
        return hiddenNumber;
    }

    @Override
    public String toString() {
        return "Box [label=" + label + ", hiddenNumber=" + hiddenNumber + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Box) {
            Box other = (Box) obj;
            boolean sameLabel = this.label == other.label;
            boolean sameHiddenNumber = this.hiddenNumber == other.hiddenNumber;
            return sameLabel && sameHiddenNumber;
        }
        return false;
    }

}
