package game.engine.components;

import game.Utility.Vector2F;

import java.awt.geom.Point2D;

public interface GeometryRepresentation {
    Point2D getPosition();
    void move(float dx, float dy);
    default void move(Vector2F vector){
        move(vector.x(), vector.y());
    }
}
