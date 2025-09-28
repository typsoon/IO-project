package game.engine.entities.inventory;

import game.actions.UsageType;
import game.engine.IWorldView;
import game.engine.entities.IEntity;
import game.engine.entities.IUsageModifiers;
import utils.FixedSizeArrayWrapper;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class Inventory implements IInventory {
    Map<Resource, Integer> resources = new EnumMap<>(Resource.class);
    ArrayList<ISlot> slots;
    int activeSlotIndex = -1;

    public Inventory(int slotsNum) {
        slots = new ArrayList<>(slotsNum);
        for (int i = 0; i < slotsNum; i++) {
            slots.add(new ObservableSlot(item -> checkActiveSlot(), new Slot()));
        }
        for (Resource m : Resource.values()) {
            resources.put(m, 0);
        }
    }

    private void checkActiveSlot() {
        if (activeSlotIndex != -1 && slots.get(activeSlotIndex).isEmpty()) {
            activeSlotIndex = -1;
        }
    }

    @Override
    public int getResourceCount(Resource material) {
        return resources.get(material);
    }

    @Override
    public void addResource(Resource material, int count) {
        resources.put(material, resources.get(material) + count);
    }

    @Override
    public boolean removeResource(Resource material, int count) {
        if (resources.get(material) - count >= 0) {
            resources.put(material, resources.get(material) - count);
            return true;
        }
        return false;
    }

    @Override
    public void swapSlots(int index1, int index2) {
        ISlot temp = slots.get(index1);
        slots.set(index1, slots.get(index2));
        slots.set(index2, temp);
    }

    @Override
    public ArrayList<ISlot> getSlots() {
        return slots;
    }

    @Override
    public ISlot getSlot(int index) {
        return slots.get(index);
    }

    @Override
    public void useSlot(int index, UsageType usageType, IWorldView view, IEntity user, IUsageModifiers modifiers) {
        if (slots.get(index).isEmpty()) {
            return;
        }
        if (activeSlotIndex != index) {
            if (activeSlotIndex != -1)
                slots.get(activeSlotIndex).getItem().itemUnequip(view, user, modifiers);
            activeSlotIndex = index;
            slots.get(activeSlotIndex).getItem().itemEquip(view, user, modifiers);
        }


        switch (usageType) {
            case NONE -> slots.get(index).getItem().stopAction(view, user, modifiers);
            case PRIMARY -> slots.get(index).getItem().primaryAction(view, user, modifiers);
            case SECONDARY -> slots.get(index).getItem().secondaryAction(view, user, modifiers);
            case SPECIAL -> slots.get(index).getItem().specialAction(view, user, modifiers);
        }
    }

    @Override
    public InventoryInfo getInventoryInfo() {
        return new InventoryInfo(
                slots.size(),
                new FixedSizeArrayWrapper<>(slots.stream().map(ISlot::getSlotInfo).toList(), SlotInfo.class),
                new FixedSizeArrayWrapper<>(resources.entrySet().stream().map(e -> new ResourceInfo(e.getValue(), e.getKey())).toList(), ResourceInfo.class)
        );
    }

    @Override
    public int getActiveSlotIndex() {
        return activeSlotIndex;
    }
}
