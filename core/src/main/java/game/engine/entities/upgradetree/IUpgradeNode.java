package game.engine.entities.upgradetree;

import game.engine.entities.inventory.IInventory;
import game.engine.entities.items.UsageModifiers;

public interface IUpgradeNode {
    boolean unlock(IInventory inventory, UsageModifiers modifiers);
    boolean isUnlocked();
    UpgradeNodeInfo getInfo();
}
