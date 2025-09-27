package frontend.concreteviews.gameclientview;

import com.badlogic.gdx.Game;

import frontend.concreteviews.gameclientview.subscreens.GameClientSubviewsFactory;
import network.client.ClientSideSocketWrapper;
import network.messages.defaultmessage.ConcreteObjectDecoder;
import frontend.assetsloading.*;
import viewmodel.IView;
import viewmodel.IViewManager;

public class GameClientViewFactory {
    private final Game game;
    private final IViewManager viewManager;
    private final ITextureManager textureManager;

    public GameClientViewFactory(Game game, ITextureManager textureManager, IViewManager viewManager) {
        this.game = game;
        this.viewManager = viewManager;
        this.textureManager = textureManager;
    }

    public IView getGameClientView(ClientSideSocketWrapper clientSideSocketWrapper, int userID) {
        // TODO: make this one
        var objectDecoder = new ConcreteObjectDecoder();
        var eventListener = new GameClientViewEventListener(viewManager, clientSideSocketWrapper, objectDecoder);

        var gameClientViewData = new GameClientViewData(userID);

        var subscreensFactory = new GameClientSubviewsFactory(sendableConsumer -> new GameClientViewMessageHandler(
                gameClientViewData, clientSideSocketWrapper, viewManager, sendableConsumer,
                userID));

        var view = new GameClientView(game, textureManager, eventListener, gameClientViewData, subscreensFactory);

        return subscreensFactory.wrapScreen(view, view, game);
    }
}
