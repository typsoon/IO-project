package game.engine;

import java.util.Objects;

import game.actions.IAction;
import game.session.IPlayerGamesStateSender;

public record Event(
        IPlayerGamesStateSender playerGamesStateSender,
        IAction action) {
    // public Event(IPlayerGamesStateSender playerGamesStateSender, IAction action)
    // {
    // this.playerGamesStateSender = Objects.requireNonNull(playerGamesStateSender);
    // this.action = Objects.requireNonNull(action);
    // }
}
