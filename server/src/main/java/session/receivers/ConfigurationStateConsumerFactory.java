package session.receivers;

import java.io.IOException;
import java.util.logging.Logger;

import game.session.ISendableConsumer;
import network.MessageDispatcher;
import network.messages.defaultmessage.ConcreteObjectDecoder;
import network.messages.defaultmessage.ObjectToMessageDecoder;
import user.IUsersMatchmakingHandle;
import user.IUsersRoomHandle;

public class ConfigurationStateConsumerFactory {
    private final ObjectToMessageDecoder objectToMessageDecoder;
    private final MessageDispatcher messageDispatcher;

    public ConfigurationStateConsumerFactory(final MessageDispatcher messageDispatcher) {
        this(new ConcreteObjectDecoder(), messageDispatcher);
    }

    public ConfigurationStateConsumerFactory(final ObjectToMessageDecoder objectToMessageDecoder,
            final MessageDispatcher messageDispatcher) {
        this.objectToMessageDecoder = objectToMessageDecoder;
        this.messageDispatcher = messageDispatcher;
    }

    public ConfigurationStateConsumer getConfigurationStateConsumer(final IUsersRoomHandle userRoomHandle,
            final IUsersMatchmakingHandle matchmakingHandle) {
        final ISendableConsumer sendableDispatcher = sendable -> {

            Logger.getGlobal().info("Sendable is going to be sent sent %s".formatted(sendable));
            try {

                final var msg = objectToMessageDecoder.decodeFromRecord(sendable);
                messageDispatcher.dispatchMessage(msg);

                Logger.getGlobal().info("Sendable sent %s".formatted(sendable));
            } catch (IOException e) {
                Logger.getGlobal().severe("IOEXception caught, tihs should not happen!!!");
            }
        };

        return new ConfigurationStateConsumer(userRoomHandle, matchmakingHandle, sendableDispatcher);
    }
}
