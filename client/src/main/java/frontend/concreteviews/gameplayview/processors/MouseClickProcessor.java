package frontend.concreteviews.gameplayview.processors;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import frontend.concreteviews.gameplayview.IGameplayInfoProvider;
import game.actions.PlayerSlotUse;
import game.actions.UsageType;
import game.utility.Point2F;
import game.utility.Vector2F;
import utility.IActionSender;
import utils.ObserverWithATwist.Subscribable;

public class MouseClickProcessor extends InputAdapter {
    private final IGameplayInfoProvider gameplayInfoProvider;
    private final IActionSender actionSender;
    private int observedPointX = 0;
    private int observedPointY = 0;
    private boolean observedPointSet = false;

    private final Subscribable gameCycles;

    private final Point2F getPlaceOfInterest() {
        return gameplayInfoProvider.getCenterOfInterest();
    }

    private final Vector2F getDirectionVector(final Point2F placeOfInterestPosition,
                                              final Point2F clickedPointInScreenCords) {
        final var clickedPointInGameCords = gameplayInfoProvider.castScreenCoordinatesToGameCoordinates(
                clickedPointInScreenCords.x(),
                clickedPointInScreenCords.y());

        final float dx = clickedPointInGameCords.x() - placeOfInterestPosition.x();
        final float dy = clickedPointInGameCords.y() - placeOfInterestPosition.y();

        final var vector = new Vector2F(dx, dy).normalize();
        return vector;
    }

    public MouseClickProcessor(final IActionSender actionSender, final IGameplayInfoProvider gameplayInfoProvider,
                               final Subscribable gameCycles) {
        this.gameplayInfoProvider = gameplayInfoProvider;
        this.actionSender = actionSender;
        this.gameCycles = gameCycles;

        gameCycles.registerSubscriber(deltaTime -> {
            final var direction = getDirectionVector(getPlaceOfInterest(), new Point2F(observedPointX, observedPointY));
            final var action = new PlayerSlotUse(getUsageType(), direction, gameplayInfoProvider.getActiveSlotIndex());

            if (pressedButton == noButton) {
                observedPointSet = false;
            }

            actionSender.sendAction(action);
        });
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

    private static final int noButton = 8;
    private int pressedButton = noButton;

    private UsageType getUsageType() {
        return buttonToUsageTypeMapping[pressedButton];
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        pressedButton = button;
        observedPointX = screenX;
        observedPointY = screenY;
        // observedPointSet = true;
        return false;
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
        pressedButton = noButton;
        return false;
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY) {
        if (!observedPointSet) {
            observedPointX = screenX;
            observedPointY = screenY;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!observedPointSet) {
            observedPointX = screenX;
            observedPointY = screenY;
        }
        return false;
    }
}
