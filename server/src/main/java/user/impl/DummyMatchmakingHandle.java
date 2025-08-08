package user.impl;

import user.IMatchmakingHandle;

public class DummyMatchmakingHandle implements IMatchmakingHandle {

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
