package frontend.concreteviews.gameplayview.processors;

import java.util.EnumSet;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

import game.actions.Direction;
import game.actions.PlayerMove;
import utility.IActionSender;
import utils.ObserverWithATwist.Subscribable;

//TODO: consider dispatching these actions after every cycle
public class PlayerMovementProcessor extends InputAdapter {
    private static enum SimpleDirection {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    private final EnumSet<SimpleDirection> pressedKeys = EnumSet.noneOf(SimpleDirection.class);
    private final IActionSender actionSender;

    private final SimpleDirection[] keyToDirections = new SimpleDirection[Keys.MAX_KEYCODE + 1];

    public PlayerMovementProcessor(IActionSender actionSender, Subscribable gameCycles, int[] leftKeycodes,
            int[] rightKeycodes,
            int[] upKeycodes, int[] downKeycodes) {
        this.actionSender = actionSender;
        for (int i : leftKeycodes) {
            keyToDirections[i] = SimpleDirection.LEFT;
        }
        for (int i : rightKeycodes) {
            keyToDirections[i] = SimpleDirection.RIGHT;
        }
        for (int i : upKeycodes) {
            keyToDirections[i] = SimpleDirection.UP;
        }
        for (int i : downKeycodes) {
            keyToDirections[i] = SimpleDirection.DOWN;
        }

        gameCycles.registerSubscriber(
                deltaTime -> {
                    actionSender.sendAction(new PlayerMove(getDirection()));
                });
    }

    public PlayerMovementProcessor(IActionSender actionSender, Subscribable gameCycles) {
        this(actionSender, gameCycles, new int[] { Keys.A }, new int[] { Keys.D }, new int[] { Keys.W },
                new int[] { Keys.S });
    }

    @Override
    public boolean keyDown(int keycode) {
        var dir = keyToDirections[keycode];
        if (dir != null) {
            pressedKeys.add(dir);
            actionSender.sendAction(new PlayerMove(getDirection()));
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        var dir = keyToDirections[keycode];
        if (dir != null) {
            pressedKeys.remove(dir);
            actionSender.sendAction(new PlayerMove(getDirection()));
        }
        return super.keyUp(keycode);
    }

    private final Direction getDirection() {
        boolean up = pressedKeys.contains(SimpleDirection.UP);
        boolean down = pressedKeys.contains(SimpleDirection.DOWN);
        boolean left = pressedKeys.contains(SimpleDirection.LEFT);
        boolean right = pressedKeys.contains(SimpleDirection.RIGHT);

        if (up) {
            if (left)
                return Direction.NW;
            if (right)
                return Direction.NE;
            return Direction.N;
        }
        if (down) {
            if (left)
                return Direction.SW;
            if (right)
                return Direction.SE;
            return Direction.S;
        }
        if (left)
            return Direction.W;
        if (right)
            return Direction.E;

        return Direction.NONE;
    }
}
