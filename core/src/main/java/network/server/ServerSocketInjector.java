package network.server;

import java.io.IOException;
import java.net.ServerSocket;

import network.socketwrappers.concretesocketwrappers.ConcreteServerSocketWrapper;

public class ServerSocketInjector {
    public ServerSocketWrapper getServerSocketWrapper(int port) throws IOException {
        var serverSocket = new ServerSocket(port);
        return new ConcreteServerSocketWrapper(serverSocket);
    }
}
