package network;

import java.io.IOException;

import network.socketwrappers.SenderTypes.LoginStateSender;
import network.socketwrappers.SenderTypes.ConfigurationStateSender;

public interface AbstractSocketWrapperFactory {
    LoginStateSender getAuthenticatingSocket(ConnectionData connectionData) throws IOException;

    ConfigurationStateSender getConfigurationSocket(ConnectionData connectionData, int authToken);

}
