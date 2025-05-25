package frontend.concreteviews.serverlookupview;

import com.badlogic.gdx.Game;

import viewmodel.AbstractView;
import viewmodel.RequestHandler;

public class ServerLookupViewFactory {
    private final Game game;

    public ServerLookupViewFactory(final Game game) {
        this.game = game;
    }

    public AbstractView getServerLookupView(RequestHandler requestHandler) {
        final var loginViewInputAdapter = new ServerLookupViewEventListener(requestHandler);
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getServerLookupView'");
    }
}
