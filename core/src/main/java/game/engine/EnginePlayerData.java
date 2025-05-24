package game.engine;

import game.session.PlayerGamesStateSender;

public record EnginePlayerData(
        PlayerGamesStateSender playerGamesStateSender,
        PlayerConfig playerConfig
){}
