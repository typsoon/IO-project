package viewmodel.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.engine.PlayerConfig;
import game.engine.modules.IManagingGeometryRepresentation;

public class RenderablePlayer extends RenderableObject implements IPlayerData {
    private int currentHp;
    private int maxHp;
    public RenderablePlayer(PlayerConfig config, IManagingGeometryRepresentation geometryRepresentation, Sprite sprite) {
        super(geometryRepresentation, sprite);
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
    public Sprite getSprite() {
        return sprite;
    }
    //this will hold information about player eq etc.
}
