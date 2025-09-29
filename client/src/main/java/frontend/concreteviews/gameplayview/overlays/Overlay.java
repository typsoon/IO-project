package frontend.concreteviews.gameplayview.overlays;

import java.util.Optional;
import java.util.logging.Logger;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

import frontend.assetsloading.ITextureManager;
import frontend.assetsloading.TexturesProvider;

abstract class Overlay<T> implements Disposable {
    protected final Optional<T> overlayData;
    protected final ITextureManager textureManager;
    protected final TexturesProvider texturesProvider;
    protected final Stage stage;

    public Overlay(TexturesProvider texturesProvider, Optional<T> chestOverlayData,
            ITextureManager textureManager, Viewport viewport) {
        this.texturesProvider = texturesProvider;
        this.overlayData = chestOverlayData;
        this.textureManager = textureManager;

        this.stage = new Stage(viewport);
    }

    abstract void updateTheStage(T data);

    public final Optional<InputProcessor> tryRender(float deltaTime) {
        if (overlayData.isEmpty()) {
            return Optional.empty();
        }

        updateTheStage(overlayData.get());

        stage.act(deltaTime);
        stage.draw();
        Logger.getGlobal().info(this.getClass().toString());
        return Optional.of(stage);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
