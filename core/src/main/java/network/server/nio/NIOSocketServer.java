package network.server.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import network.socketwrappers.SocketTypes.SocketReceiver;

// TODO: remove preprocessData method, it is not needed by most implementing classes
public interface NIOSocketServer {
    ServerSocketChannel getServerSocketChannel();

    ByteBuffer preprocessData(SocketChannel in) throws IOException;

}
