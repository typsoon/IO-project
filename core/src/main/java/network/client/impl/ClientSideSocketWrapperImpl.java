package network.client.impl;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;

import network.MessageDispatcher;
import network.client.ClientSideSocketWrapper;
import network.messages.Message;
import network.messages.Message.EncryptedMessage;
import network.messages.Message.TCPMessage;
import network.messages.Message.UDPMessage;
import network.messages.defaultmessage.ObjectToMessageDecoder;
import network.messages.loginstate.LogInResponse;
import network.messages.loginstate.PortInfoRequest;
import network.messages.loginstate.PortInfoResponse;
import network.messages.utils.InputStreamDataProducer;
import network.messages.utils.OutputStreamDataReceiver;
import network.socketwrappers.SocketTypes.DuplexSocket;
import network.socketwrappers.concretesocketwrappers.ClientSessionSSLSocket;
import network.socketwrappers.concretesocketwrappers.ClientSessionSocket;
import network.socketwrappers.concretesocketwrappers.ClientSessionUDPSocket;
import network.utils.ConnectionData;
import network.utils.TokenHolder;
import utils.ISendable;
import utils.SingleWriteContainer;

public class ClientSideSocketWrapperImpl implements ClientSideSocketWrapper {
    private final MessageDispatcher messageDispatcher;
    private final SingleWriteContainer<DuplexSocket<EncryptedMessage>> sslSocketContainer = new SingleWriteContainer<>();
    private final SingleWriteContainer<DuplexSocket<TCPMessage>> tcpSocketContainer = new SingleWriteContainer<>();
    private final SingleWriteContainer<DuplexSocket<UDPMessage>> udpSocketContainer = new SingleWriteContainer<>();

    // FIXME: consider new ConcurrentLinkedQueue(new BoundedList(1000));
    // and new ArrayBlockingQueue(1000)
    // this is important from security reasons

    private final Collection<ISendable> pendingSendables = new ArrayList<>();
    private final ExecutorService executorService = new ForkJoinPool();
    private TokenHolder tokenHolder = new TokenHolder();
    private final ObjectToMessageDecoder objectToMessageDecoder;

    private Socket sslSocket;
    private Socket tcpSocket;
    private DatagramSocket udpSocket;
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
            // Logger.getGlobal()
            // .finest("Adding %s to pendingSendables".formatted(received.getSendable()));
        }

        @Override
        public final void run() {
            while (true) {
                try {
                    // Logger.getGlobal().info("%s RECEIVING".formatted(socketWrapper));
                    var received = socketWrapper.receiveMessage();
                    // Logger.getGlobal()
                    // .finest("Received %s".formatted(received.getClass().getSimpleName()));

                    synchronized (pendingSendables) {
                        handleMessage(received);
                    }
                } catch (IOException ioException) {
                    Logger.getGlobal().severe("IOException encountered here!");
                    break;
                } catch (Exception e) {
                    Logger.getGlobal().severe("Exception here!! %s".formatted(e));
                    break;
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
                            throw e;
                        }
                    }

                    super.handleMessage(received);
                }
                case PortInfoResponse.Payload portInfo -> {
                    try {
                        // NOTE: tcp socket setup
                        tcpSocket = new Socket(sslConnectionData.host(), portInfo.tcpPort());

                        var producer = new InputStreamDataProducer(tcpSocket.getInputStream());
                        var consumer = new OutputStreamDataReceiver(tcpSocket.getOutputStream());

                        var tcpSocketWrapper = new ClientSessionSocket<Message.TCPMessage>(producer, consumer,
                                tokenHolder);
                        messageDispatcher.connectTCPSender(tcpSocketWrapper);
                        tcpSocketContainer.setContents(tcpSocketWrapper);

                        executorService.submit(new SendableReceiver(tcpSocketWrapper));

                        // NOTE: udp socket setup
                        udpSocket = new DatagramSocket();
                        udpSocket.connect(new InetSocketAddress(sslConnectionData.host(), portInfo.udpPort()));

                        var udpSocketWrapper = new ClientSessionUDPSocket(udpSocket, tokenHolder);
                        messageDispatcher.connectUDPSender(udpSocketWrapper);
                        udpSocketContainer.setContents(udpSocketWrapper);

                        executorService.submit(new SendableReceiver(udpSocketWrapper));

                        Logger.getGlobal().info("Received port info");
                    } catch (Exception e) {
                        Logger.getGlobal().severe("An error occured after trying to establish tcp and udp connection");
                        throw new IllegalStateException(e);
                    }

                    super.handleMessage(received);
                }

                default -> {
                }
            }
        }
    }

    @Override
    public Collection<ISendable> getSendables() throws IOException {
        var answer = new ArrayList<ISendable>();

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
            sslSocketContainer.setContents(sslSocketWrapper);
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
