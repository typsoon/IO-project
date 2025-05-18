package game.engine.components;

import java.util.Collection;

public interface GeometryModule {
    public Collection<Entity> getEntitiesInArea(float x, float y, float width, float height);
}
