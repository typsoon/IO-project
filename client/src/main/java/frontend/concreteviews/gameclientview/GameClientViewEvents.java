package frontend.concreteviews.gameclientview;

import com.badlogic.gdx.scenes.scene2d.Event;
import network.messages.userstate.GameConfirmation;
import network.messages.userstate.GameConfirmation.Confirmation;

public class GameClientViewEvents {
    static final class CreateRoomEvent extends Event {
        private final String name;

        public CreateRoomEvent(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static final class RequestGameStartEvent extends Event {
    }

    public static final class ConfirmGameEvent extends Event {
        private final GameConfirmation.Confirmation confirmationVal;

        public ConfirmGameEvent(final Confirmation confirmationVal) {
            this.confirmationVal = confirmationVal;
        }

        public GameConfirmation.Confirmation getConfirmationVal() {
            return confirmationVal;
        }
    }
}
