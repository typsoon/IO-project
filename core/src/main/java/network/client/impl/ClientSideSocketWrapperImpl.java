package network.client.impl;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;

import network.MessageDispatcher;
import network.client.ClientSideSocketWrapper;
import network.impl.SingleWriteSocketContainer;
import network.messages.Message;
import network.messages.Message.EncryptedMessage;
import network.messages.Message.TCPMessage;
import network.messages.Message.UDPMessage;
import network.messages.Sendable;
import network.messages.defaultmessage.ObjectToMessageDecoder;
import network.messages.loginstate.LogInResponse;
import network.messages.loginstate.PortInfoRequest;
import network.messages.loginstate.PortInfoResponse;
import network.messages.utils.InputStreamDataProducer;
import network.messages.utils.OutputStreamDataReceiver;
import network.socketwrappers.SocketTypes.DuplexSocket;
import network.socketwrappers.concretesocketwrappers.ClientSessionSSLSocket;
import network.socketwrappers.concretesocketwrappers.ClientSessionSocket;
import network.utils.ConnectionData;
import network.utils.TokenHolder;

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
    private TokenHolder tokenHolder = new TokenHolder();
    private final ObjectToMessageDecoder objectToMessageDecoder;

    private Socket sslSocket;
    private Socket tcpSocket;
    private ConnectionData sslConnectionData;

    // private final Collection<Sendable> pendingSendables = new
    // ConcurrentLinkedQueue<>();
    // private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public ClientSideSocketWrapperImpl(MessageDispatcher messageDispatcher,
            ObjectToMessageDecoder objectToMessageDecoder) {
        this.messageDispatcher = messageDispatcher;
        this.objectToMessageDecoder = objectToMessageDecoder;
    }

    private class SendableReceiver implements Runnable {
        private final DuplexSocket<?> socketWrapper;

        public SendableReceiver(DuplexSocket<?> socketWrapper) {
            this.socketWrapper = socketWrapper;
        }

        protected void handleMessage(Message received) {
            pendingSendables.add(received.getSendable());
        }

        @Override
        public final void run() {
            while (true) {
                try {
                    var received = socketWrapper.receiveMessage();

                    synchronized (pendingSendables) {
                        handleMessage(received);
                        Logger.getGlobal().info(received.getClass().getSimpleName());
                    }
                } catch (IOException ioException) {
                    Logger.getGlobal().severe("IOException encountered here!");
                }
            }
        }
    }

    private class SSLSendableReceiver extends SendableReceiver {
        public SSLSendableReceiver(DuplexSocket<?> socketWrapper) {
            super(socketWrapper);
        }

        public final void handleMessage(Message received) {
            switch (received.getSendable()) {
                case LogInResponse.Payload authTokenHolder -> {
                    var authToken = authTokenHolder.authToken();
                    if (authToken.isPresent()) {
                        tokenHolder.setToken(authToken.get());

                        var portInfoRequest = objectToMessageDecoder.decodeFromRecord(new PortInfoRequest.Payload());

                        try {
                            Logger.getGlobal().info("Requesting port info %s"
                                    .formatted(portInfoRequest.getClass().getSimpleName()));

                            messageDispatcher.dispatchMessage(portInfoRequest);

                        } catch (IOException ioException) {
                            Logger.getGlobal().severe("Error requesting portInfo");
                            throw new IllegalStateException("Error requesting portInfo");
                        } catch (Exception e) {
                            Logger.getGlobal().severe("Illegal state");
                        }
                    }
                }
                case PortInfoResponse.Payload portInfo -> {
                    try {
                        tcpSocket = new Socket(sslConnectionData.host(), portInfo.tcpPort());

                        var producer = new InputStreamDataProducer(tcpSocket.getInputStream());
                        var consumer = new OutputStreamDataReceiver(tcpSocket.getOutputStream());

                        var tcpSocketWrapper = new ClientSessionSocket<Message.TCPMessage>(producer, consumer,
                                tokenHolder);
                        messageDispatcher.connectTCPSender(tcpSocketWrapper);
                        tcpSocketContainer.setSocketWrapper(tcpSocketWrapper);
                        executorService
                                .submit(new SendableReceiver(tcpSocketWrapper));

                        Logger.getGlobal().info("Received port info");
                        return;
                    } catch (Exception e) {
                        Logger.getGlobal().severe("An error occured after trying to establish tcp and udp connection");
                        throw new IllegalStateException(e);
                    }
                }

                default -> {
                }
            }

            super.handleMessage(received);
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
            this.sslConnectionData = connectionData;
            sslSocket = new Socket(connectionData.host(), connectionData.port());

            var producer = new InputStreamDataProducer(sslSocket.getInputStream());
            var consumer = new OutputStreamDataReceiver(sslSocket.getOutputStream());

            var sslSocketWrapper = new ClientSessionSSLSocket(producer, consumer);
            messageDispatcher.connectSSLSender(sslSocketWrapper);
            sslSocketContainer.setSocketWrapper(sslSocketWrapper);
            executorService
                    .submit(new SSLSendableReceiver(sslSocketWrapper));
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
        if (tcpSocket != null) {
            tcpSocket.close();
        }
    }
}
