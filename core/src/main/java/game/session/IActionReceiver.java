package game.session;

import game.actions.IAction;

public interface IActionReceiver {
    void sendAction(ISubscribablePlayerConnector player, IAction action);
}
