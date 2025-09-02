package frontend.concreteviews.gameplayview;

import java.util.Collection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import frontend.assetsloading.TexturesProvider;
import frontend.gamestate.DrawableInfo;

public class EntitiesDrawer {
    private final SpriteBatch spriteBatch = new SpriteBatch();
    private final TexturesProvider texturesProvider;
    private final Viewport viewport;

    EntitiesDrawer(TexturesProvider texturesProvider, Viewport viewport) {
        this.texturesProvider = texturesProvider;
        this.viewport = viewport;

    }

    void drawEntities(Collection<DrawableInfo> drawables) {
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        for (DrawableInfo drawableInfo : drawables) {
            var textureRegion = texturesProvider.getTextureRegion(drawableInfo.getEntityGroupID(),
                    drawableInfo.getState(), drawableInfo.getStateTime());

            final float x = drawableInfo.getX() - drawableInfo.getWidth() / 2;
            final float y = drawableInfo.getY() - drawableInfo.getHeight() / 2;
            spriteBatch.draw(textureRegion,
                    x, y,
                    drawableInfo.getOriginX(),
                    drawableInfo.getOriginY(),
                    drawableInfo.getWidth(), drawableInfo.getHeight(),
                    drawableInfo.getScaleX(),
                    drawableInfo.getScaleY(),
                    (float) Math.toDegrees(drawableInfo.getRotation()));
        }

        spriteBatch.end();
    }

}
