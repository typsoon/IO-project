package game.engine.modules;


//TOdo change name
public interface ManagingGeometryRepresentation extends MovingGeometryRepresentation {
    void setPosition(float x, float y);
    void setVelocity(float vx, float vy);
    void setRotation(float angle);
    void dispose();
}
