package network;

import network.messages.Sendable;

public record Credentials(String login, String password) implements Sendable {
}
