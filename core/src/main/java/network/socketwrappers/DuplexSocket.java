package network.socketwrappers;

import network.messages.Message;

public interface DuplexSocket extends SocketSender<Message, Message>, SocketReceiver {
}
