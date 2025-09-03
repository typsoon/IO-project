package utility;

import java.util.Collection;

import game.actions.IAction;

@FunctionalInterface
public interface IActionSender {
    void sendIAction(IAction action);

    default void sendIActions(Collection<IAction> actions) {
        for (IAction action : actions) {
            sendIAction(action);
        }
    };
}
