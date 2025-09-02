package game.engine.entities.inventory;

import game.actions.UsageType;
import game.engine.IWorldView;
import game.engine.entities.IEntity;
import game.engine.entities.items.UsageModifiers;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class Inventory implements IInventory {
    Map<Materials, Integer> items = new EnumMap<>(Materials.class);
    ArrayList<ISlot> slots;
    int activeSlotIndex = -1;
    public Inventory(int slotsNum) {
        slots = new ArrayList<>(slotsNum);
        for(int i = 0; i < slotsNum; i++){
            slots.add(new ObservableSlot(item -> checkActiveSlot(),new Slot()));
        }
        for (Materials m : Materials.values()) {
            items.put(m, 0);
        }
    }
    private void checkActiveSlot(){
        if(activeSlotIndex != -1 && slots.get(activeSlotIndex).isEmpty()){
            activeSlotIndex = -1;
        }
    }
    @Override
    public int getMaterialsCount(Materials material) {
        return items.get(material);
    }
    @Override
    public void addMaterials(Materials material, int count) {
        items.put(material, items.get(material) + count);
    }
    @Override
    public boolean removeMaterials(Materials material, int count) {
        if (items.get(material) - count >= 0) {
            items.put(material, items.get(material) - count);
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
    public ISlot getSlot(int index){
        return slots.get(index);
    }

    @Override
    public void UseSlot(int index, UsageType usageType, IWorldView view, IEntity user, UsageModifiers modifiers) {
        if(slots.get(index).isEmpty()){
            System.out.println("xd");
            return;
        }
        if(activeSlotIndex != index){
            if(activeSlotIndex!=-1)
                slots.get(activeSlotIndex).getItem().itemUnequip(view,user,modifiers);
            activeSlotIndex = index;
            slots.get(activeSlotIndex).getItem().itemEquip(view,user,modifiers);
        }


        switch(usageType){
            case PRIMARY -> slots.get(index).getItem().primaryAction(view, user, modifiers);
            case SECONDARY -> slots.get(index).getItem().secondaryAction(view, user, modifiers);
            case SPECIAL -> slots.get(index).getItem().specialAction(view, user, modifiers);
        }
    }

}
