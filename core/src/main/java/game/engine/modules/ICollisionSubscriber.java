package game.engine.modules;

public interface ICollisionSubscriber {
    void onCollisionBegin(IManagingGeometryRepresentation entityA, IManagingGeometryRepresentation entityB);

    void onCollisionEnd(IManagingGeometryRepresentation entityA, IManagingGeometryRepresentation entityB);
}
