package network.messages;

import java.io.IOException;

import javax.swing.Action;

import network.messages.utils.DataReceiver;

public interface Message {
    void encodeAndWrite(DataReceiver out) throws IOException;

    Action getAction();
}
