package game.engine.entities.inventory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class Inventory {
    Map<Materials, Integer> items = new EnumMap<>(Materials.class);
    ArrayList<Slot> slots;
    Inventory(int slotsNum) {
        slots = new ArrayList<>(slotsNum);
        for (Materials m : Materials.values()) {
            items.put(m, 0);
        }
    }
    public int getMaterialsCount(Materials material) {
        return items.get(material);
    }
    public void addMaterials(Materials material, int count) {
        items.put(material, items.get(material) + count);
    }
    public boolean removeMaterials(Materials material, int count) {
        if (items.get(material) - count >= 0) {
            items.put(material, items.get(material) - count);
            return true;
        }
        return false;
    }

    public void swapSlots(int index1, int index2) {
        Slot temp = slots.get(index1);
        slots.set(index1, slots.get(index2));
        slots.set(index2, temp);
    }

    public ArrayList<Slot> getSlots() {
        return slots;
    }
    public Slot getSlot(int index){
        return slots.get(index);
    }



}
