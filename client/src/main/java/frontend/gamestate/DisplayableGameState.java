package frontend.gamestate;

import viewmodel.game.IPlayerData;

import java.util.Collection;

public class DisplayableGameState implements IDisplayableGameState {
    Collection<DrawableInfo> drawableInfos;
    Collection<IPlayerData> playerData;
    private final OverlaysData overlaysData = new OverlaysData();

    public DisplayableGameState() {
        drawableInfos = new java.util.ArrayList<>(); // ToDo: use a more specific collection type if needed
        playerData = new java.util.ArrayList<>();
    }

    @Override
    public Collection<DrawableInfo> getSpritesReadonly() {
        return java.util.Collections.unmodifiableCollection(drawableInfos);
    }

    @Override
    public void addDrawable(DrawableInfo drawableInfo) {
        if (drawableInfo != null) {
            drawableInfos.add(drawableInfo);
        }
    }

    @Override
    public void removeDrawable(DrawableInfo drawableInfo) {
        if (drawableInfo != null) {
            drawableInfos.remove(drawableInfo);
        }
    }

    @Override
    public Collection<IPlayerData> getPlayerData() {
        return playerData;
    }

    @Override
    public void addPlayer(IPlayerData player) {
        if (player != null) {
            playerData.add(player);
        }
    }

    @Override
    public void removePlayer(IPlayerData player) {
        if (player != null) {
            playerData.remove(player);
        }
    }

    @Override
    public OverlaysData getOverlaysData() {
        return overlaysData;
    }
}
