package session;

import java.util.HashMap;

import database.DatabaseManager.UserId;
import network.impl.ConcreteMessageDispatcher;
import network.server.nio.NIOConnectionManager.SessionCreator;
import java.util.Map;

public class ClientDataManager implements SessionCreator<ClientData> {
    Map<UserId, ClientData> activeClientsData = new HashMap<>();

    @Override
    public ClientData getSession(UserId address) {
        activeClientsData.computeIfAbsent(address, _address -> new ClientData(new ConcreteMessageDispatcher()));
        return activeClientsData.get(address);
    }
}
