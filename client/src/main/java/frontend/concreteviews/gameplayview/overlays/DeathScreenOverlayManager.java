package frontend.concreteviews.gameplayview.overlays;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Logger;

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
            ITextureManager textureManager, Viewport viewport) {
        super(texturesProvider, chestOverlayData, textureManager, viewport);

        table = textureManager.getTable();
        createTheView();
    }

    private void createTheView() {
        table.setFillParent(true);
        table.center();

        TextButton backButton = textureManager.getTextButton("Go Back to Main Menu");
        backButton.addListener(event -> {
            if (backButton.isPressed()) {
                backButton.fire(new ExitTheGameEvent());
                return true;
            }
            return false;
        });

        table.add(backButton).pad(10).width(250).height(60);

        stage.addActor(table);
    }

    void updateTheStage(DeathOverlayData data) {
        Logger.getGlobal().info("I am here");
    }

}
