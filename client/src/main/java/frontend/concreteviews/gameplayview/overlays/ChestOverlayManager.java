package frontend.concreteviews.gameplayview.overlays;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import frontend.assetsloading.ITextureManager;
import frontend.assetsloading.TexturesProvider;
import frontend.gamestate.overlays.ChestOverlayData;

public class ChestOverlayManager extends Overlay<ChestOverlayData> {
    private static final int SLOT_SIZE = 64; // size of each inventory slot
    private static final int COLUMNS = 9; // how many items per row (like Minecraft chest row)

    public ChestOverlayManager(TexturesProvider texturesProvider, Supplier<Optional<ChestOverlayData>> chestOverlayData,
            ITextureManager textureManager, Viewport viewport, EventListener listener) {
        super(texturesProvider, chestOverlayData, textureManager, viewport, listener);
    }

    void updateTheStage(ChestOverlayData chestOverlayData) {
        Collection<TextureRegion> items = chestOverlayData.items().stream()
                .map(item -> texturesProvider.getTextureRegion(item.entityGroup(), item.visibleState(),
                        item.stateTime()))
                .toList();

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        int col = 0;
        for (var texture : items) {
            // Wrap texture into an Image actor
            Image itemImage = new Image(new TextureRegionDrawable(texture));
            itemImage.setSize(SLOT_SIZE, SLOT_SIZE);

            // Optional: add background slot
            Table slot = new Table();
            slot.setBackground("default-round"); // requires a drawable in your skin
            slot.add(itemImage).size(SLOT_SIZE, SLOT_SIZE);

            table.add(slot).size(SLOT_SIZE, SLOT_SIZE).pad(4);

            col++;
            if (col >= COLUMNS) {
                col = 0;
                table.row();
            }
        }

        stage.addActor(table);
    }
}
