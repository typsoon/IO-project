package network.socketwrappers;

import java.io.IOException;
import java.util.Collection;

import network.messages.Message;

public final class SocketTypes {

    public static interface DuplexSocket<T extends Message> extends SocketSender<T>, SocketReceiver {
    }

    @FunctionalInterface
    public interface SocketReceiver {
        Message receiveMessage() throws IOException;
    }

    @FunctionalInterface
    public interface SocketSender<SentMessage extends Message> {
        /**
         * @param message
         */
        void sendMessage(final SentMessage message) throws IOException;

        default void sendMessages(final Collection<SentMessage> messages) throws IOException {
            for (SentMessage sentMessage : messages) {
                sendMessage(sentMessage);
            }
        }
    }
}
