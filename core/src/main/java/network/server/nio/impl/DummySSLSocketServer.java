package network.server.nio.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.logging.Logger;

import javax.net.ssl.SSLException;

import network.server.nio.NIOSSLSocketServer;

public class DummySSLSocketServer implements NIOSSLSocketServer {
    private final ServerSocketChannel serverSocket;
    private final int port;

    public DummySSLSocketServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = ServerSocketChannel.open();
        this.serverSocket.bind(new InetSocketAddress(port));
        this.serverSocket.configureBlocking(false);

        Logger.getGlobal().info("Server started at port %d".formatted(port));

    }

    @Override
    public AbstractSelectableChannel getServerSocketChannel() {
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

    @Override
    public int getPort() {
        return port;
    }
}
