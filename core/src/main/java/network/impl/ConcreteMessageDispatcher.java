package network.impl;

import java.io.IOException;

import network.MessageDispatcher;
import network.messages.Message;
import network.messages.Message.EncryptedMessage;
import network.messages.Message.TCPMessage;
import network.messages.Message.UDPMessage;
import network.socketwrappers.SocketTypes.SocketSender;

import java.util.Optional;

public class ConcreteMessageDispatcher implements MessageDispatcher {
    private final SingleWriteSocketContainer<UDPMessage, SocketSender<UDPMessage>> udpMessageSender = new SingleWriteSocketContainer<>();
    private final SingleWriteSocketContainer<TCPMessage, SocketSender<TCPMessage>> tcpMessageSender = new SingleWriteSocketContainer<>();
    private final SingleWriteSocketContainer<EncryptedMessage, SocketSender<EncryptedMessage>> sslMessageSender = new SingleWriteSocketContainer<>();

    @Override
    public <U extends Message> Optional<U> dispatchMessage(Message message) throws IOException {
        return switch (message) {
            case UDPMessage u -> udpMessageSender.getSocketWrapper().sendMessage(u);
            case TCPMessage t -> tcpMessageSender.getSocketWrapper().sendMessage(t);
            case EncryptedMessage e -> sslMessageSender.getSocketWrapper().sendMessage(e);
        };
    }

    @Override
    public void connectUDPSender(SocketSender<UDPMessage> socketSender) {
        udpMessageSender.setSocketWrapper(socketSender);
    }

    @Override
    public void connectTCPSender(SocketSender<TCPMessage> socketSender) {
        tcpMessageSender.setSocketWrapper(socketSender);
    }

    @Override
    public void connectSSLSender(SocketSender<EncryptedMessage> socketSender) {
        sslMessageSender.setSocketWrapper(socketSender);
    }

}
