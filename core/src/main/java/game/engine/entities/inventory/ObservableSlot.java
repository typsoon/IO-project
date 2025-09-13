package game.engine.entities.inventory;

import game.engine.entities.items.IItem;

import java.util.function.Consumer;

public class ObservableSlot implements ISlot {

    Consumer<IItem> onItemConsumed;
    ISlot slot;

    public ObservableSlot(Consumer<IItem> onItemConsumed, ISlot slot) {
        this.onItemConsumed = onItemConsumed;
        this.slot = slot;
    }

    @Override
    public IItem getItem() {
        return slot.getItem();
    }

    @Override
    public int getItemCount() {
        return slot.getItemCount();
    }

    @Override
    public boolean addItems(int count, IItem item) {
        return slot.addItems(count, item);
    }

    @Override
    public boolean removeItems(int count) {
        IItem lastItem = slot.getItem();
        boolean ret = slot.removeItems(count);
        if (ret && slot.isEmpty()) {
            onItemConsumed.accept(lastItem);
        }
        return ret;
    }
}
