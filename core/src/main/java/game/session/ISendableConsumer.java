package game.session;

import utils.ISendable;

@FunctionalInterface
public interface ISendableConsumer {
    void processSendable(ISendable sendable);
}
