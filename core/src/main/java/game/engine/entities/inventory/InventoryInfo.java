package game.engine.entities.inventory;

import utils.FixedSizeArrayWrapper;

import utils.ISendable;

public record InventoryInfo(
        int slotNum,
        FixedSizeArrayWrapper<SlotInfo> slots,
        FixedSizeArrayWrapper<ResourceInfo> resources) implements ISendable {
}
