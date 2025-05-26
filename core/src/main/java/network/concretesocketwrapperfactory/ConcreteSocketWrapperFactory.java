package network.concretesocketwrapperfactory;

import java.io.IOException;
import java.net.Socket;

import network.AbstractSocketWrapperFactory;
import network.ConnectionData;
import network.messages.utils.InputStreamDataProducer;
import network.messages.utils.OutputStreamDataReceiver;
import network.socketwrappers.SenderTypes.LoginStateSender;
import network.socketwrappers.concretesocketwrappers.ConcreteAuthenticatingSocket;

public class ConcreteSocketWrapperFactory implements AbstractSocketWrapperFactory {
    private final SocketManager socketManager;

    private static String illegalStateErrorMessage = """
            User didn't connect to valid host and authenticate themselves before
                  trying to acquire configuration socket - they didn't call getAuthenticatingSocket with valid ConnectionData prior to calling this method
            """;

    @Override
    public LoginStateSender getAuthenticatingSocket(ConnectionData connectionData) throws IOException {
        var socket = socketManager.getSSLConnection(connectionData);
        var producer = new InputStreamDataProducer(socket.getInputStream());
        var receiver = new OutputStreamDataReceiver(socket.getOutputStream());
        return new ConcreteAuthenticatingSocket(producer, receiver);
    }

    public ConcreteSocketWrapperFactory() {
        this.socketManager = new ConcreteSocketManager();
    }

    /**
     * @param socketManager Wrote this constructor for testing purposes
     */
    ConcreteSocketWrapperFactory(SocketManager socketManager) {
        this.socketManager = socketManager;
    }
}
