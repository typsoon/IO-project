package user;

import game.session.ActionReceiver;
import game.session.PlayerConnector;
import game.session.PlayerData;
import game.engine.PlayerConfig;

public interface UserHandle {
    void gameStarted(ActionReceiver lobby);

    PlayerConfig getPlayerConfig();

    PlayerConnector getPlayerConnector();

    default PlayerData getPlayerData() {
        return new PlayerData(getPlayerConnector(), getPlayerConfig());
    }
}
