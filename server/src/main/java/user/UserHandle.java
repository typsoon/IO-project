package user;

import game.ActionReceiver;
import game.PlayerConnector;
import game.PlayerData;
import game.engine.PlayerConfig;

public interface UserHandle {
    UserRoomHandler getUserRoomHandler();

    void gameStarted(ActionReceiver lobby);

    PlayerConfig getPlayerConfig();

    PlayerConnector getPlayerConnector();

    default PlayerData getPlayerData() {
        return new PlayerData(getPlayerConnector(), getPlayerConfig());
    }
}
