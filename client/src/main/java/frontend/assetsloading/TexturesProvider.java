package frontend.assetsloading;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import game.engine.entities.SpriteID;

public interface TexturesProvider {
    <T extends Enum<T>> TextureRegion getTextureRegion(SpriteID groupID, Enum<T> state, float stateTime);
}
