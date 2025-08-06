package network.server.nio.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;

import network.server.nio.NIOSocketServer;

public class TCPSocketServer implements NIOSocketServer {
    private final ServerSocketChannel serverSocket;
    private final int port;

    public TCPSocketServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = ServerSocketChannel.open();
        this.serverSocket.bind(new InetSocketAddress(port));
        this.serverSocket.configureBlocking(false);
    }

    @Override
    public AbstractSelectableChannel getServerSocketChannel() {
        return serverSocket;
    }

    @Override
    public ByteBuffer preprocessData(SocketChannel in) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'preprocessData'");
    }

    @Override
    public int getPort() {
        return port;
    }

}
