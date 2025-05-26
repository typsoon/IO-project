package network.server.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import javax.net.ssl.SSLException;

public interface NIOSSLSocketServer extends NIOSocketServer {
    // public static enum DidFinishHandshake {
    // DIDNT, DID_FINISH
    // }

    ServerSocketChannel getServerSocketChannel();

    ByteBuffer preprocessData(SocketChannel in) throws IOException;

    // DidFinishHandshake doHandshake(SocketChannel channel) throws IOException;

    void acceptClient(ReadableByteChannel in) throws SSLException;
}
