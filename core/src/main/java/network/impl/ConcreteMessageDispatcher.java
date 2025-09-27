package network.impl;

import java.io.IOException;

import network.MessageDispatcher;
import network.messages.Message;
import network.messages.Message.EncryptedMessage;
import network.messages.Message.TCPMessage;
import network.messages.Message.UDPMessage;
import network.socketwrappers.SocketTypes.SocketSender;
import utils.SingleWriteContainer;

public class ConcreteMessageDispatcher implements MessageDispatcher {
    private final SingleWriteContainer<SocketSender<UDPMessage>> udpMessageSender = new SingleWriteContainer<>();
    private final SingleWriteContainer<SocketSender<TCPMessage>> tcpMessageSender = new SingleWriteContainer<>();
    private final SingleWriteContainer<SocketSender<EncryptedMessage>> sslMessageSender = new SingleWriteContainer<>();

    @Override
    public void dispatchMessage(Message message) throws IOException {
        switch (message) {
            case UDPMessage u -> udpMessageSender.getContents().sendMessage(u);
            case TCPMessage t -> tcpMessageSender.getContents().sendMessage(t);
            case EncryptedMessage e -> sslMessageSender.getContents().sendMessage(e);
        }

    }

    @Override
    public void connectUDPSender(SocketSender<UDPMessage> socketSender) {
        udpMessageSender.setContents(socketSender);
    }

    @Override
    public void connectTCPSender(SocketSender<TCPMessage> socketSender) {
        tcpMessageSender.setContents(socketSender);
    }

    @Override
    public void connectSSLSender(SocketSender<EncryptedMessage> socketSender) {
        sslMessageSender.setContents(socketSender);
    }

}
