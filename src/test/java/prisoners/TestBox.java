package prisoners;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Modern Java tests for the Box class.
 */
public class TestBox {

    @Test
    public void testBox() {
        var box = new Box(1);
        Assertions.assertEquals(1, box.label());

        box.hideNumberInside(42);
        Assertions.assertEquals(42, box.hiddenNumber());

        box.hideNumberInside(37);
        Assertions.assertEquals(37, box.hiddenNumber());

        var box2 = new Box(1);
        box2.hideNumberInside(box.hiddenNumber());
        Assertions.assertEquals(box, box2);
    }

    @Test
    public void testValidBoxCreation() {
        var box = new Box(5);
        Assertions.assertEquals(5, box.label());
        Assertions.assertEquals(-1, box.hiddenNumber());
    }

    @Test
    public void testInvalidBoxCreation() {
        // Test modern validation with enhanced error messages
        var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Box(-1);
        });
        Assertions.assertTrue(exception.getMessage().contains("Label must be positive"));
        Assertions.assertTrue(exception.getMessage().contains("got: -1"));
    }

    @Test
    public void testInvalidHiddenNumber() {
        var box = new Box(1);
        var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            box.hideNumberInside(-5);
        });
        Assertions.assertTrue(exception.getMessage().contains("Number must be positive"));
        Assertions.assertTrue(exception.getMessage().contains("got: -5"));
    }

    @Test
    public void testModernToString() {
        var box = new Box(7);
        box.hideNumberInside(13);
        
        var result = box.toString();
        // Test modern string template format
        Assertions.assertTrue(result.contains("Box[label=7"));
        Assertions.assertTrue(result.contains("hiddenNumber=13"));
    }

    @Test
    public void testModernEquals() {
        var box1 = new Box(5);
        var box2 = new Box(5);
        var box3 = new Box(7);
        
        box1.hideNumberInside(10);
        box2.hideNumberInside(10);
        box3.hideNumberInside(10);
        
        // Test modern pattern matching in equals
        Assertions.assertEquals(box1, box2);
        Assertions.assertNotEquals(box1, box3);
        Assertions.assertNotEquals(box1, null);
        Assertions.assertNotEquals(box1, "not a box");
    }

    @Test
    public void testHashCode() {
        var box1 = new Box(5);
        var box2 = new Box(5);
        
        box1.hideNumberInside(10);
        box2.hideNumberInside(10);
        
        // Equal objects should have equal hash codes
        Assertions.assertEquals(box1.hashCode(), box2.hashCode());
    }
}
