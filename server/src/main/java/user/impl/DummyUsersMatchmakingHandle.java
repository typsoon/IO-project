package user.impl;

import user.IUsersMatchmakingHandle;

public class DummyUsersMatchmakingHandle implements IUsersMatchmakingHandle {

    @Override
    public JoinGameRequestResult findGame(MatchmakingParameters matchmakingParameters) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findGame'");
    }

    @Override
    public boolean interruptGameLookup() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'interruptGameLookup'");
    }

}
