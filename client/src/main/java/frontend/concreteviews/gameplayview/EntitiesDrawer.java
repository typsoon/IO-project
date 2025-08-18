package frontend.concreteviews.gameplayview;

import java.util.Collection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import frontend.assetsloading.TexturesProvider;
import frontend.gamestate.DrawableInfo;

public class EntitiesDrawer {
    private final SpriteBatch spriteBatch = new SpriteBatch();
    private final TexturesProvider texturesProvider;

    EntitiesDrawer(TexturesProvider texturesProvider) {
        this.texturesProvider = texturesProvider;
    }

    void drawEntities(Collection<DrawableInfo<?>> drawables) {
        spriteBatch.begin();
        for (DrawableInfo<?> drawableInfo : drawables) {
            var textureRegion = texturesProvider.getTextureRegion(drawableInfo.entityGroupID(),
                    drawableInfo.state(), drawableInfo.stateTime());

            spriteBatch.draw(textureRegion,
                    drawableInfo.x(), drawableInfo.y(),
                    drawableInfo.originX(), drawableInfo.originY(),
                    drawableInfo.width(), drawableInfo.height(),
                    drawableInfo.scaleX(),
                    drawableInfo.scaleY(),
                    drawableInfo.rotation());
        }

        spriteBatch.end();
    }

}
