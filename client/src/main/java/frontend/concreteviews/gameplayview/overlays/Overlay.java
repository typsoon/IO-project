package frontend.concreteviews.gameplayview.overlays;

import java.util.Optional;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

import frontend.assetsloading.ITextureManager;
import frontend.assetsloading.TexturesProvider;

abstract class Overlay<T> implements Disposable {
    protected final Optional<T> overlayData;
    protected final ITextureManager textureManager;
    protected final TexturesProvider texturesProvider;
    protected final Stage stage;

    public Overlay(TexturesProvider texturesProvider, Optional<T> chestOverlayData,
            ITextureManager textureManager) {
        this.texturesProvider = texturesProvider;
        this.overlayData = chestOverlayData;
        this.textureManager = textureManager;

        this.stage = new Stage();
    }

    abstract void updateTheStage(T data);

    public void render(float deltaTime) {
        if (overlayData.isEmpty()) {
            return;
        }

        updateTheStage(overlayData.get());

        stage.act(deltaTime);
        stage.draw();
    }
}
