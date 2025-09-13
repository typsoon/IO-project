package game.engine.entities.behaviours;

import game.engine.IWorldView;
import game.engine.modules.IMovingGeometryRepresentation;
import game.utility.Point2F;
import game.utility.Vector2F;

import static game.utility.Point2F.distance;

public class Wandering implements IBehaviour {

    private final Point2F startPosition;
    private final float speed;
    private final float range;
    private float counter = 0;
    private Vector2F velocity;

    public Wandering(Point2F startPosition, float speed, float range) {
        this.startPosition = startPosition;
        this.speed = speed;
        this.range = range;
    }

    @Override
    public void behave(IMovingGeometryRepresentation geometryRepresentation, IWorldView worldView) {
        if (counter > 0) {
            counter--;
            geometryRepresentation.move(velocity);
            return;
        }
        counter = 30;
        if (distance(geometryRepresentation.getPosition(), startPosition) > range) {
            Point2F direction = startPosition.subtract(geometryRepresentation.getPosition());
            float distanceToStart = distance(geometryRepresentation.getPosition(), startPosition);
            if (distanceToStart > 0) {
                direction = new Point2F(direction.x() / distanceToStart, direction.y() / distanceToStart);
            }
            velocity = new Vector2F(direction).multiply(speed);
        } else {
            Vector2F randomDirection = new Vector2F(
                    (float) (Math.random() * 2 - 1), // Random x between -1 and 1
                    (float) (Math.random() * 2 - 1)  // Random y between -1 and 1
            );
            velocity = randomDirection.multiply(speed);
        }
        geometryRepresentation.move(velocity);
    }
}
