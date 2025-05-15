package session;

import java.util.concurrent.Executors;

import network.messages.decoding.ConcreteMessageDecoder;

public class SessionManagerInjector {
    public SessionManager getSessionManager(int port) {
        var sessionfactory = new ConcreteSessionFactory();
        var messageDecoder = new ConcreteMessageDecoder();
        var workersExecutorService = Executors.newFixedThreadPool(20);
        return new SessionManager(sessionfactory, port, messageDecoder, workersExecutorService);
    }
}
