package user;

import game.engine.PlayerConfig;
import game.session.IPlayerConnector;
import game.session.ISendableConsumer;
import game.session.PlayerData;

import java.time.Duration;

public interface IMatchmakingUserHandle {
    void gameStarted(ISendableConsumer sendableConsumer);

    PlayerConfig getPlayerConfig();

    IPlayerConnector getPlayerConnector();

    default PlayerData getPlayerData() {
        return new PlayerData(getPlayerConnector(), getPlayerConfig());
    }
}
