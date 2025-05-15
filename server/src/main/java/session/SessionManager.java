package session;

import static session.HandlingResult.*;

import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import network.NIOServerSocketFactory;
import network.messages.decoding.MessageDecoder;

public class SessionManager {
    private final AbstractSessionFactory sessionFactory;
    private final int port;
    private final MessageDecoder messageDecoder;
    private final ExecutorService workersExecutorService;
    private final Map<SocketAddress, Session> clients = new HashMap<>();

    public SessionManager(AbstractSessionFactory sessionFactory, int port, MessageDecoder messageDecoder,
                          ExecutorService workersExecutorService) {
        this.sessionFactory = sessionFactory;
        this.port = port;
        this.messageDecoder = messageDecoder;
        this.workersExecutorService = workersExecutorService;
    }

    private static final Logger logger = Logger.getGlobal();

    // FIXME: We have a resource leak here
    // TODO: remove dependency on logging
    public void start() {
        var factory = new NIOServerSocketFactory();
        try (var nioServerSocket = factory.getNioServerSocket(port)) {
            Selector selector = Selector.open();
            nioServerSocket.register(selector, SelectionKey.OP_ACCEPT);

            Logger.getGlobal()
                    // .info(String.format("Server started at port: %d, InetAdress: {s}", port));
                    .info(String.format("Server started at port: %d", port));

            int emptyReads = 0;
            while (true) {
                selector.select();

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();

                    try {
                        // TODO: think whether dependency on network module is ok here
                        if (!key.channel().isOpen()) {
                            key.cancel();
                        }

                        if (key.isAcceptable()) {

                            var serverSocketChannel = (ServerSocketChannel) key.channel();
                            var clientSocketChannel = serverSocketChannel.accept();
                            clientSocketChannel.configureBlocking(false);
                            clientSocketChannel.register(selector, SelectionKey.OP_READ);

                            var clientSocket = nioServerSocket.getClientSocket(clientSocketChannel);
                            Session clientSession = sessionFactory.getSession(clientSocket);

                            var adress = clientSocketChannel.getRemoteAddress();
                            clients.put(adress, clientSession);

                            logger.info(String.format("Client connected %s", adress));
                        }

                        if (key.isReadable()) {
                            var clientSocketChannel = (SocketChannel) key.channel();

                            var remoteAdress = clientSocketChannel.getRemoteAddress();
                            Session session = clients.get(remoteAdress);
                            if (session == null) {
                                logger.severe("Message from unexpected source, this should not happen!!!");
                            }
                            else {
                                Callable<Void> task = () -> {
                                    var handlingRes = session.handleIncomingBytes(clientSocketChannel);
                                    if (handlingRes == SHOULD_RESPOND) {
                                        // TODO: Think whether I should do it in key.isWritable

                                        session.sendResponses(clientSocketChannel);
                                    }
                                    else if (handlingRes == CONNECTION_ENDED) {

                                        // TODO: this doesn't seem to be working
                                        logger.info("Client %s disconnected".formatted(remoteAdress));
                                        clients.remove(remoteAdress, session);
                                        key.cancel();
                                        clientSocketChannel.close();
                                    }
                                    return null;
                                };

                                Future<?> future = workersExecutorService.submit(task);
                                future.get();
                            }

                        }

                    }
                    catch (Exception e) {
                        logger.severe(String.format("An error occured at server loop %s", e));
                        throw e;
                    }
                    finally {
                        iter.remove();
                    }
                }
            }

        }
        catch (

                Exception e) {
            e.printStackTrace(System.out);
            logger.severe(String.format("An error occured, ending server loop %s", e));
        }
    }

    ;
}
