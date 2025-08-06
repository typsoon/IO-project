package session;

import java.util.HashMap;

import database.DatabaseManager.UserId;
import network.impl.ConcreteMessageDispatcher;
import network.server.nio.NIOConnectionManager.SessionCreator;
import user.IUserRoomHandle;

import java.util.Map;

public class ClientDataManager implements SessionCreator<ClientData> {
    Map<UserId, ClientData> activeClientsData = new HashMap<>();

    @Override
    public ClientData getSession(UserId address) {
        IUserRoomHandle userRoomHandle = null;

        activeClientsData.computeIfAbsent(address,
                _address -> new ClientData(new ConcreteMessageDispatcher(), userRoomHandle));
        return activeClientsData.get(address);
    }
}
