package viewmodel.game;

import frontend.gamestate.DrawableInfo;
import game.engine.PlayerConfig;
import game.engine.entities.inventory.InventoryInfo;
import game.engine.modules.IManagingGeometryRepresentation;
import game.utility.Vector2F;

public class RenderablePlayer extends RenderableObjectWithItem implements IPlayerData {
    private int currentHp;
    private int maxHp;
    private Vector2F range;
    private InventoryInfo inventoryInfo;

    public RenderablePlayer(PlayerConfig config, IManagingGeometryRepresentation geometryRepresentation,
                            DrawableInfo drawableInfo, DrawableInfo itemInfo) {
        super(geometryRepresentation, drawableInfo, itemInfo);
        currentHp = 0;
        maxHp = 0;
        range = new Vector2F(0, 0);
    }

    public RenderablePlayer(PlayerConfig config, IManagingGeometryRepresentation geometryRepresentation,
                            DrawableInfo drawableInfo) {
        this(config, geometryRepresentation, drawableInfo, null);
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
    public InventoryInfo getInventoryInfo() {
        return inventoryInfo;
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

    public void setInventoryInfo(InventoryInfo inventoryInfo) {
        this.inventoryInfo = inventoryInfo;
    }
}
