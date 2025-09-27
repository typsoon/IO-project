package viewmodel.game;

import frontend.gamestate.DrawableInfo;
import game.engine.modules.IManagingGeometryRepresentation;
import game.utility.Point2F;

public class RenderableObjectWithItem extends RenderableObject {

    private DrawableInfo itemInfo;

    RenderableObjectWithItem(IManagingGeometryRepresentation geometryRepresentation, DrawableInfo drawableInfo, DrawableInfo itemInfo) {
        super(geometryRepresentation, drawableInfo);
        this.itemInfo = itemInfo;
    }

    public DrawableInfo getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(DrawableInfo itemInfo) {
        this.itemInfo = itemInfo;
    }


    @Override
    public void setPosition(Point2F position) {
        super.setPosition(position);
        if (itemInfo != null) {
            itemInfo.setX(position.x() + drawableInfo.getWidth() / 2);
            itemInfo.setY(position.y() + drawableInfo.getHeight() / 2);
        }
    }

    @Override
    public void setRotation(float angle) {
        super.setRotation(angle);
        if (itemInfo != null) {
            itemInfo.setRotation(angle);
        }
    }

}
