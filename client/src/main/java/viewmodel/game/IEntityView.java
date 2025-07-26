package viewmodel.game;

import game.utility.Point2F;
import game.utility.Vector2F;

public interface IEntityView {
    void setPosition(Point2F position);

    void setRotation(float angle);

    void setVelocity(Vector2F velocity);

    // TODO: dispose shouldn't be here
    void dispose();
}
