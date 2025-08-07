
package frontend.concreteviews.gameclientview;

import com.badlogic.gdx.Game;

import network.client.ClientSideSocketWrapper;
import network.messages.defaultmessage.ConcreteObjectDecoder;
import viewmodel.AbstractTextureManager;
import viewmodel.AbstractView;
import viewmodel.AbstractViewManager;

public class GameClientViewInjector {
    private final Game game;
    private final AbstractViewManager viewManager;
    private final AbstractTextureManager textureManager;

    public GameClientViewInjector(Game game, AbstractTextureManager textureManager, AbstractViewManager viewManager) {
        this.game = game;
        this.viewManager = viewManager;
        this.textureManager = textureManager;
    }

    public AbstractView getGameClientView(ClientSideSocketWrapper clientSideSocketWrapper) {
        return new GameClientView(game, textureManager,
                new GameClientViewEventListener(viewManager, clientSideSocketWrapper,
                        new ConcreteObjectDecoder()));
    }
}
