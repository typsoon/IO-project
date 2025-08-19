package viewmodel.game;

import frontend.gamestate.DrawableInfo;
import game.engine.modules.IManagingGeometryRepresentation;

public class TimedRenderableObject extends RenderableObject {
    public float timeSinceUpdate = 0f;
    public TimedRenderableObject(IManagingGeometryRepresentation geometryRepresentation, DrawableInfo drawableInfo) {
        super(geometryRepresentation, drawableInfo);
    }
}
