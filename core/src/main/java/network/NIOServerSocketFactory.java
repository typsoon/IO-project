package network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

import network.socketwrappers.concretesocketwrappers.ConcreteNIOServerSocketWrapper;

public class NIOServerSocketFactory {
  public NIOServerSocketWrapper getNioServerSocket(int port) throws IOException {
    var socketChannel = ServerSocketChannel.open();
    socketChannel.bind(new InetSocketAddress(port));
    socketChannel.configureBlocking(false);

    return new ConcreteNIOServerSocketWrapper(port, socketChannel);
  }
}
