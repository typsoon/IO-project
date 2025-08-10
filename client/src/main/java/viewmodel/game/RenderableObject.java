package viewmodel.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.engine.modules.IManagingGeometryRepresentation;
import game.utility.Point2F;
import game.utility.Vector2F;

public class RenderableObject {
    IManagingGeometryRepresentation geometryRepresentation;
    Sprite sprite; //TODO think about using more generic type

    RenderableObject(IManagingGeometryRepresentation geometryRepresentation, Sprite sprite) {
        this.geometryRepresentation = geometryRepresentation;
        this.sprite = sprite;
    }

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

    public Sprite GetSprite() {
        return sprite;
    }
}
