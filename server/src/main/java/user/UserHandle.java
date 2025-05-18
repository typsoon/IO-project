package user;

import game.PlayerConnector;
import game.PlayerData;
import game.engine.PlayerConfig;
import lobby.Lobby;

public interface UserHandle {
    UserRoomHandler getUserRoomHandler();

    void setLobby(Lobby lobby);

    PlayerConfig getPlayerConfig();

    PlayerConnector getPlayerConnector();

    default PlayerData getPlayerData() {
        return new PlayerData(getPlayerConnector(), getPlayerConfig());
    }
}
