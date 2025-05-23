package network;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import network.socketwrappers.SocketTypes.DuplexSocket;

public interface NIOServerSocketWrapper extends AutoCloseable {
    SelectionKey register(Selector sel, int ops) throws ClosedChannelException;

    DuplexSocket getClientSocket(SocketChannel socketChannel);

}
