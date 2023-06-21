package prisoners;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestApp {

    @Test
    public void testApp() throws Exception {
        App app = new App();
        int result = app.call();
        Assertions.assertTrue(result < 50, "Result must be less than 50%");
    }

}
