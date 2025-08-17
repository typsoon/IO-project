package network.server.nio;

import java.io.IOException;

import network.server.nio.NIOConnectionManager.SessionContract;
import network.server.nio.NIOConnectionManager.SessionCreator;

public interface NIOConnectionManagerFactory {
    <T extends SessionContract> NIOConnectionManager<T> getConnectionManager(SessionCreator<T> sessionCreator,
                                                                             int sslServerPort, int udpServerPort, int tcpServerPort)
            throws IOException;
}
