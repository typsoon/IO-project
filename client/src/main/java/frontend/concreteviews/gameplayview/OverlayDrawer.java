package frontend.concreteviews.gameplayview;

import frontend.gamestate.IReadOnlyOverlaysData;

public class OverlayDrawer {
    private final IReadOnlyOverlaysData overlaysData;

    public OverlayDrawer(IReadOnlyOverlaysData overlaysData) {
        this.overlaysData = overlaysData;
    }
}
