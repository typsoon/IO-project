package game.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class Vector2FTest {

    @Test
    void normalizingAReallySmallVectorShouldntYieldNaN() {
        var a = new Vector2F(0, 0);
        var b = new Vector2F(0, 0);
        var c = a.subtract(b);

        var normalized = c.normalize();

        // check that neither x nor y is NaN
        assertFalse(Float.isNaN(normalized.x()));
        assertFalse(Float.isNaN(normalized.y()));

        // and that it’s actually the zero vector
        assertEquals(0f, normalized.x());
        assertEquals(0f, normalized.y());
    }

}
