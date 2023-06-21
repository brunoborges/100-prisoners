package prisoners;

/**
 * This class represents a box with a label and a hidden number.
 * 
 * @see https://en.wikipedia.org/wiki/100_prisoners_problem
 */
public class Box {

    private int label;
    private int hiddenNumber;

    public Box(int label) {
        this.label = label;
    }

    public void hideNumberInside(int number) {
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

}
