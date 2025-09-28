package frontend.gamestate;

import java.util.Optional;

import frontend.gamestate.overlays.ChestOverlayData;
import frontend.gamestate.overlays.DeathOverlayData;

public interface IReadOnlyOverlaysData {
    public Optional<ChestOverlayData> getChestOverlayData();

    public void setChestOverlayData(Optional<ChestOverlayData> chestOverlayData);

    public Optional<DeathOverlayData> getDeathOverlayData();
}
