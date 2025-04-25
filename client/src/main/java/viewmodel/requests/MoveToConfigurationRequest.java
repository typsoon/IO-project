package viewmodel.requests;

import network.ClientSocketWrapper;

public class MoveToConfigurationRequest extends AbstractRequest<ClientSocketWrapper> {
  public MoveToConfigurationRequest(ClientSocketWrapper payload) {
    super(payload);
  }
}
