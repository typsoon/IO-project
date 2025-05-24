package game.session;

import game.engine.PlayerConfig;

public record PlayerData(
        PlayerConnector connector,
        PlayerConfig config
) {
}
