package game.engine.entities.inventory;

import game.engine.entities.items.ItemInfo;

public record SlotInfo(
        int amount,
        ItemInfo item
) {
}
