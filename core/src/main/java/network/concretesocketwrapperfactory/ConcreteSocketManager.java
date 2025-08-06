package network.concretesocketwrapperfactory;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import network.utils.ConnectionData;

class ConcreteSocketManager implements SocketManager {
    // private final Map<ConnectionData, Socket> activeConnections = new
    // HashMap<>();
    private final Map<ConnectionData, Socket> activeSSLConnections = new HashMap<>();

    //
    // private SSLSocket establishConnection(ConnectionData connectionData) throws
    // IOException {
    // try {
    // TrustManager[] trustAllCerts = new TrustManager[] {
    // new X509TrustManager() {
    // public void checkClientTrusted(java.security.cert.X509Certificate[] certs,
    // String authType) {
    // // Nic nie sprawdzamy — ufamy wszystkim klientom
    // }
    //
    // public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
    // String authType) {
    // // Nic nie sprawdzamy — ufamy wszystkim serwerom
    // }
    //
    // public java.security.cert.X509Certificate[] getAcceptedIssuers() {
    // return new java.security.cert.X509Certificate[0];
    // }
    // }
    // };
    //
    // SSLContext sslContext = SSLContext.getInstance("TLS");
    // sslContext.init(null, trustAllCerts, new SecureRandom());
    //
    // SSLSocketFactory factory = sslContext.getSocketFactory();
    // SSLSocket socket = (SSLSocket) factory.createSocket(connectionData.host(),
    // connectionData.port());
    // socket.startHandshake();
    //
    // return socket;
    //
    // // TrustManagerFactory tmf =
    // //
    // TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    // // tmf.init((KeyStore) null); // null = use system truststore
    // //
    // // TrustManager[] trustManagers = tmf.getTrustManagers();
    // // SSLContext context = SSLContext.getInstance("TLS");
    // //
    // // context.init(null, trustManagers, new SecureRandom());
    // //
    // // SSLSocketFactory factory = context.getSocketFactory();
    // // SSLSocket socket = (SSLSocket) factory.createSocket(connectionData.host(),
    // // connectionData.port());
    // // socket.startHandshake();
    // //
    // // return socket;
    // // return new Socket(connectionData.host(), connectionData.port());
    //
    // } catch (IOException e) {
    // throw e;
    // } catch (Exception e) {
    // throw new IllegalStateException(e);
    // }
    // }

    private Socket establishConnection(ConnectionData connectionData) throws IOException {
        return new Socket(connectionData.host(), connectionData.port());
    }

    @Override
    public Socket getSSLConnection(ConnectionData connectionData) throws IOException {
        if (!activeSSLConnections.containsKey(connectionData)) {
            var val = establishConnection(connectionData);
            activeSSLConnections.put(connectionData, val);
            return val;
        }
        return activeSSLConnections.get(connectionData);
    }
}
