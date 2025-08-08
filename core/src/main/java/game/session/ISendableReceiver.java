package game.session;

import network.messages.Sendable;

@FunctionalInterface
public interface ISendableReceiver {
    void sendSendable(Sendable sendable);
}
