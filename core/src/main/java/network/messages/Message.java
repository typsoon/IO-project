package network.messages;

import java.io.IOException;

import game.Action;
import network.messages.utils.DataConsumer;

import messagetraits.MessageTraits;

@MessageTraits
public abstract class Message {
    public abstract void encodeAndWrite(DataConsumer out) throws IOException;

    public abstract Action getAction();
}
