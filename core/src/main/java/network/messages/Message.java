package network.messages;

import java.io.IOException;

import game.Action;
import network.messages.utils.DataReceiver;

import messagetraits.MessageTraits;

@MessageTraits
public abstract class Message {
    public abstract void encodeAndWrite(DataReceiver out) throws IOException;

    public abstract Action getAction();
}
