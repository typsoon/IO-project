package user;

import game.engine.PlayerConfig;
import game.session.IPlayerConnector;
import game.session.ISendableConsumer;
import game.session.PlayerData;

public interface IMatchmakingUserHandle {
    void gameStarted(ISendableConsumer sendableConsumer);

    void moveToConfirmationState(ISendableConsumer confirmationReceiver);

    void moveToDefaultState();

    PlayerConfig getPlayerConfig();

    IPlayerConnector getPlayerConnector();

    default PlayerData getPlayerData() {
        return new PlayerData(getPlayerConnector(), getPlayerConfig());
    }
}
