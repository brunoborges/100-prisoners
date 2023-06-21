package prisoners;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestBox {

    @Test
    public void testBox() {
        Box box = new Box(1);
        Assertions.assertEquals(1, box.label());

        box.hideNumberInside(42);
        Assertions.assertEquals(42, box.hiddenNumber());
    }

}
