package game.engine.components;

import game.utility.Point2F;
import game.utility.Vector2F;

public interface GeometryRepresentation {
    Point2F getPosition();
    Vector2F getVelocity();
    void move(float dx, float dy);
    default void move(Vector2F vector){
        move(vector.x(), vector.y());
    }
}
