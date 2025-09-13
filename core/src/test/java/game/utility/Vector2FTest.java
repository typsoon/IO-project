package game.utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class Vector2FTest {

    private static final float DELTA = 1e-6f;

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

    @Test
    void testAdd() {
        Vector2F v1 = new Vector2F(1f, 2f);
        Vector2F v2 = new Vector2F(3f, 4f);

        Vector2F result = v1.add(v2);

        assertEquals(4f, result.x(), DELTA);
        assertEquals(6f, result.y(), DELTA);
    }

    @Test
    void testSubtract() {
        Vector2F v1 = new Vector2F(5f, 7f);
        Vector2F v2 = new Vector2F(2f, 3f);

        Vector2F result = v1.subtract(v2);

        assertEquals(3f, result.x(), DELTA);
        assertEquals(4f, result.y(), DELTA);
    }

    @Test
    void testMultiply() {
        Vector2F v = new Vector2F(2f, 3f);
        Vector2F result = v.multiply(2f);

        assertEquals(4f, result.x(), DELTA);
        assertEquals(6f, result.y(), DELTA);
    }

    @Test
    void testLength() {
        Vector2F v = new Vector2F(3f, 4f);
        assertEquals(5f, v.length(), DELTA);
    }

    @Test
    void testAngle() {
        Vector2F v = new Vector2F(0f, 1f);
        assertEquals(Math.PI / 2, v.angle(), DELTA);

        Vector2F v2 = new Vector2F(1f, 0f);
        assertEquals(0f, v2.angle(), DELTA);
    }

    @Test
    void testDot() {
        Vector2F v1 = new Vector2F(1f, 2f);
        Vector2F v2 = new Vector2F(3f, 4f);

        assertEquals(11f, v1.dot(v2), DELTA);
    }

    @Test
    void testNormalize() {
        Vector2F v = new Vector2F(3f, 4f);
        Vector2F normalized = v.normalize();

        assertEquals(0.6f, normalized.x(), DELTA);
        assertEquals(0.8f, normalized.y(), DELTA);
        assertEquals(1f, normalized.length(), DELTA);
    }

    @Test
    void testNormalizeZeroVector() {
        Vector2F v = new Vector2F(0f, 0f);
        Vector2F normalized = v.normalize();

        assertEquals(0f, normalized.x(), DELTA);
        assertEquals(0f, normalized.y(), DELTA);
    }
}
