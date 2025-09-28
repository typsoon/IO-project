package game.engine.entities.inventory;

import game.engine.entities.items.ItemInfo;
import utils.ISendable;

public record SlotInfo(
        int amount,
        ItemInfo item
) implements ISendable {
}
