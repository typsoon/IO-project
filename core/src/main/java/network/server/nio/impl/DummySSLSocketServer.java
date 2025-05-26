package network.server.nio.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import javax.net.ssl.SSLException;

import network.server.nio.NIOSSLSocketServer;

public class DummySSLSocketServer implements NIOSSLSocketServer {
    private final ServerSocketChannel serverSocket;

    public DummySSLSocketServer(int port) throws IOException {
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
        var byteBuf = ByteBuffer.allocate(1000);
        in.read(byteBuf);
        byteBuf.flip();
        return byteBuf;
    }

    @Override
    public void acceptClient(ReadableByteChannel in) throws SSLException {
    }
}
