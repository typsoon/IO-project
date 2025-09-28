package frontend.gamestate;

import java.util.Optional;
import frontend.gamestate.overlays.*;

public record OverlaysData(Optional<ChestOverlayData> chestOverlayData) {
    public OverlaysData() {
        this(Optional.empty());
    }
}
