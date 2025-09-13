package game.engine.modules;

import game.utility.Point2F;
import game.utility.Vector2F;

public interface IGeometryRepresentation {
    Point2F getPosition();

    Vector2F getVelocity();

    float getRotation();
}
