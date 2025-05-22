package game;

import game.actions.Action;

public interface ActionReceiver {
    void sendAction(PlayerConnector player, Action action);
}
