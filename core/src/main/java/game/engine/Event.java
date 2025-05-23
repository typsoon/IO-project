package game.engine;

import game.PlayerGamesStateSender;
import game.actions.Action;

public record Event(
        PlayerGamesStateSender playerGamesStateSender,
        Action action
) {}
