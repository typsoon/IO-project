package frontend.concreteviews.gameclientview;

import com.badlogic.gdx.scenes.scene2d.Event;
import network.messages.userstate.GameConfirmation;
import network.messages.userstate.GameConfirmation.Confirmation;

public class GameClientViewEvents {
    static final class CreateRoomEvent extends Event {
        private final String name;
        private final String password;
        private final int maxPlayers;
        private final boolean isPublic;

        public CreateRoomEvent(final String name, final String password, final int maxPlayers, final boolean isPublic) {
            this.name = name;
            this.password = password;
            this.maxPlayers = maxPlayers;
            this.isPublic = isPublic;
        }

        public String getName() {
            return name;
        }

        public String getPassword() {
            return password;
        }

        public int getMaxPlayers() {
            return maxPlayers;
        }

        public boolean isPublic() {
            return isPublic;
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
