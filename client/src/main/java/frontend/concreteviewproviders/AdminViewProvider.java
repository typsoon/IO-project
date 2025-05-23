package frontend.concreteviewproviders;

import com.badlogic.gdx.Game;

import network.socketwrappers.SenderTypes.ConfigurationStateSender;
import viewmodel.AbstractView;
import viewmodel.AbstractViewProvider;
import viewmodel.RequestHandler;

public class AdminViewProvider implements AbstractViewProvider {
    private final Game game;

    public AdminViewProvider(final Game game) {
        this.game = game;
    }

    @Override
    public AbstractView createConfigurationView(RequestHandler requestHandler,
            ConfigurationStateSender configurationSocket) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createConfigurationView'");
    }
}
