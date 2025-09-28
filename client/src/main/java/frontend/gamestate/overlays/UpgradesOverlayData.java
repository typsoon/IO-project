package frontend.gamestate.overlays;

import game.engine.entities.EntityGroupID;
import frontend.gamestate.EntityVisibleState;

public record UpgradesOverlayData() {

    public static record ItemData(EntityGroupID entityGroup, EntityVisibleState visibleState, float stateTime) {
    }
}
