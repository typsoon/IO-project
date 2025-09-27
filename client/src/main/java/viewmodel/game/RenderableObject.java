package viewmodel.game;

import frontend.gamestate.DrawableInfo;
import game.engine.modules.IManagingGeometryRepresentation;
import game.utility.Point2F;
import game.utility.Vector2F;
import utils.IDisposable;

public class RenderableObject implements IDisposable {
    IManagingGeometryRepresentation geometryRepresentation;
    DrawableInfo drawableInfo;

    RenderableObject(IManagingGeometryRepresentation geometryRepresentation, DrawableInfo drawableInfo) {
        this.geometryRepresentation = geometryRepresentation;
        this.drawableInfo = drawableInfo;
    }

    public void setPosition(Point2F position) {
        geometryRepresentation.setPosition(position);
        drawableInfo.setX(position.x());
        drawableInfo.setY(position.y());
    }

    public void setRotation(float angle) {
        geometryRepresentation.setRotation(angle);
        drawableInfo.setRotation(angle);
    }

    public void setVelocity(Vector2F velocity) {
        geometryRepresentation.setVelocity(velocity);
    }

    public void dispose() {
        geometryRepresentation.dispose();
    }

    public DrawableInfo getDrawable() {
        return drawableInfo;
    }
}
