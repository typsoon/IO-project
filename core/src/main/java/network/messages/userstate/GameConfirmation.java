package network.messages.userstate;

import utils.ISendable;

public record GameConfirmation(Confirmation confirmation) implements ISendable {
    public enum Confirmation {
        CONFIRMED,
        CANCELLED,
    }
}
