package session;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import database.IDatabaseManager;
import database.IDatabaseManager.UserId;
import network.impl.ConcreteMessageDispatcher;
import network.messages.defaultmessage.ConcreteObjectDecoder;
import network.server.nio.NIOConnectionManager.SessionCreator;
import session.receivers.ConfigurationStateConsumerFactory;
import user.impl.UsersHandlesFactory;

public class ClientDataManager implements SessionCreator<ClientData> {
    private final Map<UserId, ClientData> activeClientsData = new HashMap<>();
    private final UsersHandlesFactory usersHandlesFactory;
    private final IDatabaseManager databaseManager;

    public ClientDataManager(final UsersHandlesFactory usersHandlesFactory, IDatabaseManager databaseManager) {
        this.usersHandlesFactory = usersHandlesFactory;
        this.databaseManager = databaseManager;
    }

    @Override
    public ClientData getSession(final UserId address) {
        final var dispatcher = new ConcreteMessageDispatcher();

        final var configurationStateConsumerFactory = new ConfigurationStateConsumerFactory(dispatcher);
        final var objectToMessageDecoder = new ConcreteObjectDecoder();

        Logger.getGlobal().info(address.toString());

        return activeClientsData.computeIfAbsent(address,
                _address -> new ClientData(dispatcher, configurationStateConsumerFactory,
                        objectToMessageDecoder, usersHandlesFactory, address, databaseManager));
    }
}
