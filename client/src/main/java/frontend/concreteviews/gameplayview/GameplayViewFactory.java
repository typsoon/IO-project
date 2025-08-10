package frontend.concreteviews.gameplayview;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import frontend.ViewWithTimedEventLoop;
import frontend.concreteviews.gameplayview.gameplaymanager.GameplayManagerFactory;
import frontend.gamestate.DisplayableGameState;
import frontend.gamestate.processor.GameStateProcessor;
import game.engine.PlayerConfig;
import game.engine.modules.GeometryModule;
import network.client.ClientSideSocketWrapper;
import viewmodel.ITextureManager;
import viewmodel.IView;
import viewmodel.IViewManager;
import viewmodel.game.RenderableObjectFactory;
import viewmodel.game.RenderablePlayer;
import viewmodel.game.SpriteFactory;

public class GameplayViewFactory {
    IView getGameplayView(
            Game game, IViewManager viewManager, ClientSideSocketWrapper clientSideSocketWrapper,
            ITextureManager textureManager, PlayerConfig playerConfig) {

        //rethink this
        GeometryModule geometryModule = new GeometryModule();
        SpriteFactory spriteFactory = new SpriteFactory();
        RenderableObjectFactory objectFactory = new RenderableObjectFactory(geometryModule,spriteFactory);
        RenderablePlayer player = objectFactory.createRenderablePlayer(playerConfig);
        DisplayableGameState gameState = new DisplayableGameState(player);
        GameStateProcessor gameStateProcessor = new GameStateProcessor(geometryModule, objectFactory,gameState, player);

        var gameplayManager = new GameplayManagerFactory().getGameplayManager(viewManager, clientSideSocketWrapper,
                gameStateProcessor);

        // see other screens (Login, GameClient) for clues about how these work
        var listeners = new ArrayList<EventListener>();

        // see libgdx docs
        var processors = new ArrayList<InputProcessor>();

        var view = new GameplayView(game, listeners, processors, textureManager, gameState);

        return new ViewWithTimedEventLoop(gameplayManager, view, game);
    }
}
