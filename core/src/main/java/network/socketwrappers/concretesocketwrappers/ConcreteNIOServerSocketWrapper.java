package network.socketwrappers.concretesocketwrappers;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import network.NIOServerSocketWrapper;
import network.messages.utils.ByteChannelDataReceiver;
import network.socketwrappers.DuplexSocket;

public class ConcreteNIOServerSocketWrapper implements NIOServerSocketWrapper {
  private final int port;
  private final ServerSocketChannel socketChannel;

  public ConcreteNIOServerSocketWrapper(int port, ServerSocketChannel socketChannel) {
    this.port = port;
    this.socketChannel = socketChannel;
  }

  @Override
  public SelectionKey register(Selector sel, int ops) throws ClosedChannelException {
    return socketChannel.register(sel, ops);
  }

  public final void close() throws IOException {
    socketChannel.close();
  }

  @Override
  public DuplexSocket getClientSocket(SocketChannel socketChannel) {
    var receiver = new ByteChannelDataReceiver(socketChannel);

    // FIXME: Get this null out of here! Receive should never be called on a
    // NIOClientSocket
    return new ClientSessionSocket(null, receiver);
  }

  // @Override
  // public DuplexSocket acceptClient(SelectionKey key, Selector selector) throws
  // IOException {
  // // assert key.isAcceptable() : "Selection key should be acceptable when
  // trying
  // // to accept client using it";
  //
  // var serverSocketChannel = (ServerSocketChannel) key.channel();
  // var clientSocketChannel = serverSocketChannel.accept();
  // clientSocketChannel.configureBlocking(false);
  // clientSocketChannel.register(selector, SelectionKey.OP_READ);
  //
  // return getClientSocket(clientSocketChannel);
  // }
}
