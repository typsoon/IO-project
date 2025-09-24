package frontend.concreteviews.gameplayview.processors;

import com.badlogic.gdx.InputProcessor;
import frontend.concreteviews.gameplayview.IGameplayInfoProvider;
import frontend.gamestate.DisplayableGameState;
import utility.IActionSender;
import utils.ObserverWithATwist.Subscribable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class ProcessorFactoriesCreator {
    public ProcessorFactoriesCreator(final IActionSender actionSender) {
        this.actionSender = actionSender;
    }

    public record DataNeededForCreation(
            IGameplayInfoProvider infoProvider, Subscribable gameCycles) {
    }

    private final IActionSender actionSender;

    public Collection<Function<DataNeededForCreation, InputProcessor>> getProcessorsFactories(
            DisplayableGameState displayableGameState) {
        Function<DataNeededForCreation, InputProcessor> playerMovementProcessorFactory = data -> new PlayerMovementProcessor(
                actionSender, data.gameCycles);

        Function<DataNeededForCreation, InputProcessor> mouseClickProcessorCreator = data -> new MouseClickProcessor(
                actionSender,
                data.infoProvider, data.gameCycles);

        Function<DataNeededForCreation, InputProcessor> slotChangeProcessorCreator = data -> new SlotChangeProcessor(
                actionSender,
                data.infoProvider, data.gameCycles);

        return List.of(playerMovementProcessorFactory, mouseClickProcessorCreator, slotChangeProcessorCreator);
    }
}
