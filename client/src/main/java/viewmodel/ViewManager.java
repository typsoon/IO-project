package viewmodel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.badlogic.gdx.Gdx;

import network.AbstractSocketWrapperFactory;
import network.ConnectionData;
import network.socketwrappers.sendertypes.LoginStateSender;
import viewmodel.requests.AbstractRequest;
import viewmodel.requests.MoveToConfigurationRequest;

public class ViewManager implements RequestHandler {
    private final AbstractViewProvider userViewProvider;
    private final AbstractSocketWrapperFactory socketWrapperFactory;
    private final AbstractViewProvider adminViewProvider;
    private final AbstractLoginViewFactory loginViewFactory;

    public ViewManager(final AbstractViewProvider userViewProvider,
                       final AbstractSocketWrapperFactory abstractSocketWrapperFactory,
                       final AbstractViewProvider adminViewProvider,
                       final AbstractLoginViewFactory abstractLoginViewFactory) {
        this.userViewProvider = userViewProvider;
        this.socketWrapperFactory = abstractSocketWrapperFactory;
        this.adminViewProvider = adminViewProvider;
        this.loginViewFactory = abstractLoginViewFactory;
    }

    public void start() {
        // TODO: BIG TODO: Import this from config
        var propertiesLoader = new PropertiesLoader();

        ConnectionData connectionData = new ConnectionData(propertiesLoader.hostname, propertiesLoader.port,
                propertiesLoader.udp_port);
        LoginStateSender authenticatingSocket;

        // TODO: this is ugly, fix this
        try {
            authenticatingSocket = socketWrapperFactory.getAuthenticatingSocket(connectionData);

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        final var loginView = loginViewFactory.getLoginView(this, authenticatingSocket);
        loginView.display();
    }

    @Override
    public void handleRequest(final AbstractRequest request) {
        switch (request) {
            case final MoveToConfigurationRequest moveToConfigurationRequest -> {
                final var connectionData = moveToConfigurationRequest.getConnectionData();
                final var authToken = moveToConfigurationRequest.getAuthToken();
                final var configurationSocket = socketWrapperFactory.getConfigurationSocket(connectionData,
                        authToken);
                final var configurationView = userViewProvider.createConfigurationView(this, configurationSocket);
                configurationView.display();
            }
            default -> {
                throw new IllegalArgumentException(request.toString());
            }
        }
    }

    ;
}

// TODO: delete this class later
class PropertiesLoader {
    String hostname;
    int port;
    int udp_port;

    PropertiesLoader() {
        var fileName = "ServerAdress.properties";
        Properties properties = new Properties();
        try (
                InputStream input = Gdx.files.internal(fileName).read()) {
            properties.load(input);

            hostname = properties.getProperty("hostname");
            port = Integer.parseInt(properties.getProperty("port"));
            udp_port = Integer.parseInt(properties.getProperty("udp_port"));
        }
        catch (IOException e) {
            hostname = "localhost";
            port = 4567;
            udp_port = 4568;
            // throw new RuntimeException(e);
        }
    }
}
