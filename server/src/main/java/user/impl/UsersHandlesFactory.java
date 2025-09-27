package user.impl;

import gameclient.user.IUserView;
import matchmaking.IMatchmakingEngine;
import matchmaking.lobby.LobbyMember;
import room.IRoomManager;
import user.*;
import utils.ObserverWithATwist;

public class UsersHandlesFactory implements IUsersHandlesFactory {
    private final IRoomManager roomManager;
    private final IMatchmakingEngine matchmakingEngine;

    public UsersHandlesFactory(IRoomManager roomManager, IMatchmakingEngine matchmakingEngine) {
        this.roomManager = roomManager;
        this.matchmakingEngine = matchmakingEngine;
    }

    @Override
    public UsersHandles getUsersHandles(IUserView userView, IMatchmakingUserHandle matchmakingUserHandle, ObserverWithATwist.Notifiable subscriber) {
        var userState = new UserState();
        IUsersRoomHandle roomHandle = new RoomHandle(roomManager, userView, matchmakingUserHandle, userState, subscriber);
        IUsersMatchmakingHandle matchmakingHandle = new UsersMatchmakingHandle(matchmakingEngine,
                new LobbyMember(userView, matchmakingUserHandle, userState));
        return new UsersHandles(roomHandle, matchmakingHandle);
    }
}
