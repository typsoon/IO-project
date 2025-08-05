package network.client.impl;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;

import network.ConnectionData;
import network.MessageDispatcher;
import network.client.ClientSideSocketWrapper;
import network.impl.SingleWriteSocketContainer;
import network.messages.Message;
import network.messages.Message.EncryptedMessage;
import network.messages.Message.TCPMessage;
import network.messages.Message.UDPMessage;
import network.messages.Sendable;
import network.messages.utils.InputStreamDataProducer;
import network.messages.utils.OutputStreamDataReceiver;
import network.socketwrappers.SocketTypes.DuplexSocket;
import network.socketwrappers.concretesocketwrappers.ClientSessionSSLSocket;

public class ClientSideSocketWrapperImpl implements ClientSideSocketWrapper {
    private final MessageDispatcher messageDispatcher;
    private final SingleWriteSocketContainer<EncryptedMessage, DuplexSocket<EncryptedMessage>> sslSocketContainer = new SingleWriteSocketContainer<>();
    private final SingleWriteSocketContainer<TCPMessage, DuplexSocket<TCPMessage>> tcpSocketContainer = new SingleWriteSocketContainer<>();
    private final SingleWriteSocketContainer<UDPMessage, DuplexSocket<UDPMessage>> udpSocketContainer = new SingleWriteSocketContainer<>();

    // FIXME: consider new ConcurrentLinkedQueue(new BoundedList(1000));
    // and new ArrayBlockingQueue(1000)
    // this is important from security reasons

    private final Collection<Sendable> pendingSendables = new ArrayList<>();
    private final ExecutorService executorService = new ForkJoinPool();

    private Socket sslSocket;

    // private final Collection<Sendable> pendingSendables = new
    // ConcurrentLinkedQueue<>();
    // private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public ClientSideSocketWrapperImpl(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    private class SendableReceiver implements Runnable {
        private final DuplexSocket<?> socketWrapper;

        public SendableReceiver(DuplexSocket<?> socketWrapper) {
            this.socketWrapper = socketWrapper;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    var received = socketWrapper.receiveMessage();

                    synchronized (pendingSendables) {
                        pendingSendables.add(received.getSendable());
                    }
                } catch (IOException ioException) {
                    Logger.getGlobal().severe("IOException encountered here!");
                }
            }
        }
    }

    @Override
    public Collection<Sendable> getSendables() throws IOException {
        var answer = new ArrayList<Sendable>();

        synchronized (pendingSendables) {
            answer.addAll(pendingSendables);
            pendingSendables.clear();
        }

        return Collections.unmodifiableCollection(answer);
    }

    @Override
    public void dispatchMessage(Message message) throws IOException {
        this.messageDispatcher.dispatchMessage(message);
    }

    @Override
    public EstablishConnectionResult establishConnection(ConnectionData connectionData) {
        try {
            sslSocket = new Socket(connectionData.host(), connectionData.port());

            var producer = new InputStreamDataProducer(sslSocket.getInputStream());
            var consumer = new OutputStreamDataReceiver(sslSocket.getOutputStream());

            var sslSocketWrapper = new ClientSessionSSLSocket(producer, consumer);
            messageDispatcher.connectSSLSender(sslSocketWrapper);
            sslSocketContainer.setSocketWrapper(sslSocketWrapper);
            executorService
                    .submit(new SendableReceiver(sslSocketWrapper));
        } catch (IOException e) {
            return EstablishConnectionResult.FAILED;
        }

        return EstablishConnectionResult.ESTABLISHED;
    }

    @Override
    public void close() throws Exception {
        if (sslSocket != null) {
            sslSocket.close();
        }
    }
}
