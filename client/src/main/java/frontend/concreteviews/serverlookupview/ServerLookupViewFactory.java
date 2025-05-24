package frontend.concreteviews.serverlookupview;

import com.badlogic.gdx.Game;

import viewmodel.AbstractServerLookupViewFactory;
import viewmodel.AbstractLoginView;
import viewmodel.RequestHandler;

public class ServerLookupViewFactory implements AbstractServerLookupViewFactory {
    private final Game game;

    public ServerLookupViewFactory(final Game game) {
        this.game = game;
    }

    @Override
    public AbstractLoginView getServerLookupView(RequestHandler requestHandler) {
        final var loginViewInputAdapter = new ServerLookupViewEventListener(requestHandler);
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getServerLookupView'");
    }
}
