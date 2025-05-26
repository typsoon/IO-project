package session;

import java.io.IOException;
import java.util.concurrent.Executors;

import network.server.nio.impl.NIOConnectionManagerFactoryImpl;

public class ServerInjector {
    public static interface Server {
        void start();
    }

    public Server getServer(int sslPort, int udpPort, int tcpPort) throws IOException {
        var workersExecutorService = Executors.newFixedThreadPool(20);
        var clientDataManager = new ClientDataManager();

        var connectionManager = new NIOConnectionManagerFactoryImpl().getConnectionManager(clientDataManager,
                sslPort,
                udpPort,
                tcpPort);

        return new NIOServer(connectionManager, workersExecutorService);
    }
}
