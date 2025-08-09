package session;

import java.util.HashMap;
import java.util.Map;

import database.IDatabaseManager.UserId;
import game.engine.PlayerConfig;
import game.engine.entities.GeometryConfigID;
import network.impl.ConcreteMessageDispatcher;
import network.messages.defaultmessage.ConcreteObjectDecoder;
import network.server.nio.NIOConnectionManager.SessionCreator;
import session.receivers.ConfigurationStateConsumerFactory;
import user.IMatchmakingHandle;
import user.IUserRoomHandle;

public class ClientDataManager implements SessionCreator<ClientData> {
    Map<UserId, ClientData> activeClientsData = new HashMap<>();

    @Override
    public ClientData getSession(UserId address) {
        var dispatcher = new ConcreteMessageDispatcher();
        // TODO: get this from a server
        var playerConfig = new PlayerConfig(GeometryConfigID.HUMAN);

        // TODO: put factory calls here
        IUserRoomHandle userRoomHandle = null;
        IMatchmakingHandle matchmakingHandle = null;

        var configurationStateConsumer = new ConfigurationStateConsumerFactory()
                .getConfigurationStateConsumer(userRoomHandle, matchmakingHandle);
        var objectToMessageDecoder = new ConcreteObjectDecoder();

        activeClientsData.computeIfAbsent(address,
                _address -> new ClientData(dispatcher, configurationStateConsumer,
                        playerConfig, objectToMessageDecoder));
        return activeClientsData.get(address);
    }
}
