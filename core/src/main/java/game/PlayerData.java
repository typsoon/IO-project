package game;

import game.engine.PlayerConfig;

public record PlayerData(
        PlayerConnector connector,
        PlayerConfig config
) {
}
