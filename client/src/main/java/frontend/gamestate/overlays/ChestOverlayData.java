package frontend.gamestate.overlays;

import java.util.Collection;

import game.engine.entities.EntityAction;
import game.engine.entities.EntityGroupID;

public record ChestOverlayData(Collection<ItemData> items) {
    public static record ItemData(EntityGroupID entityGroup, EntityAction action) {
    }
}
