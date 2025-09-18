package game.engine;

import game.engine.entities.IEntity;
import game.utility.Point2F;
import game.utility.Rectangle2F;

import java.util.Collection;
import java.util.function.Predicate;

public interface IWorldView {
    Collection<IEntity> getEntitiesInArea(float x, float y, float width, float height);

    default Collection<IEntity> getEntitiesInArea(float x, float y, float width, float height, Predicate<IEntity> filter) {
        return getEntitiesInArea(x, y, width, height).stream().filter(filter).toList();
    }

    default Collection<IEntity> getEntitiesInArea(Point2F begin, Point2F end) {
        return getEntitiesInArea(begin.x(), begin.y(), end.x() - begin.x(), end.y() - begin.y());
    }

    default Collection<IEntity> getEntitiesInArea(Point2F begin, Point2F end, Predicate<IEntity> filter) {
        return getEntitiesInArea(begin, end).stream().filter(filter).toList();
    }

    default Collection<IEntity> getEntitiesInArea(Rectangle2F rectangle) {
        return getEntitiesInArea(rectangle.begin(), rectangle.end());
    }

    default Collection<IEntity> getEntitiesInArea(Rectangle2F rectangle, Predicate<IEntity> filter) {
        return getEntitiesInArea(rectangle).stream().filter(filter).toList();
    }

    default Collection<IEntity> getEntitiesInArea(Point2F center, float radius) {
        return getEntitiesInArea(center.x() - radius, center.y() - radius, center.x() + radius, center.y() + radius);
    }

}
