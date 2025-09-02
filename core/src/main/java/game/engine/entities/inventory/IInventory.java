package game.engine.entities.inventory;

import game.actions.UsageType;
import game.engine.IWorldView;
import game.engine.entities.IEntity;
import game.engine.entities.items.UsageModifiers;

import java.util.ArrayList;

//think about dividing into smaller interfaces
public interface IInventory {
    int getMaterialsCount(Materials material);

    void addMaterials(Materials material, int count);

    boolean removeMaterials(Materials material, int count);

    ArrayList<ISlot> getSlots();

    ISlot getSlot(int index);

    void swapSlots(int index1, int index2);

    void UseSlot(int index, UsageType usageType, IWorldView view, IEntity user, UsageModifiers modifiers);
}
