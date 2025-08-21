package network.messages.userstate;

import game.utility.ISendable;

public record GameConfirmation(Confirmation confirmation) implements ISendable {
    public enum Confirmation {
        CONFIRMED,
        CANCELLED,
    }
}

