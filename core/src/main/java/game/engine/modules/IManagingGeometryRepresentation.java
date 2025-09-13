package game.engine.modules;


import game.utility.Point2F;
import game.utility.Vector2F;

//TOdo: change name
public interface IManagingGeometryRepresentation extends IMovingGeometryRepresentation {
    void setPosition(float x, float y);

    default void setPosition(Point2F position) {
        setPosition(position.x(), position.y());
    }

    void setVelocity(float vx, float vy);

    default void setVelocity(Vector2F velocity) {
        setVelocity(velocity.x(), velocity.y());
    }

    void setRotation(float angle);

    void dispose();
}
