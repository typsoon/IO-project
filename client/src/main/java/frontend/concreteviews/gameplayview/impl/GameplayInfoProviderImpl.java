package frontend.concreteviews.gameplayview.impl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import frontend.concreteviews.gameplayview.IGameplayInfoProvider;
import game.engine.entities.inventory.InventoryInfo;
import game.utility.Point2F;

import java.util.function.Supplier;

public class GameplayInfoProviderImpl implements IGameplayInfoProvider {
    private final Viewport viewport;
    private final Supplier<Point2F> centerOfInterestGetter;
    private final Supplier<InventoryInfo> inventoryInfoGetter;
    private final Supplier<Integer> hpValueGetter;
    private final Supplier<Integer> maxHpValueGetter;
    private int activeSlotIndex = 0;

    public GameplayInfoProviderImpl(final Viewport viewport, final Supplier<Point2F> centerOfInterestGetter, final Supplier<InventoryInfo> inventoryInfoGetter,
                                    final Supplier<Integer> hpValueGetter, final Supplier<Integer> maxHpValueGetter) {
        this.viewport = viewport;
        this.centerOfInterestGetter = centerOfInterestGetter;
        this.inventoryInfoGetter = inventoryInfoGetter;
        this.hpValueGetter = hpValueGetter;
        this.maxHpValueGetter = maxHpValueGetter;
    }

    @Override
    public final Point2F castScreenCoordinatesToGameCoordinates(final float worldX, final float worldY) {
        var projectionRes = viewport.unproject(new Vector2(worldX, worldY));
        // var logger = Logger.getGlobal();
        // logger.info("%f %f world %s game".formatted(worldX, worldY, projectionRes));

        return new Point2F(projectionRes.x, projectionRes.y);
    }

    @Override
    public final Point2F getCenterOfInterest() {
        return centerOfInterestGetter.get();
    }

    @Override
    public int getActiveSlotIndex() {
        return activeSlotIndex;
    }

    @Override
    public void setActiveSlotIndex(int newIndex) {
        this.activeSlotIndex = newIndex;
    }

    @Override
    public InventoryInfo getInventoryInfo() {
        return inventoryInfoGetter.get();
    }

    @Override
    public int getHpValue() {
        return hpValueGetter.get();
    }

    @Override
    public int getMaxHpValue() {
        return maxHpValueGetter.get();
    }
}
