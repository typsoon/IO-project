package lobby;

import user.UserHandle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameManager implements MatchmakingEngine {
    private final List<Lobby> lobbies = new ArrayList<>();
    private final List<LobbyBuilder> pendingLobbies = new ArrayList<>();

    @Override
    public void findGame(UserHandle user, int lobbySize) {
        LobbyBuilder lobbyBuilder = findOrCreateLobbyBuilder(lobbySize, 1);
        addUserToLobby(user, lobbyBuilder);
    }

    @Override
    public void findGame(Collection<UserHandle> users, int lobbySize) {
        LobbyBuilder lobbyBuilder = findOrCreateLobbyBuilder(lobbySize, users.size());
        users.forEach(user -> addUserToLobby(user, lobbyBuilder));
    }

    private LobbyBuilder findOrCreateLobbyBuilder(int lobbySize, int requiredCapacity) {
        for (LobbyBuilder builder : pendingLobbies) {
            if (builder.lobbySize() == lobbySize && builder.capacityLeft() >= requiredCapacity) {
                return builder;
            }
        }
        LobbyBuilder newBuilder = new LobbyBuilder(lobbySize);
        pendingLobbies.add(newBuilder);
        return newBuilder;
    }

    private void addUserToLobby(UserHandle user, LobbyBuilder lobbyBuilder) {
        lobbyBuilder.addPlayer(user);
        if (lobbyBuilder.full()) {
            Lobby lobby = lobbyBuilder.build();
            lobbies.add(lobby);
            pendingLobbies.remove(lobbyBuilder);
        }
    }
}
