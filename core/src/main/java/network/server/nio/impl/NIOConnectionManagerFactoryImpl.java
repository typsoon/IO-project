package network.server.nio.impl;

import java.io.IOException;

import network.messages.defaultmessage.ConcreteObjectDecoder;
import network.server.ConcreteAuthenticationService;
import network.server.nio.NIOConnectionManager;
import network.server.nio.NIOConnectionManager.SessionContract;
import network.server.nio.NIOConnectionManager.SessionCreator;
import network.server.nio.NIOConnectionManagerFactory;
import network.server.nio.NIOSSLSocketServer;
import database.IDatabaseManager;

public class NIOConnectionManagerFactoryImpl implements NIOConnectionManagerFactory {

    @Override
    public <T extends SessionContract> NIOConnectionManager<T> getConnectionManager(SessionCreator<T> sessionCreator,
            int sslServerPort, int udpServerPort, int tcpServerPort,
            IDatabaseManager databaseManager) throws IOException {

        var udpServer = new UDPSocketServer(udpServerPort);
        var tcpServer = new TCPSocketServer(tcpServerPort);

        NIOSSLSocketServer sslServer = null;
        try {
            // sslServer = new SSLSocketServerImpl(sslServerPort);
            sslServer = new DummySSLSocketServer(sslServerPort);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        return new ConcreteNIOConnectionManager<>(databaseManager,
                new ConcreteAuthenticationService(
                        databaseManager),
                udpServer, tcpServer, sslServer, sessionCreator,
                new ConcreteObjectDecoder());
    }

}
