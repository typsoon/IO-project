package user.impl;

import lobby.IMatchmakingEngine;
import user.IMatchmakingUserHandle;
import user.IUsersMatchmakingHandle;
import user.UserState;

public class UsersMatchmakingHandle implements IUsersMatchmakingHandle {
    private final IMatchmakingEngine matchmakingEngine;
    private final UserState userState;
    private final IMatchmakingUserHandle matchmakingUserHandle;

    UsersMatchmakingHandle(IMatchmakingEngine matchmakingEngine, UserState userState, IMatchmakingUserHandle matchmakingUserHandle) {
        this.matchmakingEngine = matchmakingEngine;
        this.userState = userState;
        this.matchmakingUserHandle = matchmakingUserHandle;
    }

    @Override
    public JoinGameRequestResult findGame(MatchmakingParameters matchmakingParameters) {
        if (userState.state == UserState.State.IN_ROOM) return JoinGameRequestResult.ALREADY_IN_ROOM;
        matchmakingEngine.findGame(matchmakingUserHandle, 5); // TODO: wiadomo co
        return JoinGameRequestResult.REQUEST_SUCCESSFUL;
    }

    @Override
    public boolean interruptGameLookup() {
        return false;
    }
}
