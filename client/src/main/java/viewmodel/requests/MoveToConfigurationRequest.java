package viewmodel.requests;

import network.ConnectionData;


            //TODO remove
public class MoveToConfigurationRequest extends AbstractRequest {
    private final int authToken;

    public MoveToConfigurationRequest(final ConnectionData connectionData, final int authToken) {
        super(connectionData);
        this.authToken = authToken;
    }

    public int getAuthToken() {
        return authToken;
    }
}
