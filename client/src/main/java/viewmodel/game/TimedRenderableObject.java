package viewmodel.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.engine.modules.IManagingGeometryRepresentation;

public class TimedRenderableObject extends RenderableObject {
    public float timeSinceUpdate = 0f;
    public TimedRenderableObject(IManagingGeometryRepresentation geometryRepresentation, Sprite sprite) {
        super(geometryRepresentation, sprite);
    }
}
