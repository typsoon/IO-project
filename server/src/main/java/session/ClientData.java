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
import utils.ObserverWithATwist;

public class ClientData implements SessionContract, IMatchmakingUserHandle {
    private final MessageDispatcher messageDispatcher;
    private ISendableConsumer sendableReceiver;
    private final PlayerConfig playerConfig;
    private final ObjectToMessageDecoder objectToMessageDecoder;
    private final ISendableConsumer defaultSendableReceiver;
    private final IDatabaseManager databaseManager;
    private final IPlayerConnector playerConnector;

    private final UserId userId;

    public ClientData(final MessageDispatcher messageDispatcher,
            final IConfigurationStateConsumerFactory sendableReceiverFactory,
            final ObjectToMessageDecoder objectToMessageDecoder, final IUsersHandlesFactory usersHandlesFactory,
            final UserId userId,
            final IDatabaseManager databaseManager) {
        this.messageDispatcher = messageDispatcher;
        this.objectToMessageDecoder = objectToMessageDecoder;
        this.databaseManager = databaseManager;

        this.playerConfig = databaseManager.getPlayerConfig(userId);

        final var userName = databaseManager.getPlayerUsername(userId);

        var observer = new ObserverWithATwist.ObserverImpl();
        final var usersHandles = usersHandlesFactory.getUsersHandles(new UserInfo(userId, userName),
                this, observer); // TODO:
                                 // add
                                 // subscriber

        this.userId = userId;
        this.defaultSendableReceiver = sendableReceiverFactory.getConfigurationStateConsumer(usersHandles.roomHandle(),
                usersHandles.matchmakingHandle(), userId, observer);
        this.sendableReceiver = defaultSendableReceiver;

        this.playerConnector = (gameStates) -> {
            try {
                for (final IGameState gameState : gameStates) {
                    final var msg = objectToMessageDecoder.decodeFromRecord(gameState);
                    messageDispatcher.dispatchMessage(msg);
                }
            } catch (final IOException e) {
                Logger.getGlobal().severe("An IOException caught");
            } catch (final Exception e) {
                Logger.getGlobal().severe("An unpredictable error occured %s".formatted(e));
                e.printStackTrace();
            }
        };
    }

    @Override
    public MessageDispatcher getMessageDispatcher() {
        return messageDispatcher;
    }

    public void handleMessage(final Message message) {
        sendableReceiver.processSendable(message.getSendable());
    }

    @Override
    public void gameStarted(final ISendableConsumer lobby) {
        try {
            messageDispatcher
                    .dispatchMessage(
                            objectToMessageDecoder.decodeFromRecord(new GameStartedNotification.Payload()));
        } catch (final IOException ioException) {
            Logger.getGlobal().severe("IOException should not have occured there!!!");
            return;
        }

        sendableReceiver = lobby;
    }

    @Override
    public void moveToConfirmationState(final ISendableConsumer confirmationReceiver) {
        try {
            messageDispatcher
                    .dispatchMessage(
                            objectToMessageDecoder.decodeFromRecord(new GameConfirmationRequestMessage.Payload()));
        } catch (final IOException ioException) {
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
        return playerConnector;
    }
}
