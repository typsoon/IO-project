package game.engine;

import game.engine.entities.IEntity;

import java.util.Collection;

public interface IWorldView {
    Collection<IEntity> getEntitiesInArea(float x, float y, float width, float height);
}
