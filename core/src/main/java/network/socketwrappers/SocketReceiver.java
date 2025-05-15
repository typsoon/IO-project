package network.socketwrappers;

import java.io.IOException;

import network.messages.Message;

public interface SocketReceiver {
    Message receiveMessage() throws IOException;
}
