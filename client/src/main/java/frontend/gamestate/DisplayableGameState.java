package frontend.gamestate;

import viewmodel.game.IPlayerData;

import java.util.Collection;

public class DisplayableGameState implements IDisplayableGameState{
    Collection<DrawableInfo> drawableInfos;
    Collection<IPlayerData> playerData;
    public DisplayableGameState() {
        drawableInfos = new java.util.ArrayList<>(); //ToDo: use a more specific collection type if needed
        playerData = new java.util.ArrayList<>();
    }
    @Override
    public Collection<DrawableInfo> getSpritesReadonly() {
        return java.util.Collections.unmodifiableCollection(drawableInfos);
    }

    @Override
    public void AddDrawable(DrawableInfo drawableInfo) {
        if (drawableInfo != null) {
            drawableInfos.add(drawableInfo);
        }
    }
    @Override
    public void RemoveDrawable(DrawableInfo drawableInfo) {
        if (drawableInfo != null) {
            drawableInfos.remove(drawableInfo);
        }
    }
    @Override
    public Collection<IPlayerData> getPlayerData() {
        return playerData;
    }
    @Override
    public void AddPlayer(IPlayerData player) {
        if (player != null) {
            playerData.add(player);
        }
    }
    @Override
    public void RemovePlayer(IPlayerData player) {
        if (player != null) {
            playerData.remove(player);
        }
    }
}
