package matchmaking;

import database.IDatabaseManager;
import matchmaking.lobby.Lobby;
import matchmaking.lobby.LobbyBuilder;
import matchmaking.lobby.LobbyMember;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MatchmakingEngine implements IMatchmakingEngine {
    private final List<Lobby> lobbies = new ArrayList<>();
    private final List<LobbyBuilder> pendingLobbies = new ArrayList<>();

//    @Override
//    public void findGame(IMatchmakingUserHandle user, int lobbySize) {
//        LobbyBuilder lobbyBuilder = findOrCreateLobbyBuilder(lobbySize, 1);
//        addUserToLobby(user, lobbyBuilder);
//    }
//
//    @Override
//    public void createGame(Collection<IMatchmakingUserHandle> users) {
//        LobbyBuilder lobbyBuilder = findOrCreateLobbyBuilder(users.size(), users.size());
//        users.forEach(user -> addUserToLobby(user, lobbyBuilder));
//    }
//
//    private LobbyBuilder findOrCreateLobbyBuilder(int lobbySize, int requiredCapacity) {
//        for (LobbyBuilder builder : pendingLobbies) {
//            if (builder.lobbySize() == lobbySize && builder.capacityLeft() >= requiredCapacity) {
//                return builder;
//            }
//        }
//        LobbyBuilder newBuilder = new LobbyBuilder(lobbySize);
//        pendingLobbies.add(newBuilder);
//        return newBuilder;
//    }
//
//    private void addUserToLobby(IMatchmakingUserHandle user, LobbyBuilder lobbyBuilder) {
//        lobbyBuilder.addPlayer(user);
//        if (lobbyBuilder.full()) {
//            Lobby lobby = lobbyBuilder.build();
//            lobbies.add(lobby);
//            pendingLobbies.remove(lobbyBuilder);
//        }
//    }

    @Override
    public void findGame(LobbyMember user, MatchmakingParameters matchmakingParameters) {

    }

    @Override
    public void createGame(Collection<LobbyMember> room, MatchmakingParameters matchmakingParameters) {

    }

    @Override
    public boolean interruptSearch(IDatabaseManager.UserId userId) {
        return false;
    }
}
