package network;

import java.io.IOException;

import network.socketwrappers.SenderTypes.LoginStateSender;

public interface AbstractSocketWrapperFactory {
    LoginStateSender getAuthenticatingSocket(ConnectionData connectionData) throws IOException;
}
