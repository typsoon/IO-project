package game.engine;

import game.PlayerGamesStateSender;
import game.Action;

public record Event(PlayerGamesStateSender playerGamesStateSender, Action action) {
}
