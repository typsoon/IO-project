package game.utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Point2FTest {

    @Test
    void testAddPoint() {
        Point2F p1 = new Point2F(1f, 2f);
        Point2F p2 = new Point2F(3f, 4f);

        Point2F result = p1.add(p2);

        assertEquals(4f, result.x());
        assertEquals(6f, result.y());
    }

    @Test
    void testSubtractPoint() {
        Point2F p1 = new Point2F(5f, 7f);
        Point2F p2 = new Point2F(2f, 3f);

        Point2F result = p1.subtract(p2);

        assertEquals(3f, result.x());
        assertEquals(4f, result.y());
    }

    @Test
    void testAddVector() {
        Point2F p = new Point2F(1f, 2f);
        Vector2F v = new Vector2F(3f, 4f);

        Point2F result = p.add(v);

        assertEquals(4f, result.x());
        assertEquals(6f, result.y());
    }

    @Test
    void testSubtractVector() {
        Point2F p = new Point2F(5f, 7f);
        Vector2F v = new Vector2F(2f, 3f);

        Point2F result = p.subtract(v);

        assertEquals(3f, result.x());
        assertEquals(4f, result.y());
    }

    @Test
    void testRotationWithDelta() {
        Point2F p = new Point2F(1f, 0f);
        Point2F center = new Point2F(0f, 0f);

        Point2F rotated = p.rotation((float) Math.PI / 2, center);

        float delta = 1e-6f;
        assertEquals(0f, rotated.x(), delta);
        assertEquals(1f, rotated.y(), delta);
    }

    @Test
    void testRotationAroundNonOrigin() {
        Point2F p = new Point2F(2f, 2f);
        Point2F center = new Point2F(1f, 1f);

        Point2F rotated = p.rotation((float) Math.PI, center);

        float delta = 1e-6f;
        assertEquals(0f, rotated.x(), delta);
        assertEquals(0f, rotated.y(), delta);
    }

    @Test
    void testDistance() {
        Point2F p1 = new Point2F(0f, 0f);
        Point2F p2 = new Point2F(3f, 4f);

        float dist = Point2F.distance(p1, p2);

        assertEquals(5f, dist);
    }

    @Test
    void testMultiply() {
        Point2F p = new Point2F(2f, 3f);

        Vector2F result = p.multiply(2f);

        assertEquals(4f, result.x());
        assertEquals(6f, result.y());
    }

    @Test
    void testBytesConstant() {
        assertEquals(8, Point2F.BYTES);
    }
}
