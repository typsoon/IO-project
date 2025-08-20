package viewmodel.game;

import frontend.gamestate.DrawableInfo;
import game.engine.PlayerConfig;
import game.engine.modules.IManagingGeometryRepresentation;
import game.utility.Vector2F;

public class RenderablePlayer extends RenderableObject implements IPlayerData {
    private int currentHp;
    private int maxHp;
    private Vector2F range;
    public RenderablePlayer(PlayerConfig config, IManagingGeometryRepresentation geometryRepresentation, DrawableInfo drawableInfo) {
        super(geometryRepresentation, drawableInfo);
        currentHp = 0;
        maxHp = 0;
        range = new Vector2F(0, 0);
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
    public Vector2F getRange() {
        return range;
    }
    @Override
    public DrawableInfo getDrawableInfo() {
        return drawableInfo;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setRange(Vector2F range) {
        this.range = range;
    }
}
