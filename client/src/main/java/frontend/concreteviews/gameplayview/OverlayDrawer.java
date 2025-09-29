package frontend.concreteviews.gameplayview;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.viewport.Viewport;

import frontend.assetsloading.ITextureManager;
import frontend.assetsloading.TexturesProvider;
import frontend.concreteviews.gameplayview.overlays.ChestOverlayManager;
import frontend.concreteviews.gameplayview.overlays.DeathScreenOverlayManager;
import frontend.concreteviews.gameplayview.overlays.GameplayHud;
import frontend.gamestate.IReadOnlyOverlaysData;

public class OverlayDrawer {
    private final IReadOnlyOverlaysData overlaysData;

    private final GameplayHud gameplayHud;
    private final ChestOverlayManager chestOverlayManager;
    private final DeathScreenOverlayManager deathScreenOverlayManager;
    private final InputProcessor idleInputProcessor = new InputAdapter();
    private InputProcessor activeProcessor = idleInputProcessor;

    public OverlayDrawer(TexturesProvider texturesProvider,
            ITextureManager textureManager, Viewport viewport, IReadOnlyOverlaysData overlaysData,
            InputMultiplexer multiplexer, IGameplayInfoProvider gameplayInfoProvider, EventListener listener) {
        this.overlaysData = overlaysData;

        this.gameplayHud = new GameplayHud(gameplayInfoProvider, multiplexer, textureManager, texturesProvider,
                viewport, listener);

        this.chestOverlayManager = new ChestOverlayManager(texturesProvider, overlaysData::getChestOverlayData,
                textureManager, viewport, listener);
        this.deathScreenOverlayManager = new DeathScreenOverlayManager(texturesProvider,
                overlaysData::getDeathOverlayData,
                textureManager, viewport, listener);

        multiplexer.addProcessor(activeProcessor);
    }

    public final void render(float deltaTime) {
        gameplayHud.tryRender(deltaTime).ifPresent(this::setActiveProcessor);
        chestOverlayManager.tryRender(deltaTime).ifPresent(this::setActiveProcessor);
        deathScreenOverlayManager.tryRender(deltaTime).ifPresent(this::setActiveProcessor);
    }

    private final void setActiveProcessor(InputProcessor activeProcessor) {
        this.activeProcessor = activeProcessor;
    }
}
