package game.engine.entities.inventory;

import utils.FixedSizeArrayWrapper;

public record InventoryInfo(
        int slotNum,
        FixedSizeArrayWrapper<SlotInfo> slots,
        FixedSizeArrayWrapper<ResourceInfo> resources
) {
}

