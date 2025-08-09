package viewmodel.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.engine.entities.SpriteID;

public interface ISpriteFactory {
    Sprite createSprite(SpriteID spriteID);
}
