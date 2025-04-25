package viewmodel.requests;

import network.ClientSocketWrapper;

public class MoveToConfigurationRequest extends AbstractRequest<ClientSocketWrapper> {
  public MoveToConfigurationRequest(final ClientSocketWrapper payload) {
    super(payload);
  }
}
