package game.session;

import game.engine.PlayerConfig;

public record PlayerData(
        IPlayerConnector connector,
        PlayerConfig config
) {
}
