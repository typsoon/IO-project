package network.utils;

import utils.ISendable;

public record Credentials(String login, String password) implements ISendable {
}
