package frontend.concreteviews.gameplayview.overlays;

import java.util.Optional;
import java.util.function.Supplier;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

import frontend.assetsloading.ITextureManager;
import frontend.assetsloading.TexturesProvider;

abstract class Overlay<T> implements Disposable {
    protected final Supplier<Optional<T>> overlayDataSupplier;
    protected final ITextureManager textureManager;
    protected final TexturesProvider texturesProvider;
    protected final Stage stage;

    public Overlay(TexturesProvider texturesProvider, Supplier<Optional<T>> chestOverlayData,
            ITextureManager textureManager, Viewport viewport, EventListener listener) {
        this.texturesProvider = texturesProvider;
        this.overlayDataSupplier = chestOverlayData;
        this.textureManager = textureManager;

        this.stage = new Stage(viewport);
        stage.addListener(listener);
    }

    abstract void updateTheStage(T data);

    public final Optional<InputProcessor> tryRender(float deltaTime) {
        var overlayData = overlayDataSupplier.get();
        if (overlayData.isEmpty()) {
            return Optional.empty();
        }

        updateTheStage(overlayData.get());

        stage.act(deltaTime);
        stage.draw();
        return Optional.of(stage);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
