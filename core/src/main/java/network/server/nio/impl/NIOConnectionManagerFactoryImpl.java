package network.server.nio.impl;

import java.io.IOException;

import database.impl.ConcreteDatabaseManager;
import network.server.ConcreteAuthenticationService;
import network.server.nio.NIOConnectionManager;
import network.server.nio.NIOConnectionManager.SessionConcract;
import network.server.nio.NIOConnectionManager.SessionCreator;
import network.server.nio.NIOConnectionManagerFactory;
import network.server.nio.NIOSSLSocketServer;
import network.messages.defaultmessage.ConcreteObjectDecoder;

public class NIOConnectionManagerFactoryImpl implements NIOConnectionManagerFactory {

    @Override
    public <T extends SessionConcract> NIOConnectionManager<T> getConnectionManager(SessionCreator<T> sessionCreator,
            int sslServerPort, int udpServerPort, int tcpServerPort) throws IOException {

        var udpServer = new UDPSocketServer(udpServerPort);
        var tcpServer = new TCPSocketServer(tcpServerPort);

        NIOSSLSocketServer sslServer = null;
        try {
            // sslServer = new SSLSocketServerImpl(sslServerPort);
            sslServer = new DummySSLSocketServer(sslServerPort);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        ConcreteDatabaseManager concreteDatabaseManager = new ConcreteDatabaseManager();
        return new ConcreteNIOConnectionManager<>(concreteDatabaseManager,
                new ConcreteAuthenticationService(concreteDatabaseManager),
                udpServer, tcpServer, sslServer, sessionCreator,
                new ConcreteObjectDecoder());
    }

}
