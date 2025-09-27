package game.engine.entities.inventory;

import game.actions.UsageType;
import game.engine.IWorldView;
import game.engine.entities.IEntity;
import game.engine.entities.IUsageModifiers;

import java.util.ArrayList;

//think about dividing into smaller interfaces
public interface IInventory {
    int getResourceCount(Resource material);

    void addResource(Resource material, int count);

    default void addResource(ResourceInfo resourceInfo) {
        addResource(resourceInfo.resource(), resourceInfo.amount());
    }

    boolean removeResource(Resource material, int count);

    ArrayList<ISlot> getSlots();

    ISlot getSlot(int index);

    void swapSlots(int index1, int index2);

    void useSlot(int index, UsageType usageType, IWorldView view, IEntity user, IUsageModifiers modifiers);

    InventoryInfo getInventoryInfo();

    int getActiveSlotIndex();
}
