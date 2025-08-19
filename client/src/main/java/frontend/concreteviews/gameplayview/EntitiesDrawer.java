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

    void drawEntities(Collection<DrawableInfo> drawables) {
        spriteBatch.begin();
        for (DrawableInfo drawableInfo : drawables) {
            var textureRegion = texturesProvider.getTextureRegion(drawableInfo.getEntityGroupID(),
                    drawableInfo.getState(), drawableInfo.getStateTime());

            spriteBatch.draw(textureRegion,
                    drawableInfo.getX(), drawableInfo.getY(),
                    drawableInfo.getOriginX(), drawableInfo.getOriginY(),
                    drawableInfo.getWidth(), drawableInfo.getHeight(),
                    drawableInfo.getScaleX(),
                    drawableInfo.getScaleY(),
                    drawableInfo.getRotation());
        }

        spriteBatch.end();
    }

}
