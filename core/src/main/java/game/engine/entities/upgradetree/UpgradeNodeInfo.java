package game.engine.entities.upgradetree;

import game.engine.entities.inventory.ResourceInfo;

import java.util.Collection;

public record UpgradeNodeInfo(
        Collection<ResourceInfo> costs,
        String name,
        String description,
        Collection<Integer> prerequisiteNodeIDs
) {
}
