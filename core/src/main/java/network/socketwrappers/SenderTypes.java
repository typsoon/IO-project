package network.socketwrappers;

import network.messages.loginstate.LogInQuery;
import network.socketwrappers.SocketTypes.SocketSender;

public final class SenderTypes {
    public static interface LoginStateSender extends SocketSender<LogInQuery> {
    }
}
