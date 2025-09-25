package game.engine.modules;

import game.utility.Point2F;
import game.utility.Rectangle2F;

import java.util.Collection;

public interface IGeometryModule {
    Collection<IMovingGeometryRepresentation> getEntitiesInArea(float x, float y, float width, float height);

    default Collection<IMovingGeometryRepresentation> getEntitiesInArea(Point2F begin, Point2F end) {
        return getEntitiesInArea(begin.x(), begin.y(), end.x() - begin.x(), end.y() - begin.y());
    }

    default Collection<IMovingGeometryRepresentation> getEntitiesInArea(Rectangle2F rectangle) {
        return getEntitiesInArea(rectangle.begin(), rectangle.end());
    }

    void cycle();

    void subscribeToCollisions(ICollisionSubscriber subscriber);

    void unsubscribeFromCollisions(ICollisionSubscriber subscriber);
}
