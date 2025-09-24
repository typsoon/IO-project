package frontend.concreteviews.gameplayview.processors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import frontend.concreteviews.gameplayview.IGameplayInfoProvider;
import utility.IActionSender;
import utils.ObserverWithATwist;

public class SlotChangeProcessor extends InputAdapter {
    private final IGameplayInfoProvider gameplayInfoProvider;
    private final IActionSender actionSender;
    private final ObserverWithATwist.Subscribable gameCycles;

    public SlotChangeProcessor(final IActionSender actionSender, final IGameplayInfoProvider gameplayInfoProvider,
                               final ObserverWithATwist.Subscribable gameCycles) {
        this.gameplayInfoProvider = gameplayInfoProvider;
        this.actionSender = actionSender;
        this.gameCycles = gameCycles;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {
            int slotIndex = keycode - Input.Keys.NUM_1;
            if (slotIndex < gameplayInfoProvider.getInventoryInfo().slotNum()) {
                gameplayInfoProvider.setActiveSlotIndex(slotIndex);
            }
            return true;
        }
        return false;
    }
}
