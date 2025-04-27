package network.socketwrappers.sendertypes;

import network.messages.loginstate.LogInQuery;
import network.messages.loginstate.LogInResponse;
import network.socketwrappers.SocketSender;

public interface LoginStateSender extends SocketSender<LogInQuery, LogInResponse> {
}
