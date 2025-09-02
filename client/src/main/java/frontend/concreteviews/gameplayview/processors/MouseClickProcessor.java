package frontend.concreteviews.gameplayview.processors;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;

import frontend.concreteviews.gameplayview.IGameplayInfoProvider;
import game.actions.PlayerSlotUse;
import game.actions.UsageType;
import game.utility.Point2F;
import game.utility.Vector2F;
import utility.IActionSender;

public class MouseClickProcessor extends InputAdapter {
    private final IGameplayInfoProvider gameplayInfoProvider;
    private final IActionSender actionSender;

    private final Point2F getPlaceOfInterest() {
        return gameplayInfoProvider.getCenterOfInterest();
    }

    private final Vector2F getDirectionVector(Point2F placeOfInterestPosition, Point2F clickedPointInScreenCords) {
        var clickedPointInGameCords = gameplayInfoProvider.castScreenCordinatesToGameCordinates(
                clickedPointInScreenCords.x(),
                clickedPointInScreenCords.y());

        float dx = clickedPointInGameCords.x() - placeOfInterestPosition.x();
        float dy = clickedPointInGameCords.y() - placeOfInterestPosition.y();

        var vector = new Vector2F(dx, dy).normalize();
        return vector;
    }

    public MouseClickProcessor(IActionSender actionSender, IGameplayInfoProvider gameplayInfoProvider) {
        this.gameplayInfoProvider = gameplayInfoProvider;
        this.actionSender = actionSender;
    }

    private static UsageType[] buttonToUsageTypeMapping = new UsageType[10];
    static {
        for (int i = 0; i < buttonToUsageTypeMapping.length; i++) {
            buttonToUsageTypeMapping[i] = UsageType.NONE;
        }

        buttonToUsageTypeMapping[Buttons.LEFT] = UsageType.PRIMARY;
        buttonToUsageTypeMapping[Buttons.RIGHT] = UsageType.SECONDARY;
        buttonToUsageTypeMapping[Buttons.MIDDLE] = UsageType.SPECIAL;
    }

    // @Override
    // public boolean touchCancelled(int screenX, int screenY, int pointer, int
    // button) {
    // return false;
    // }
    //
    // @Override
    // public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    // return false;
    // }
    //
    // @Override
    // public boolean touchDragged(int screenX, int screenY, int pointer) {
    // return false;
    // }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        var usageType = buttonToUsageTypeMapping[button];
        var direction = getDirectionVector(getPlaceOfInterest(), new Point2F(screenX, screenY));

        // Logger.getGlobal().info("%s".formatted(direction));

        var action = new PlayerSlotUse(usageType, direction, 0);
        actionSender.sendIAction(action);

        return false;
    }
}
