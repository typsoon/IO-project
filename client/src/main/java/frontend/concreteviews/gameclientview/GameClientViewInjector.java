
package frontend.concreteviews.gameclientview;

import com.badlogic.gdx.Game;

import frontend.ViewWithEventLoop;
import network.client.ClientSideSocketWrapper;
import network.messages.defaultmessage.ConcreteObjectDecoder;
import utility.CyclePerformer;
import viewmodel.TextureManager;
import viewmodel.View;
import viewmodel.ViewManager;

public class GameClientViewInjector {
    private final Game game;
    private final ViewManager viewManager;
    private final TextureManager textureManager;

    public GameClientViewInjector(Game game, TextureManager textureManager, ViewManager viewManager) {
        this.game = game;
        this.viewManager = viewManager;
        this.textureManager = textureManager;
    }

    public View getGameClientView(ClientSideSocketWrapper clientSideSocketWrapper) {
        // TODO: make this one
        CyclePerformer cyclePerformer = () -> {
        };
        var objectDecoder = new ConcreteObjectDecoder();
        var eventListener = new GameClientViewEventListener(viewManager, clientSideSocketWrapper, objectDecoder);

        var view = new GameClientView(game, textureManager, eventListener);

        return new ViewWithEventLoop(cyclePerformer, view, game);
    }
}
