package frontend.concreteviews.gameplayview.processors;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import com.badlogic.gdx.InputProcessor;

import frontend.concreteviews.gameplayview.IGameplayInfoProvider;
import frontend.gamestate.DisplayableGameState;
import utility.IActionSender;

public class ProcessorFactoriesCreator {
    public ProcessorFactoriesCreator(final IActionSender actionSender) {
        this.actionSender = actionSender;
    }

    private final IActionSender actionSender;

    public Collection<Function<IGameplayInfoProvider, InputProcessor>> getProcessorsFactories(
            DisplayableGameState displayableGameState) {
        Function<IGameplayInfoProvider, InputProcessor> playerMovementProcessorFactory = infoProvider -> new PlayerMovementProcessor(
                actionSender);

        return List.of(playerMovementProcessorFactory);
    }
}
