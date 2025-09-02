package network.server.nio;

import java.io.IOException;

import database.IDatabaseManager;
import network.server.nio.NIOConnectionManager.SessionContract;
import network.server.nio.NIOConnectionManager.SessionCreator;

public interface NIOConnectionManagerFactory {
    <T extends SessionContract> NIOConnectionManager<T> getConnectionManager(SessionCreator<T> sessionCreator,
            int sslServerPort, int udpServerPort, int tcpServerPort, IDatabaseManager databaseManager)
            throws IOException;
}
