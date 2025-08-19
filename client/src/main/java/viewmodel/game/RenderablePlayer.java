package viewmodel.game;

import frontend.gamestate.DrawableInfo;
import game.engine.PlayerConfig;
import game.engine.modules.IManagingGeometryRepresentation;

public class RenderablePlayer extends RenderableObject implements IPlayerData {
    private int currentHp;
    private int maxHp;
    public RenderablePlayer(PlayerConfig config, IManagingGeometryRepresentation geometryRepresentation, DrawableInfo drawableInfo) {
        super(geometryRepresentation, drawableInfo);
        currentHp = 0;
        maxHp = 0;
    }
    @Override
    public int getHpValue() {
        return currentHp;
    }
    @Override
    public int getMaxHpValue() {
        return maxHp;
    }
    @Override
    public DrawableInfo getDrawableInfo() {
        return drawableInfo;
    }
    //this will hold information about player eq etc.
}
