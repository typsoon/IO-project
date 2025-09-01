package session;

import java.io.IOException;
import java.util.concurrent.Executors;

import database.impl.ConcreteDatabaseManager;
import matchmaking.MatchmakingEngineFactory;
import network.server.nio.impl.NIOConnectionManagerFactoryImpl;
import room.RoomManager;
import user.impl.UsersHandlesFactory;

public class ServerInjector {
    public static interface Server {
        void start();
    }

    public Server getServer(int sslPort, int udpPort, int tcpPort) throws IOException {
        var workersExecutorService = Executors.newFixedThreadPool(20);

        var matchmakingEngine = new MatchmakingEngineFactory().create();
        var roomManager = new RoomManager(matchmakingEngine);
        var usersHandlesFactory = new UsersHandlesFactory(roomManager, matchmakingEngine);

        var databaseManager = new ConcreteDatabaseManager();

        var clientDataManager = new ClientDataManager(usersHandlesFactory, databaseManager);

        var connectionManager = new NIOConnectionManagerFactoryImpl().getConnectionManager(clientDataManager,
                sslPort,
                udpPort,
                tcpPort,
                databaseManager);

        return new NIOServer(connectionManager, workersExecutorService);
    }
}
