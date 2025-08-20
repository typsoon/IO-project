package frontend.assetsloading;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import frontend.gamestate.EntityVisibleState;
import game.engine.entities.EntityGroupID;

public interface TexturesProvider {
    TextureRegion getTextureRegion(EntityGroupID groupID, EntityVisibleState state, float stateTime);
}
