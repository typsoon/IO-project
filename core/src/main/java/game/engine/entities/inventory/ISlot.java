package game.engine.entities.inventory;

import game.engine.entities.items.IItem;

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
                    null
            );
        }
        return new SlotInfo(
                getItemCount(),
                getItem().getItemInfo()
        );
    }
}
