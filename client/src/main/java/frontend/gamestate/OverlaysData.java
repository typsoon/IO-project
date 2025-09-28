package frontend.gamestate;

import java.util.Optional;
import frontend.gamestate.overlays.*;

public record OverlaysData(Optional<ChestOverlayData> chestOverlayData,
        Optional<DeathOverlayData> deathOverlayData) {
    public OverlaysData() {
        this(Optional.empty(), Optional.empty());
    }
}
