package game.engine.components;

import java.awt.geom.Point2D;

public interface GeometryRepresentation {
    Point2D getPosition();
    void move(float dx, float dy);
}
