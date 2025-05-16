package user;

import game.PlayerData;

public interface PlayerDataFactory {
    PlayerData getPlayerData(UserHandle userHandle);
}
