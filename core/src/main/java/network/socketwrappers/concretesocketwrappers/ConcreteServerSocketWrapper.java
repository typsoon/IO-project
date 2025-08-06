package network.socketwrappers.concretesocketwrappers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import network.messages.Message;
import network.messages.utils.InputStreamDataProducer;
import network.messages.utils.OutputStreamDataReceiver;
import network.server.ServerSocketWrapper;
import network.socketwrappers.SocketTypes.DuplexSocket;

public class ConcreteServerSocketWrapper implements ServerSocketWrapper {
    private final ServerSocket serverSocket;

    public ConcreteServerSocketWrapper(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public DuplexSocket<Message> acceptClient() throws IOException {
        Socket clientSocket = serverSocket.accept();
        var producer = new InputStreamDataProducer(clientSocket.getInputStream());
        var receiver = new OutputStreamDataReceiver(clientSocket.getOutputStream());
        return new ServerClientSessionSocket<Message>(producer, receiver);
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
