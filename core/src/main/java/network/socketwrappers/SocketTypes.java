package network.socketwrappers;

import java.io.IOException;

import network.messages.Message;

public final class SocketTypes {

    public static interface DuplexSocket<T extends Message> extends SocketSender<T>, SocketReceiver {
    }

    public interface SocketReceiver {
        Message receiveMessage() throws IOException;
    }

    public interface SocketSender<SentMessage extends Message> {
        /**
         * @param message
         */
        void sendMessage(SentMessage message) throws IOException;
    }
}
