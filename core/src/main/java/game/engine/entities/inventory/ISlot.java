package game.engine.entities.inventory;

import game.engine.entities.items.IItem;

public interface ISlot {
    IItem getItem();

    default boolean isEmpty(){
        return getItemCount() == 0;
    }

    int getItemCount();

    boolean addItems(int count, IItem item);

    boolean removeItems(int count);
}
