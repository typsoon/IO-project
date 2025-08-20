package utility;

import game.actions.IAction;

@FunctionalInterface
public interface IActionSender {
    void sendIAction(IAction action);
}
