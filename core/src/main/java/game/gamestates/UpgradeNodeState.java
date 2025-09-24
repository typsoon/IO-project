package game.gamestates;

import game.engine.entities.inventory.ResourceInfo;
import utils.FixedSizeArrayWrapper;

public record UpgradeNodeState(
        FixedSizeArrayWrapper<ResourceInfo> costs,
        String name,
        String description,
        boolean isUnlocked,
        FixedSizeArrayWrapper<Integer> prerequisiteNodeIDs
) {
}
