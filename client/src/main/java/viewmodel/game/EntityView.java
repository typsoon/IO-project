package viewmodel.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.engine.modules.ManagingGeometryRepresentation;
import game.utility.Point2F;
import game.utility.Vector2F;

public record EntityView(ManagingGeometryRepresentation geometryRepresentation, Sprite sprite) {
    public void setPosition(Point2F position) {
        geometryRepresentation.setPosition(position);
        sprite.setPosition(position.x(), position.y());
    }

    public void setRotation(float angle) {
        geometryRepresentation.setRotation(angle);
        sprite.setRotation(angle);
    }

    public void setVelocity(Vector2F velocity) {
        geometryRepresentation.setVelocity(velocity);
    }

    public void dispose() {
        geometryRepresentation.dispose();
        sprite.getTexture().dispose();
    }
}
