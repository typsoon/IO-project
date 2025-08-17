package user.impl;

import lobby.IMatchmakingEngine;
import room.IRoomManager;
import user.*;

public class UsersHandlesFactory implements IUsersHandlesFactory {
    private final IRoomManager roomManager;
    private final IMatchmakingEngine matchmakingEngine;

    public UsersHandlesFactory(IRoomManager roomManager, IMatchmakingEngine matchmakingEngine) {
        this.roomManager = roomManager;
        this.matchmakingEngine = matchmakingEngine;
    }

    @Override
    public UsersHandles getUsersHandles(UserInfo userInfo, IMatchmakingUserHandle matchmakingUserHandle) {
        var userState = new UserState();
        IUsersRoomHandle roomHandle = new RoomHandle(roomManager, userInfo, matchmakingUserHandle, userState);
        IUsersMatchmakingHandle matchmakingHandle = new UsersMatchmakingHandle(matchmakingEngine, userState, matchmakingUserHandle);
        return new UsersHandles(roomHandle, matchmakingHandle);
    }
}
