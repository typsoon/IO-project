package network.server.nio.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import network.server.nio.NIOSocketServer;

public class UDPSocketServer implements NIOSocketServer {
    private final ServerSocketChannel serverSocket;

    public UDPSocketServer(int port) throws IOException {
        this.serverSocket = ServerSocketChannel.open();
        this.serverSocket.bind(new InetSocketAddress(port));
        this.serverSocket.configureBlocking(false);
    }

    @Override
    public ServerSocketChannel getServerSocketChannel() {
        return serverSocket;
    }

    @Override
    public ByteBuffer preprocessData(SocketChannel in) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'preprocessData'");
    }
}
