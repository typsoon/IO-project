package frontend.concreteviews.gameplayview.overlays;

import java.util.Optional;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;

import frontend.assetsloading.ITextureManager;
import frontend.assetsloading.TexturesProvider;
import frontend.gamestate.overlays.DeathOverlayData;

public class DeathScreenOverlayManager extends Overlay<DeathOverlayData> {
    public DeathScreenOverlayManager(TexturesProvider texturesProvider, Optional<DeathOverlayData> chestOverlayData,
            ITextureManager textureManager, Viewport viewport) {
        super(texturesProvider, chestOverlayData, textureManager, viewport);
    }

    void updateTheStage(DeathOverlayData data) {
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        TextButton backButton = textureManager.getTextButton("Go Back to Main Menu");
        backButton.addListener(event -> {
            if (backButton.isPressed()) {
                return true;
            }
            return false;
        });

        table.add(backButton).pad(10).width(250).height(60);

        stage.addActor(table);
    }

}
