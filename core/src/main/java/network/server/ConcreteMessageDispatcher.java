package network.server;

import java.io.IOException;
import java.util.Objects;

import network.messages.Message;
import network.messages.Message.EncryptedMessage;
import network.messages.Message.TCPMessage;
import network.messages.Message.UDPMessage;
import network.socketwrappers.SocketTypes.SocketSender;

public class ConcreteMessageDispatcher implements MessageDispatcher {
    private static class SingleWriteSocketContainer<T extends Message> {
        private SocketSender<T> socketSender = null;

        private void setSocketSender(SocketSender<T> socketSender) {
            if (this.socketSender != null) {
                throw new IllegalStateException("Element already set to %s".formatted(this.socketSender));
            }
            this.socketSender = socketSender;
        }

        private SocketSender<T> getSocketSender() {
            return Objects.requireNonNull(socketSender);
        }
    }

    private final SingleWriteSocketContainer<UDPMessage> udpMessageSender = new SingleWriteSocketContainer<>();
    private final SingleWriteSocketContainer<TCPMessage> tcpMessageSender = new SingleWriteSocketContainer<>();
    private final SingleWriteSocketContainer<EncryptedMessage> sslMessageSender = new SingleWriteSocketContainer<>();

    @Override
    public void dispatchMessage(Message message) throws IOException {
        switch (message) {
            case UDPMessage u -> udpMessageSender.getSocketSender().sendMessage(u);
            case TCPMessage t -> tcpMessageSender.getSocketSender().sendMessage(t);
            case EncryptedMessage e -> sslMessageSender.getSocketSender().sendMessage(e);
        }

    }

    @Override
    public void connectUDPSender(SocketSender<UDPMessage> socketSender) {
        udpMessageSender.setSocketSender(socketSender);
    }

    @Override
    public void connectTCPSender(SocketSender<TCPMessage> socketSender) {
        tcpMessageSender.setSocketSender(socketSender);
    }

    @Override
    public void connectSSLSender(SocketSender<EncryptedMessage> socketSender) {
        sslMessageSender.setSocketSender(socketSender);
    }

}
