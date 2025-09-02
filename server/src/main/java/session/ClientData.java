package session;

import java.io.IOException;
import java.util.logging.Logger;

import database.IDatabaseManager;
import database.IDatabaseManager.UserId;
import game.engine.PlayerConfig;
import game.gamestates.IGameState;
import game.session.IPlayerConnector;
import game.session.ISendableConsumer;
import gameclient.user.UserInfo;
import network.MessageDispatcher;
import network.messages.Message;
import network.messages.configurationstate.GameStartMessages.GameStartedNotification;
import network.messages.defaultmessage.ObjectToMessageDecoder;
import network.messages.userstate.GameConfirmationRequestMessage;
import network.server.nio.NIOConnectionManager.SessionContract;
import session.receivers.IConfigurationStateConsumerFactory;
import user.IMatchmakingUserHandle;
import user.IUsersHandlesFactory;

public class ClientData implements SessionContract, IMatchmakingUserHandle {
    private final MessageDispatcher messageDispatcher;
    private ISendableConsumer sendableReceiver;
    private final PlayerConfig playerConfig;
    private final ObjectToMessageDecoder objectToMessageDecoder;
    private final ISendableConsumer defaultSendableReceiver;
    private final IDatabaseManager databaseManager;

    private final UserId userId;

    public ClientData(MessageDispatcher messageDispatcher, IConfigurationStateConsumerFactory sendableReceiverFactory,
            ObjectToMessageDecoder objectToMessageDecoder, IUsersHandlesFactory usersHandlesFactory,
            UserId userId,
            IDatabaseManager databaseManager) {
        this.messageDispatcher = messageDispatcher;
        this.objectToMessageDecoder = objectToMessageDecoder;
        this.databaseManager = databaseManager;

        this.playerConfig = databaseManager.getPlayerConfig(userId);

        var userName = databaseManager.getPlayerUsername(userId);

        var usersHandles = usersHandlesFactory.getUsersHandles(new UserInfo(userId, userName), this);

        this.defaultSendableReceiver = sendableReceiverFactory.getConfigurationStateConsumer(usersHandles.roomHandle(),
                usersHandles.matchmakingHandle());
        this.sendableReceiver = defaultSendableReceiver;
        this.userId = userId;
    }

    @Override
    public MessageDispatcher getMessageDispatcher() {
        return messageDispatcher;
    }

    public void handleMessage(Message message) {
        sendableReceiver.processSendable(message.getSendable());
    }

    @Override
    public void gameStarted(ISendableConsumer lobby) {
        try {
            messageDispatcher
                    .dispatchMessage(
                            objectToMessageDecoder.decodeFromRecord(new GameStartedNotification.Payload()));
        } catch (IOException ioException) {
            Logger.getGlobal().severe("IOException should not have occured there!!!");
            return;
        }

        sendableReceiver = lobby;
    }

    @Override
    public void moveToConfirmationState(ISendableConsumer confirmationReceiver) {
        try {
            messageDispatcher
                    .dispatchMessage(
                            objectToMessageDecoder.decodeFromRecord(new GameConfirmationRequestMessage.Payload()));
        } catch (IOException ioException) {
            Logger.getGlobal().severe("IOException should not have occured there!!!");
            return;
        }

        this.sendableReceiver = confirmationReceiver;
    }

    @Override
    public void moveToDefaultState() {
        sendableReceiver = defaultSendableReceiver;
    }

    @Override
    public PlayerConfig getPlayerConfig() {
        return playerConfig;
    }

    @Override
    public IPlayerConnector getPlayerConnector() {
        return (gameStates) -> {
            try {
                for (IGameState gameState : gameStates) {
                    var msg = objectToMessageDecoder.decodeFromRecord(gameState);
                    messageDispatcher.dispatchMessage(msg);
                }
            } catch (IOException e) {
                Logger.getGlobal().severe("An IOException caught");
            } catch (Exception e) {
                Logger.getGlobal().severe("An unpredictable error occured %s".formatted(e));
                e.printStackTrace();
            }
        };
    }
}
