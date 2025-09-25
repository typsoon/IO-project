package game.engine.entities.geometry;

import game.engine.entities.IEntity;

public interface ICollisionAware {
    void onCollisionBegin(IEntity other);

    void onCollisionEnd(IEntity other);
}
