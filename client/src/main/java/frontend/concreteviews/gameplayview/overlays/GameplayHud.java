package frontend.concreteviews.gameplayview.overlays;

import java.util.ArrayList;
import java.util.Optional;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import frontend.assetsloading.ITextureManager;
import frontend.assetsloading.TexturesProvider;
import frontend.concreteviews.gameplayview.IGameplayInfoProvider;

public class GameplayHud extends Overlay<IGameplayInfoProvider> {
    private final IGameplayInfoProvider gameplayInfoProvider;
    private final ITextureManager textureManager;

    private Label hpLabel;
    private ProgressBar hpBar;
    ArrayList<TextButton> hotbarSlots = new ArrayList<>();
    ArrayList<Label> resourceLabels = new ArrayList<>();

    public GameplayHud(IGameplayInfoProvider gameplayInfoProvider, InputMultiplexer multiplexer,
            ITextureManager textureManager, TexturesProvider texturesProvider, Viewport viewport,
            EventListener listener) {
        super(texturesProvider, () -> Optional.of(gameplayInfoProvider), textureManager, viewport, listener);

        this.gameplayInfoProvider = gameplayInfoProvider;
        this.textureManager = textureManager;
        createHud();
    }

    private void createHud() {
        hpBar = textureManager.getProgressBar(0, 100, 1, false);
        hpBar.setColor(Color.RED);
        hpBar.setWidth(200);
        hpLabel = textureManager.getHeading("HP: 100/100");
        hpLabel.setAlignment(Align.center);
        float maxWidth = 200;
        float scale = Math.min(1f, maxWidth / hpLabel.getPrefWidth());
        hpLabel.setFontScale(scale);
        Table hudTable = textureManager.getTable();
        hudTable.setFillParent(true);
        hudTable.top().left();
        hudTable.add(hpLabel).width(200).pad(10).left();
        hudTable.row();
        hudTable.add(hpBar).width(200).pad(10).left();

        hudTable.row();

        Table hotbarTable = textureManager.getTable();
        hotbarTable.top().right();
        hotbarTable.setFillParent(true);

        for (int i = 0; i < 5; i++) {
            TextButton slot = textureManager.getTextButton("");
            slot.getLabel().setFontScale(0.3f);
            hotbarSlots.add(slot);
            hotbarTable.add(slot).size(64, 64).pad(5);
            slot.setColor(Color.BLACK);
        }

        Table resourcesTable = textureManager.getTable();
        resourcesTable.bottom().right();
        resourcesTable.setFillParent(true);

        int resourceCount = 10; // this should be from file or server
        for (int i = 0; i < resourceCount; i++) {
            Label resourceLabel = textureManager.getHeading("");
            resourceLabel.setAlignment(Align.right);
            resourceLabel.setFontScale(0.3f);
            resourceLabels.add(resourceLabel);
            resourcesTable.add(resourceLabel).pad(5).right();
            resourcesTable.row();
        }

        stage.addActor(hudTable);
        stage.addActor(hotbarTable);
        stage.addActor(resourcesTable);
    }

    @Override
    void updateTheStage(IGameplayInfoProvider data) {
        hpBar.setValue((float) gameplayInfoProvider.getHpValue() / (gameplayInfoProvider.getMaxHpValue()) * 100);
        hpLabel.setText(String.format("HP: %3d out of %3d", gameplayInfoProvider.getHpValue(),
                gameplayInfoProvider.getMaxHpValue()));
        for (int i = 0; i < gameplayInfoProvider.getInventoryInfo().slotNum(); i++) {
            if (i < gameplayInfoProvider.getInventoryInfo().slots().getData().length) {
                hotbarSlots.get(i).setColor(Color.GRAY);
                if (gameplayInfoProvider.getInventoryInfo().slots().getData()[i].amount() > 0) {
                    hotbarSlots.get(i)
                            .setText(gameplayInfoProvider.getInventoryInfo().slots().getData()[i].item().name());
                } else {
                    hotbarSlots.get(i).setText("");
                }
            } else {
                hotbarSlots.get(i).setColor(Color.BLACK);
                hotbarSlots.get(i).setText("");
            }
        }

        for (int i = 0; i < gameplayInfoProvider.getInventoryInfo().resources().getData().length; i++) {
            resourceLabels.get(i)
                    .setText(gameplayInfoProvider.getInventoryInfo().resources().getData()[i].amount() + " x " +
                            gameplayInfoProvider.getInventoryInfo().resources().getData()[i].resource().name());
        }
    }
}
