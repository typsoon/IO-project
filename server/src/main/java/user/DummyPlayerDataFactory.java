package user;

import game.PlayerData;

public class DummyPlayerDataFactory implements PlayerDataFactory {
    @Override
    public PlayerData getPlayerData(UserHandle userHandle) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
