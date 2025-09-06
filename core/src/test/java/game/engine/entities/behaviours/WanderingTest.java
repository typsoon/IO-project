package game.engine.entities.behaviours;

import game.engine.IWorldView;
import game.engine.modules.IMovingGeometryRepresentation;
import game.utility.Point2F;
import game.utility.Vector2F;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class WanderingTest {

    private IMovingGeometryRepresentation geometry;
    private IWorldView worldView;

    @BeforeEach
    void setUp() {
        geometry = mock(IMovingGeometryRepresentation.class);
        worldView = mock(IWorldView.class);
    }

    @Test
    void testMovesTowardStartWhenOutOfRange() {
        Point2F start = new Point2F(0, 0);
        Wandering wandering = new Wandering(start, 2.0f, 5.0f);

        Point2F currentPos = new Point2F(10, 0);
        when(geometry.getPosition()).thenReturn(currentPos);

        wandering.behave(geometry, worldView);

        verify(geometry).move(argThat(v -> Math.abs(v.x() + 2.0f) < 0.001 && Math.abs(v.y()) < 0.001));
    }

    @Test
    void testMovesInRandomDirectionWhenInRange() {
        Point2F start = new Point2F(0, 0);
        Wandering wandering = new Wandering(start, 1.0f, 50.0f);

        Point2F currentPos = new Point2F(1, 1);
        when(geometry.getPosition()).thenReturn(currentPos);

        wandering.behave(geometry, worldView);

        verify(geometry).move(argThat(v ->
                !(Math.abs(v.x()) < 0.0001 && Math.abs(v.y()) < 0.0001)
        ));
    }

    @Test
    void testUsesCounterToContinueMovingWithSameVelocity() {
        Point2F start = new Point2F(0, 0);
        Wandering wandering = new Wandering(start, 1.0f, 5.0f);

        Point2F currentPos = new Point2F(10, 0);
        when(geometry.getPosition()).thenReturn(currentPos);

        wandering.behave(geometry, worldView);

        clearInvocations(geometry);

        wandering.behave(geometry, worldView);

        verify(geometry, times(1)).move(any(Vector2F.class));
    }

    @Test
    void testVelocityPointsBackToStartWhenFarAway() {
        Point2F start = new Point2F(0, 0);
        Wandering wandering = new Wandering(start, 1.0f, 2.0f);

        Point2F currentPos = new Point2F(3, 4);
        when(geometry.getPosition()).thenReturn(currentPos);

        wandering.behave(geometry, worldView);

        verify(geometry).move(argThat(v -> Math.abs(v.x() + 0.6f) < 0.01 && Math.abs(v.y() + 0.8f) < 0.01));
    }
}