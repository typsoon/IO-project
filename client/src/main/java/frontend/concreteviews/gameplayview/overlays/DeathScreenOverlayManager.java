package frontend.concreteviews.gameplayview.overlays;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Logger;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;

import frontend.assetsloading.ITextureManager;
import frontend.assetsloading.TexturesProvider;
import frontend.concreteviews.gameplayview.events.GameplayEvents.ExitTheGameEvent;
import frontend.gamestate.overlays.DeathOverlayData;

public class DeathScreenOverlayManager extends Overlay<DeathOverlayData> {
    private final Table table;

    public DeathScreenOverlayManager(TexturesProvider texturesProvider,
            Supplier<Optional<DeathOverlayData>> chestOverlayData,
            ITextureManager textureManager, Viewport viewport, EventListener listener) {
        super(texturesProvider, chestOverlayData, textureManager, viewport, listener);

        table = textureManager.getTable();
        createTheView();
    }

    private void createTheView() {
        table.setFillParent(true);
        table.center();

        TextButton backButton = textureManager.getTextButton("Go Back to Main Menu");

        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                    final int button) {
                Logger.getGlobal().info("Trying to go back to mainMenu");
                backButton.fire(new ExitTheGameEvent());
                return true;
            }
        });

        table.add(backButton).pad(10).width(250).height(60);

        stage.addActor(table);
    }

    void updateTheStage(DeathOverlayData data) {
        // Logger.getGlobal().info("I am here");
    }

}
