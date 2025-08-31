package game.engine.entities.inventory;

import game.engine.entities.items.IItem;

public class Slot {
    int itemCount;
    IItem item;
    Slot(IItem item, int itemCount) {
        this.item = item;
        this.itemCount = itemCount;
    }
    IItem getItem() {
        return item;
    }
    int getItemCount() {
        return itemCount;
    }
    boolean addItems(int count) {
        if (itemCount + count <= item.stackSize()) {
            itemCount += count;
            return true;
        }
        return false;
    }
    boolean removeItems(int count) {
        if (itemCount - count >= 0) {
            itemCount -= count;
            return true;
        }
        return false;
    }
}
