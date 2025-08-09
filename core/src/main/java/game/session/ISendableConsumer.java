package game.session;

import game.utility.ISendable;

@FunctionalInterface
public interface ISendableConsumer {
    void processSendable(ISendable sendable);
}
