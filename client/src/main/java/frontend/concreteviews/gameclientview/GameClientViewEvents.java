package frontend.concreteviews.gameclientview;

import com.badlogic.gdx.scenes.scene2d.Event;
import matchmaking.MatchmakingParameters;
import network.messages.userstate.GameConfirmation;
import network.messages.userstate.GameConfirmation.Confirmation;

public class GameClientViewEvents {
    public static final class CreateRoomEvent extends Event {
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

    public static final class JoinRoomEvent extends Event {
        private final String roomName;
        private final String password;

        public JoinRoomEvent(final String roomId, final String password) {
            this.roomName = roomId;
            this.password = password;
        }

        public String getRoomName() {
            return roomName;
        }

        public String getPassword() {
            return password;
        }
    }

    public static final class BrowseRoomsEvent extends Event {
    }

    public static final class RequestGameStartEvent extends Event {
    }

    public static final class FindGameEvent extends Event {
        private final MatchmakingParameters matchmakingParameters;

        public FindGameEvent(final MatchmakingParameters matchmakingParameters) {
            this.matchmakingParameters = matchmakingParameters;
        }

        public MatchmakingParameters getMatchmakingParameters() {
            return matchmakingParameters;
        }
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
