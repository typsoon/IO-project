package user.impl;

import matchmaking.IMatchmakingEngine;
import matchmaking.MatchmakingParameters;
import matchmaking.lobby.LobbyMember;
import user.IUsersMatchmakingHandle;
import user.UserState;

public class UsersMatchmakingHandle implements IUsersMatchmakingHandle {
    private final IMatchmakingEngine matchmakingEngine;
    private final LobbyMember member;

    UsersMatchmakingHandle(IMatchmakingEngine matchmakingEngine, LobbyMember member) {
        this.matchmakingEngine = matchmakingEngine;
        this.member = member;
    }

    @Override
    public JoinGameRequestResult findGame(MatchmakingParameters matchmakingParameters) {
        if (member.userState().getState() == UserState.State.IN_ROOM) return JoinGameRequestResult.ALREADY_IN_ROOM;
        matchmakingEngine.findGame(member, matchmakingParameters);
        return JoinGameRequestResult.REQUEST_SUCCESSFUL;
    }

    @Override
    public boolean interruptGameLookup() {
        return matchmakingEngine.interruptSearch(member.userView().id());
    }
}
