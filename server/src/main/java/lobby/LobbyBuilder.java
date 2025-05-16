package lobby;

import user.UserHandle;

import java.util.ArrayList;
import java.util.List;

public class LobbyBuilder {
    private final int lobbySize;
    private List<UserHandle> members;

    public LobbyBuilder(int lobbySize) {
        this.lobbySize = lobbySize;
        this.members = new ArrayList<>();
    }

    public int capacityLeft() {
        return lobbySize - members.size();
    }

    public void addPlayer(UserHandle member) {
        if (members.size() < lobbySize) {
            members.add(member);
        }
        else {
            throw new IllegalStateException("Lobby is full");
        }
    }

    public Lobby build() {
        if (members.size() < lobbySize) {
            throw new IllegalStateException("Not enough players to build a lobby");
        }
        return new Lobby(members);
    }
}
