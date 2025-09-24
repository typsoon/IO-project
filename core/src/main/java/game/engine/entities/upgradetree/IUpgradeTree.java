package game.engine.entities.upgradetree;

import game.engine.entities.inventory.IInventory;
import game.gamestates.UpgradeTreeState;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface IUpgradeTree {
    boolean unlock(int nodeId, IInventory inventory);

    boolean isUnlocked(int nodeId);

    UpgradeNodeInfo getNodeInfo(int nodeId);

    UpgradeTreeState getState();

    int getTreeSize();

    default Collection<UpgradeNodeInfo> getUpgrades() {
        return IntStream.range(0, getTreeSize())
                .mapToObj(this::getNodeInfo)
                .collect(Collectors.toList());
    }

    default Collection<Integer> getUnlockedNodes() {
        return IntStream.range(0, getTreeSize())
                .filter(this::isUnlocked)
                .boxed()
                .collect(Collectors.toList());
    }
}
