package game.engine.entities.upgradetree;

import game.engine.entities.IUsageModifiers;
import game.engine.entities.inventory.IInventory;
import game.engine.entities.inventory.ResourceInfo;
import game.gamestates.UpgradeNodeState;
import utils.FixedSizeArrayWrapper;

public interface IUpgradeNode {
    boolean unlock(IInventory inventory, IUsageModifiers modifiers);

    boolean isUnlocked();

    UpgradeNodeInfo getInfo();

    default UpgradeNodeState getState() {
        UpgradeNodeInfo info = getInfo();
        return new UpgradeNodeState(
                new FixedSizeArrayWrapper<>(info.costs(), ResourceInfo.class),
                info.name(),
                info.description(),
                isUnlocked(),
                new FixedSizeArrayWrapper<>(info.prerequisiteNodeIDs(), Integer.class)
        );
    }
}
