package game.engine.entities.upgradetree;

import game.engine.entities.inventory.IInventory;
import game.engine.entities.items.UsageModifiers;

import java.util.List;

public class UpgradeTree implements IUpgradeTree{

    private final UsageModifiers usageModifiers;
    private final List<IUpgradeNode> nodes;

    UpgradeTree(List<IUpgradeNode> nodes, UsageModifiers modifiers)
    {
        this.usageModifiers = modifiers;
        this.nodes = nodes;
    }

    @Override
    public boolean unlock(int nodeId, IInventory inventory) {
        if(nodeId < 0 || nodeId >= nodes.size() || isUnlocked(nodeId)){
            return false;
        }
        for(int idx : nodes.get(nodeId).getInfo().prerequisiteNodeIDs()){
            if(!isUnlocked(idx)){
                return false;
            }
        }
        return nodes.get(nodeId).unlock(inventory,usageModifiers);
    }

    @Override
    public boolean isUnlocked(int nodeId) {
        if(nodeId >= 0 && nodeId < nodes.size()){
            return nodes.get(nodeId).isUnlocked();
        }
        return false;
    }

    @Override
    public UpgradeNodeInfo getNodeInfo(int nodeId) {
        if(nodeId >= 0 && nodeId < nodes.size()){
            return nodes.get(nodeId).getInfo();
        }
        return null;
    }

    @Override
    public int getTreeSize() {
        return nodes.size();
    }
}
