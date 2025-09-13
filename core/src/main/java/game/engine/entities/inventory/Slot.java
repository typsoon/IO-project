package game.engine.entities.inventory;

import game.engine.entities.items.IItem;

public class Slot implements ISlot {
    int itemCount;
    IItem item;

    Slot() {
        item = null;
        itemCount = 0;
    }

    Slot(IItem item, int itemCount) {
        this.item = item;
        this.itemCount = itemCount;
    }

    @Override
    public IItem getItem() {
        return item;
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    @Override
    public boolean addItems(int count, IItem item) {
        if (this.item == null && count <= item.stackSize()) {
            this.item = item;
            itemCount = count;
            return true;
        }
        //remember about this when creating stackable items
        else if (this.item == item && itemCount + count <= item.stackSize()) {
            itemCount += count;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeItems(int count) {
        if (this.isEmpty()) {
            return false;
        }
        if (itemCount - count >= 0) {
            itemCount -= count;
            if (itemCount == 0) {
                item = null;
            }
            return true;
        }
        return false;
    }
}
