package network.utils;

import game.utility.ISendable;

public record Credentials(String login, String password) implements ISendable {
}
