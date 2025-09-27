package game.engine.entities.items;

import game.engine.entities.EntityAction;
import game.engine.entities.EntityGroupID;

public record ItemInfo(
        String name,
        //this probably should something more complex (stats for weapon, description for potion etc.)
        String description,
        EntityGroupID spriteID,
        EntityAction action,
        int timer
) {
}
