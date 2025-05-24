package frontend.concreteviewproviders;

import com.badlogic.gdx.Game;

import network.socketwrappers.SenderTypes.ConfigurationStateSender;
import viewmodel.AbstractLoginView;
import viewmodel.AbstractViewProvider;
import viewmodel.RequestHandler;

public class UserViewProvider implements AbstractViewProvider {
    private final Game game;

    public UserViewProvider(final Game game) {
        this.game = game;
    }

    @Override
    public AbstractLoginView createConfigurationView(RequestHandler requestHandler,
                                                     ConfigurationStateSender configurationSocket) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createConfigurationView'");
    }

}
