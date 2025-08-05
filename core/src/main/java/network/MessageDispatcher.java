package network;

import java.io.IOException;

import network.messages.Message;
import network.messages.Message.EncryptedMessage;
import network.messages.Message.TCPMessage;
import network.messages.Message.UDPMessage;
import network.socketwrappers.SocketTypes.SocketSender;

public interface MessageDispatcher {
    void dispatchMessage(Message message) throws IOException;

    void connectUDPSender(SocketSender<UDPMessage> socketSender);

    void connectTCPSender(SocketSender<TCPMessage> socketSender);

    void connectSSLSender(SocketSender<EncryptedMessage> socketSender);
}
