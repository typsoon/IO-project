package network.messages.defaultmessage;

import network.messages.Message;
import utils.ISendable;

public interface ObjectToMessageDecoder {
    public Message decodeFromRecord(ISendable record);
}
