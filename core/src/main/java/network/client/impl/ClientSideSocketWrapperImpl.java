package network.client.impl;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

import network.ConnectionData;
import network.MessageDispatcher;
import network.client.ClientSideSocketWrapper;
import network.impl.SingleWriteSocketContainer;
import network.messages.Message;
import network.messages.Message.EncryptedMessage;
import network.messages.Message.TCPMessage;
import network.messages.Message.UDPMessage;
import network.messages.Sendable;
import network.socketwrappers.SocketTypes.DuplexSocket;

public class ClientSideSocketWrapperImpl implements ClientSideSocketWrapper {
    private final MessageDispatcher messageDispatcher;
    private final SingleWriteSocketContainer<EncryptedMessage, DuplexSocket<EncryptedMessage>> sslSocketContainer = new SingleWriteSocketContainer<>();
    private final SingleWriteSocketContainer<TCPMessage, DuplexSocket<TCPMessage>> tcpSocketContainer = new SingleWriteSocketContainer<>();
    private final SingleWriteSocketContainer<UDPMessage, DuplexSocket<UDPMessage>> udpSocketContainer = new SingleWriteSocketContainer<>();

    public ClientSideSocketWrapperImpl(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public Sendable getSendable() throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSendable'");
    }

    @Override
    public <U extends Message> Optional<U> dispatchMessage(Message message) throws IOException {
        return this.messageDispatcher.dispatchMessage(message);
    }

    @Override
    public EstablishConnectionResult establishConnection(ConnectionData connectionData) {
        Socket sslSocket = null;
        try {
            // TODO: fix this resource leak
            sslSocket = new Socket(connectionData.host(), connectionData.port());
        } catch (IOException e) {
            return EstablishConnectionResult.FAILED;
        }

        return EstablishConnectionResult.ESTABLISHED;
    }
}
