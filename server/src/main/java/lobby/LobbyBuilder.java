package lobby;

import user.IMatchmakingUserHandle;

import java.util.ArrayList;
import java.util.Collection;

public class LobbyBuilder {
    private final int lobbySize;
    private final Collection<IMatchmakingUserHandle> members = new ArrayList<>();

    public LobbyBuilder(int lobbySize) {
        this.lobbySize = lobbySize;
    }

    public int lobbySize() { return lobbySize; }

    public int capacityLeft() { return lobbySize - members.size(); }

    public boolean full() { return members.size() == lobbySize; }

    public void addPlayer(IMatchmakingUserHandle member) { members.add(member); }

    public Lobby build() {
        if (members.size() < lobbySize) {
            throw new IllegalStateException("Not enough players to build a lobby");
        }
        return LobbyFactory.createLobby(members);
    }
}
