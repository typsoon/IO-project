package network.socketwrappers.concretesocketwrappers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import network.ServerSocketWrapper;
import network.messages.utils.InputStreamDataProducer;
import network.messages.utils.OutputStreamDataReceiver;
import network.socketwrappers.DuplexSocket;

public class ConcreteServerSocketWrapper implements ServerSocketWrapper {
    private final ServerSocket serverSocket;

    public ConcreteServerSocketWrapper(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public DuplexSocket acceptClient() throws IOException {
        Socket clientSocket = serverSocket.accept();
        var producer = new InputStreamDataProducer(clientSocket.getInputStream());
        var receiver = new OutputStreamDataReceiver(clientSocket.getOutputStream());
        return new ClientSessionSocket(producer, receiver);
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
