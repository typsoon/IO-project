package game.engine.modules;

import game.utility.Point2F;
import game.utility.Rectangle2F;
import java.util.Collection;

public interface GeometryModule {
    Collection<MovingGeometryRepresentation> getEntitiesInArea(float x, float y, float width, float height);
    default Collection<MovingGeometryRepresentation> getEntitiesInArea(Point2F begin, Point2F end) {
        return getEntitiesInArea(begin.x(), begin.y(), end.x() - begin.x(), end.y() - begin.y());
    }
    default Collection<MovingGeometryRepresentation> getEntitiesInArea(Rectangle2F rectangle) {
        return getEntitiesInArea(rectangle.begin(), rectangle.end());
    }
    void Cycle();
}
