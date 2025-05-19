package game.engine.components;

import java.util.Collection;

public interface GeometryModule {
    Collection<GeometryRepresentation> getEntitiesInArea(float x, float y, float width, float height);
    void Cycle();
}
