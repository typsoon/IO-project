package session;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import network.server.nio.NIOConnectionManager;
import network.server.nio.NIOConnectionManager.ClientAndTheirMessage;
import session.SessionManagerInjector.SessionManager;

public class NIOSessionManager implements SessionManager {
    private final NIOConnectionManager<ClientData> connectionManager;
    private final ExecutorService workerThreadPool;
    private final Logger logger = Logger.getGlobal();

    public NIOSessionManager(NIOConnectionManager<ClientData> connectionManager, ExecutorService executorService) {
        this.connectionManager = connectionManager;
        this.workerThreadPool = executorService;
    }

    @Override
    public void start() {
        while (true) {
            try {
                var messagesToHandle = connectionManager.select();

                workerThreadPool.submit(() -> {
                    for (ClientAndTheirMessage<ClientData> clientAndTheirMessage : messagesToHandle) {
                        clientAndTheirMessage.client().handleMessage(clientAndTheirMessage.message());
                    }
                });
            } catch (IOException e) {
                logger.severe("An error occured %s".formatted(e));
            }
        }
    }

}
