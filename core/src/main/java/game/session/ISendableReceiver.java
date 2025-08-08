package game.session;

import game.utility.ISendable;

@FunctionalInterface
public interface ISendableReceiver {
    void sendSendable(ISendable sendable);
}
