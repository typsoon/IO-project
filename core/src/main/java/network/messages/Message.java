package network.messages;

import java.io.IOException;

import messagetraits.MessageTraits;
import network.messages.utils.DataConsumer;
import utils.ISendable;

@MessageTraits
public sealed abstract class Message permits Message.EncryptedMessage, Message.UDPMessage, Message.TCPMessage {
    public abstract void encodeAndWrite(DataConsumer out) throws IOException;

    public abstract ISendable getSendable();

    public static non-sealed abstract class EncryptedMessage extends Message {
    }

    public static non-sealed abstract class UDPMessage extends Message {
    }

    public static non-sealed abstract class TCPMessage extends Message {
    }
}
