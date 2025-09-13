package game.utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Rectangle2FTest {

    private static final float DELTA = 1e-6f;

    @Test
    void testRotateBBAroundCenter() {
        Rectangle2F rect = new Rectangle2F(0f, 0f, 2f, 2f);

        Rectangle2F rotated = rect.rotateBB((float) Math.PI / 2); // 90 stopni

        assertEquals(2f, rotated.end().x() - rotated.begin().x(), DELTA, "Width unchanged");
        assertEquals(2f, rotated.end().y() - rotated.begin().y(), DELTA, "Height unchanged");

        assertTrue(rotated.begin().x() <= rotated.end().x());
        assertTrue(rotated.begin().y() <= rotated.end().y());
    }

    @Test
    void testRotateBBDoesNotInvertCoordinates() {
        Rectangle2F rect = new Rectangle2F(0f, 0f, 1f, 1f);
        Rectangle2F rotated = rect.rotateBB((float) Math.PI / 4);

        assertTrue(rotated.begin().x() <= rotated.end().x());
        assertTrue(rotated.begin().y() <= rotated.end().y());
    }

    @Test
    void testRotateBBAxisAligned() {
        Rectangle2F rect = new Rectangle2F(0f, 0f, 2f, 1f); // prostokąt 2x1
        Point2F center = new Point2F(1f, 0.5f);

        Rectangle2F rotated = rect.rotateBB((float) Math.PI / 2, center);

        assertTrue(rotated.begin().x() <= rotated.end().x());
        assertTrue(rotated.begin().y() <= rotated.end().y());

        Point2F[] corners = {
                new Point2F(rect.begin().x(), rect.begin().y()).rotation((float) Math.PI / 2, center),
                new Point2F(rect.end().x(), rect.begin().y()).rotation((float) Math.PI / 2, center),
                new Point2F(rect.end().x(), rect.end().y()).rotation((float) Math.PI / 2, center),
                new Point2F(rect.begin().x(), rect.end().y()).rotation((float) Math.PI / 2, center)
        };

        for (Point2F p : corners) {
            assertTrue(p.x() >= rotated.begin().x() - 1e-6f && p.x() <= rotated.end().x() + 1e-6f);
            assertTrue(p.y() >= rotated.begin().y() - 1e-6f && p.y() <= rotated.end().y() + 1e-6f);
        }
    }

}
