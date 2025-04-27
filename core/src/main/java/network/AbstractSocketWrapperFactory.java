package network;

import network.socketwrappers.sendertypes.LoginStateSender;

import java.io.IOException;

import network.socketwrappers.sendertypes.ConfigurationStateSender;

public interface AbstractSocketWrapperFactory {
  LoginStateSender getAuthenticatingSocket(ConnectionData connectionData) throws IOException;

  ConfigurationStateSender getConfigurationSocket(ConnectionData connectionData, int authToken);

}
