package lobby;

import game.PlayerConnector;
import game.engine.PlayerConfig;
import jdk.jshell.spi.ExecutionControl;
import user.UserHandle;

import java.util.List;

public class Lobby {
    private final List<UserHandle> members;

    public Lobby(List<UserHandle> members) { this.members = members; }

    public List<PlayerConfig> getPlayerConfigs() {
        return members.stream()
                .map(UserHandle::getPlayerConfig)
                .toList();
    }

    public List<PlayerConnector> getPlayerConnectors() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
