package frontend.assetsloading;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import game.engine.entities.EntityGroupID;

public interface TexturesProvider {
    <T extends Enum<T>> TextureRegion getTextureRegion(EntityGroupID groupID, Enum<T> state, float stateTime);
}
