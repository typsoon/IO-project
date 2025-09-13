package game.engine;

import game.session.IPlayerGamesStateSender;

public record EnginePlayerData(
        IPlayerGamesStateSender playerGamesStateSender,
        PlayerConfig playerConfig
) {
}
