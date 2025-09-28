package frontend.concreteviews.gameplayview.overlays;

import java.util.Optional;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import frontend.assetsloading.ITextureManager;
import frontend.assetsloading.TexturesProvider;
import frontend.gamestate.overlays.DeathOverlayData;

public class DeathScreenOverlayManager extends Overlay<DeathOverlayData> {
    public DeathScreenOverlayManager(TexturesProvider texturesProvider, Optional<DeathOverlayData> chestOverlayData,
            ITextureManager textureManager) {
        super(texturesProvider, chestOverlayData, textureManager);
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

    @Override
    public void dispose() {
        stage.dispose();
    }
}
