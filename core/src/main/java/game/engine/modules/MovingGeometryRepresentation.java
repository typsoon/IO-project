package game.engine.modules;

import game.utility.Vector2F;

public interface MovingGeometryRepresentation extends GeometryRepresentation {
    void move(float dx, float dy);
    default void move(Vector2F vector){
        move(vector.x(), vector.y());
    }
}
