package game.engine.entities.upgradetree;

import game.engine.entities.IUsageModifiers;
import game.engine.entities.inventory.IInventory;
import game.engine.entities.inventory.ResourceInfo;

import java.util.Collection;

public class NodeWithResourceUsage implements IUpgradeNode {

    private final IUpgradeNode upgradeNode;

    NodeWithResourceUsage(IUpgradeNode upgradeNode) {
        this.upgradeNode = upgradeNode;
    }

    @Override
    public boolean unlock(IInventory inventory, IUsageModifiers modifiers) {
        Collection<ResourceInfo> costs = upgradeNode.getInfo().costs();
        for (ResourceInfo cost : costs) {
            if (inventory.getResourceCount(cost.resource()) < cost.amount()) {
                return false;
            }
        }
        boolean result = upgradeNode.unlock(inventory, modifiers);
        if (result) {
            for (ResourceInfo cost : costs) {
                inventory.removeResource(cost.resource(), cost.amount());
            }
        }
        return result;
    }

    @Override
    public boolean isUnlocked() {
        return upgradeNode.isUnlocked();
    }

    @Override
    public UpgradeNodeInfo getInfo() {
        return upgradeNode.getInfo();
    }
}
