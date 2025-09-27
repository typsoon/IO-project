package game.engine.entities.upgradetree;

import game.engine.entities.IUsageModifiers;
import game.engine.entities.inventory.IInventory;
import game.gamestates.UpgradeNodeState;
import game.gamestates.UpgradeTreeState;
import utils.FixedSizeArrayWrapper;

import java.util.Collection;
import java.util.List;

public class UpgradeTree implements IUpgradeTree {

    private final IUsageModifiers IUsageModifiers;
    private final List<IUpgradeNode> nodes;

    UpgradeTree(List<IUpgradeNode> nodes, IUsageModifiers modifiers) {
        this.IUsageModifiers = modifiers;
        this.nodes = nodes;
    }

    @Override
    public boolean unlock(int nodeId, IInventory inventory) {
        if (nodeId < 0 || nodeId >= nodes.size() || isUnlocked(nodeId)) {
            return false;
        }
        for (int idx : nodes.get(nodeId).getInfo().prerequisiteNodeIDs()) {
            if (!isUnlocked(idx)) {
                return false;
            }
        }
        return nodes.get(nodeId).unlock(inventory, IUsageModifiers);
    }

    @Override
    public boolean isUnlocked(int nodeId) {
        if (nodeId >= 0 && nodeId < nodes.size()) {
            return nodes.get(nodeId).isUnlocked();
        }
        return false;
    }

    @Override
    public UpgradeNodeInfo getNodeInfo(int nodeId) {
        if (nodeId >= 0 && nodeId < nodes.size()) {
            return nodes.get(nodeId).getInfo();
        }
        return null;
    }

    @Override
    public int getTreeSize() {
        return nodes.size();
    }

    @Override
    public UpgradeTreeState getState() {
        Collection<UpgradeNodeState> nodeStates = nodes.stream()
                .map(IUpgradeNode::getState)
                .toList();
        return new UpgradeTreeState(new FixedSizeArrayWrapper<>(nodeStates, UpgradeNodeState.class));
    }
}
