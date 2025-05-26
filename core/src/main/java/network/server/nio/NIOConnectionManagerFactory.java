package network.server.nio;

import java.io.IOException;

import network.server.nio.NIOConnectionManager.SessionConcract;
import network.server.nio.NIOConnectionManager.SessionCreator;

public interface NIOConnectionManagerFactory {
    <T extends SessionConcract> NIOConnectionManager<T> getConnectionManager(SessionCreator<T> sessionCreator,
            int sslServerPort, int udpServerPort, int tcpServerPort)
            throws IOException;
}
