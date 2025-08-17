package session;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

import game.engine.PlayerConfig;
import game.gamestates.IGameState;
import game.session.IPlayerConnector;
import game.session.ISendableConsumer;
import network.MessageDispatcher;
import network.messages.Message;
import network.messages.defaultmessage.ObjectToMessageDecoder;
import network.server.nio.NIOConnectionManager.SessionContract;
import user.IMatchmakingUserHandle;

public class ClientData implements SessionContract, IMatchmakingUserHandle {
    private final MessageDispatcher messageDispatcher;
    private ISendableConsumer sendableReceiver;
    private final PlayerConfig playerConfig;
    private final ObjectToMessageDecoder objectToMessageDecoder;

    public ClientData(MessageDispatcher messageDispatcher, ISendableConsumer sendableReceiver,
            PlayerConfig playerConfig, ObjectToMessageDecoder objectToMessageDecoder) {
        this.messageDispatcher = messageDispatcher;
        this.sendableReceiver = Objects.requireNonNull(sendableReceiver);
        this.playerConfig = playerConfig;
        this.objectToMessageDecoder = objectToMessageDecoder;
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
        sendableReceiver = lobby;
        // sendableReceiver = new
        // GameplayStateConsumerFactory().getGameplayStateConsumer();
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
                Logger.getGlobal().severe("An unpredictable error occured");
            }
        };
    }

}
