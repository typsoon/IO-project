package session;

import java.io.IOException;
import java.util.concurrent.Executors;

import network.messages.decoding.ConcreteMessageDecoder;
import network.server.nio.impl.NIOConnectionManagerFactoryImpl;

public class SessionManagerInjector {
    public static interface SessionManager {
        void start();
    }

    public SessionManager getSessionManager(int sslPort, int udpPort, int tcpPort) throws IOException {
        var workersExecutorService = Executors.newFixedThreadPool(20);
        var sessionManager = new ClientDataManager();

        var connectionManager = new NIOConnectionManagerFactoryImpl().getConnectionManager(sessionManager,
                sslPort,
                udpPort,
                tcpPort);

        return new NIOSessionManager(connectionManager, workersExecutorService);
    }

    // public SessionManager getSessionManager(int sslPort, int udpPort, int
    // tcpPort) throws IOException {
    // var sessionfactory = new ConcreteSessionFactory();
    // var messageDecoder = new ConcreteMessageDecoder();
    // var workersExecutorService = Executors.newFixedThreadPool(20);
    // return new SessionManagerImpl(sessionfactory, sslPort, messageDecoder,
    // workersExecutorService);
    // }

}
