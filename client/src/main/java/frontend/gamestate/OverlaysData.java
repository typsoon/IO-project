package frontend.gamestate;

import java.util.Optional;

import frontend.gamestate.overlays.ChestOverlayData;
import frontend.gamestate.overlays.DeathOverlayData;

public class OverlaysData implements IReadOnlyOverlaysData {
    private Optional<ChestOverlayData> chestOverlayData;
    private Optional<DeathOverlayData> deathOverlayData;

    public OverlaysData() {
        this.chestOverlayData = Optional.empty();
        this.deathOverlayData = Optional.empty();
    }

    public OverlaysData(Optional<ChestOverlayData> chestOverlayData,
            Optional<DeathOverlayData> deathOverlayData) {
        this.chestOverlayData = chestOverlayData;
        this.deathOverlayData = deathOverlayData;
    }

    public Optional<ChestOverlayData> getChestOverlayData() {
        return chestOverlayData;
    }

    public void setChestOverlayData(Optional<ChestOverlayData> chestOverlayData) {
        this.chestOverlayData = chestOverlayData;
    }

    public Optional<DeathOverlayData> getDeathOverlayData() {
        return deathOverlayData;
    }

    public void setDeathOverlayData(Optional<DeathOverlayData> deathOverlayData) {
        this.deathOverlayData = deathOverlayData;
    }
}
