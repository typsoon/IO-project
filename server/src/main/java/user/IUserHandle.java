package user;

import game.session.IActionReceiver;
import game.session.IPlayerConnector;
import game.session.PlayerData;
import game.engine.PlayerConfig;

public interface IUserHandle {
    void gameStarted(IActionReceiver lobby);

    PlayerConfig getPlayerConfig();

    IPlayerConnector getPlayerConnector();

    default PlayerData getPlayerData() {
        return new PlayerData(getPlayerConnector(), getPlayerConfig());
    }
}
