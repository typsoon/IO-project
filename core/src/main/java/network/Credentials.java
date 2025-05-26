package network;

import game.actions.Action;

public record Credentials(String login, String password) implements Action {
}
