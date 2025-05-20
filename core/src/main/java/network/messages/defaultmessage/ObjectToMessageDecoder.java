package network.messages.defaultmessage;

import network.messages.Message;

public interface ObjectToMessageDecoder {
    public Message decodeFromRecord(Object record);
}
