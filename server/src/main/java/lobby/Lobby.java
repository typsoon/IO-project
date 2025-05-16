package lobby;

import game.PlayerConnector;
import game.PlayerData;
import game.engine.PlayerConfig;
import jdk.jshell.spi.ExecutionControl;
import user.PlayerDataFactory;
import user.UserHandle;

import java.util.List;

public class Lobby {
    private final List<UserHandle> members;
    private final PlayerDataFactory playerDataFactory;

    public Lobby(List<UserHandle> members, PlayerDataFactory playerDataFactory) {
        this.members = members;
        this.playerDataFactory = playerDataFactory;
    }

    public List<PlayerData> getPlayerData() {
        return members.stream()
                .map(playerDataFactory::getPlayerData)
                .toList();
    }
}
