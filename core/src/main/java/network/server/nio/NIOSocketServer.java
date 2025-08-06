package network.server.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;

// TODO: remove preprocessData method, it is not needed by most implementing classes
public interface NIOSocketServer {
    AbstractSelectableChannel getServerSocketChannel();

    ByteBuffer preprocessData(SocketChannel in) throws IOException;

    int getPort();
}
