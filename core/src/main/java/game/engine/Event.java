package game.engine;

import game.session.PlayerGamesStateSender;
import game.actions.Action;

public record Event(
        PlayerGamesStateSender playerGamesStateSender,
        Action action
) {}
