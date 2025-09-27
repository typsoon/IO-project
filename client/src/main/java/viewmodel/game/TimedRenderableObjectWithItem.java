package viewmodel.game;

import frontend.gamestate.DrawableInfo;
import game.engine.modules.IManagingGeometryRepresentation;

public class TimedRenderableObjectWithItem extends RenderableObjectWithItem {
    public float timeSinceUpdate = 0f;

    public TimedRenderableObjectWithItem(IManagingGeometryRepresentation geometryRepresentation, DrawableInfo drawableInfo, DrawableInfo itemInfo) {
        super(geometryRepresentation, drawableInfo, itemInfo);
    }

    public TimedRenderableObjectWithItem(IManagingGeometryRepresentation geometryRepresentation, DrawableInfo drawableInfo) {
        super(geometryRepresentation, drawableInfo, null);
    }
}
