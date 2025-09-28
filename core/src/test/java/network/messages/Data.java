package network.messages;

import static game.engine.entities.EntityAction.NONE;
import static game.engine.entities.EntityGroupID.HUMAN_BASIC;

import java.util.LinkedList;
import java.util.stream.IntStream;

import game.engine.entities.inventory.InventoryInfo;
import game.engine.entities.inventory.Resource;
import game.engine.entities.inventory.ResourceInfo;
import game.engine.entities.inventory.SlotInfo;
import game.engine.entities.items.ItemInfo;
import game.gamestates.PlayerInventoryState;
import utils.FixedSizeArrayWrapper;

public class Data {
    public final static PlayerInventoryState state;
    static {
        var slotsList = new LinkedList<SlotInfo>();
        var resources = new LinkedList<ResourceInfo>();

        IntStream.range(0, 3).forEach(val -> {
            var valStr = String.valueOf(val);
            slotsList.add(new SlotInfo(val,
                    new ItemInfo(valStr, valStr, HUMAN_BASIC, NONE, val)));
            resources.add(new ResourceInfo(val, Resource.WOOD));
        });

        state = new PlayerInventoryState(new InventoryInfo(1, new FixedSizeArrayWrapper<>(slotsList, SlotInfo.class),
                new FixedSizeArrayWrapper<>(resources, ResourceInfo.class)));
    }
}
