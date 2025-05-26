package game.session;

import game.actions.IAction;

public interface IActionReceiver {
    void sendAction(IPlayerConnector player, IAction action);
}
