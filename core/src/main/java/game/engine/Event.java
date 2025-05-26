package game.engine;

import game.session.IPlayerGamesStateSender;
import game.actions.IAction;

public record Event(
        IPlayerGamesStateSender playerGamesStateSender,
        IAction action
) {}
