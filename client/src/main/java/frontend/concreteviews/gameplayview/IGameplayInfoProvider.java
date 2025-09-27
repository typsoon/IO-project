package frontend.concreteviews.gameplayview;

import game.engine.entities.inventory.InventoryInfo;
import game.utility.Point2F;

public interface IGameplayInfoProvider {
    Point2F castScreenCoordinatesToGameCoordinates(float worldX, float worldY);

    /**
     * @return when in game it returns the player position
     */
    Point2F getCenterOfInterest();

    int getActiveSlotIndex();

    void setActiveSlotIndex(int newIndex);

    InventoryInfo getInventoryInfo();

    int getHpValue();

    int getMaxHpValue();
}
