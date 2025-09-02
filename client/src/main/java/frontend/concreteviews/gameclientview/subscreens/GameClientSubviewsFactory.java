package frontend.concreteviews.gameclientview.subscreens;

import java.util.function.Function;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import frontend.ViewWithEventLoop;
import game.session.ISendableConsumer;
import utility.ICyclePerformer;
import viewmodel.IView;

public final class GameClientSubviewsFactory {
    private final Function<ISendableConsumer, ICyclePerformer> messageHandlerGetter;

    public GameClientSubviewsFactory(final Function<ISendableConsumer, ICyclePerformer> messageHandlerGetter) {
        this.messageHandlerGetter = messageHandlerGetter;
    }

    public IView wrapScreen(final ISendableConsumer displayManager, final Screen screen, final Game game) {
        final var msgHandler = messageHandlerGetter.apply(displayManager);

        return new ViewWithEventLoop(msgHandler, screen, game);
    }

}
