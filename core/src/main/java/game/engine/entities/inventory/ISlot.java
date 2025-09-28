package game.engine.entities.inventory;

import game.engine.entities.items.IItem;
import game.engine.entities.items.ItemInfo;

public interface ISlot {
    IItem getItem();

    default boolean isEmpty() {
        return getItemCount() == 0;
    }

    int getItemCount();

    boolean addItems(int count, IItem item);

    boolean removeItems(int count);

    default SlotInfo getSlotInfo() {
        if (isEmpty()) {
            return new SlotInfo(
                    0,
                    ItemInfo.EMPTY
            );
        }
        return new SlotInfo(
                getItemCount(),
                getItem().getItemInfo()
        );
    }
}
