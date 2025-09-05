package game.engine.entities.inventory;

import java.util.Collection;

public record InventoryInfo(
        int slotNum,
        Collection<SlotInfo> slots,
        Collection<ResourceInfo> resources
) {
}
