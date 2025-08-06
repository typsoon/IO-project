package network.server.nio.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import network.server.nio.NIOSSLSocketServer;

public class SSLSocketServerImpl implements NIOSSLSocketServer {
    public static enum DidFinishHandshake {
        DIDNT, DID_FINISH
    }

    private static class Box<T> {
        private T value;

        public Box(T value) {
            this.value = value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }
    }

    private record SocketChannelData(
            ByteBuffer appIn,
            ByteBuffer netIn,
            ByteBuffer netOut,
            SSLEngine sslEngine,
            Box<DidFinishHandshake> didFinishHandshake) {
    }

    private static final SSLContext sslContext;

    static {
        try {
            // InputStream keystoreFile =
            // SSLSocketServer.class.getClassLoader().getResourceAsStream("keystore.jks");
            // if (keystoreFile == null)
            // throw new IllegalStateException("Keystore not found");
            //
            // KeyStore ks = KeyStore.getInstance("PKCS12");
            // ks.load(keystoreFile, "storepass".toCharArray());
            //
            // KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            // kmf.init(ks, "keypass".toCharArray());
            //
            // TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            // tmf.init(ks);
            //
            // sslContext = SSLContext.getInstance("TLS");
            // sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new
            // SecureRandom());
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(
                    new KeyManager[0],
                    new TrustManager[] { new X509TrustManager() {
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[0];
                        }
                    } },
                    new SecureRandom());
        } catch (Exception e) {
            throw new RuntimeException("SSLContext init failed", e);
        }
    }
    private final Map<ReadableByteChannel, SocketChannelData> socketsData = new HashMap<>();
    private final ServerSocketChannel serverSocket;
    private final int port;

    public SSLSocketServerImpl(int port) throws IOException {
        this.port = port;
        this.serverSocket = ServerSocketChannel.open();
        this.serverSocket.bind(new InetSocketAddress(port));
        this.serverSocket.configureBlocking(false);
    }

    @Override
    public AbstractSelectableChannel getServerSocketChannel() {
        return serverSocket;
    }

    @Override
    public ByteBuffer preprocessData(SocketChannel in) throws IOException {
        var data = socketsData.get(in);
        if (data == null)
            throw new IllegalStateException("No socket data for channel");

        if (data.didFinishHandshake.getValue() == DidFinishHandshake.DIDNT) {
            var result = doHandshake(in);
            data.didFinishHandshake.setValue(result);
            return null;
        }

        var sslEngine = data.sslEngine;
        var netIn = data.netIn;
        var appIn = data.appIn;

        netIn.clear();
        int bytesRead = in.read(netIn);
        if (bytesRead <= 0)
            return null;

        netIn.flip();
        appIn.clear();
        SSLEngineResult result = sslEngine.unwrap(netIn, appIn);

        switch (result.getStatus()) {
            case OK -> {
                appIn.flip();
                ByteBuffer copy = ByteBuffer.allocate(appIn.remaining());
                copy.put(appIn);
                copy.flip();
                return copy;
            }
            case BUFFER_UNDERFLOW -> {
                return null;
            }
            case BUFFER_OVERFLOW -> throw new IOException("Buffer overflow during unwrap");
            case CLOSED -> {
                in.close();
                socketsData.remove(in);
                throw new IOException("SSLEngine closed during unwrap");
            }
            default -> throw new IOException("Unexpected SSL status: " + result.getStatus());
        }
    }

    private DidFinishHandshake doHandshake(SocketChannel channel) throws IOException {
        var data = socketsData.get(channel);
        if (data == null)
            throw new IllegalStateException("No socket data for channel");

        var sslEngine = data.sslEngine;
        var netIn = data.netIn;
        var netOut = data.netOut;
        var appIn = data.appIn;

        SSLEngineResult result = null;
        ByteBuffer dummy = ByteBuffer.allocate(0);

        while (true) {
            switch (sslEngine.getHandshakeStatus()) {
                case NEED_UNWRAP -> {
                    if (channel.read(netIn) < 0)
                        throw new IOException("Channel closed during handshake");
                    netIn.flip();
                    result = sslEngine.unwrap(netIn, appIn);
                    netIn.compact();
                }
                case NEED_WRAP -> {
                    netOut.clear();
                    result = sslEngine.wrap(dummy, netOut);
                    netOut.flip();
                    while (netOut.hasRemaining()) {
                        channel.write(netOut);
                    }
                }
                case NEED_TASK -> {
                    Runnable task;
                    while ((task = sslEngine.getDelegatedTask()) != null)
                        task.run();
                }
                case FINISHED, NOT_HANDSHAKING -> {
                    System.out.println("TLS handshake completed!");
                    return DidFinishHandshake.DID_FINISH;
                }
                default -> throw new IllegalStateException("Unexpected HandshakeStatus");
            }

            if (result != null && result.getStatus() == SSLEngineResult.Status.CLOSED)
                throw new IOException("SSLEngine closed during handshake");
        }
    }

    public void acceptClient(ReadableByteChannel in) throws SSLException {
        if (socketsData.containsKey(in)) {
            throw new IllegalStateException("SocketChannel already registered");
        }

        var sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(false);
        sslEngine.beginHandshake();

        sslEngine.setNeedClientAuth(false);
        sslEngine.setWantClientAuth(false);

        SSLSession session = sslEngine.getSession();
        var appIn = ByteBuffer.allocate(session.getApplicationBufferSize());
        var netIn = ByteBuffer.allocate(session.getPacketBufferSize());
        var netOut = ByteBuffer.allocate(session.getPacketBufferSize());

        socketsData.put(in, new SocketChannelData(appIn, netIn, netOut, sslEngine,
                new Box<>(DidFinishHandshake.DIDNT)));
    }

    @Override
    public int getPort() {
        return port;
    }
}
