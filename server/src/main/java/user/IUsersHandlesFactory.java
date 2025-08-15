package user;

import lobby.IMatchmakingEngine;
import room.IRoomManager;

public interface IUsersHandlesFactory {
    UsersHandles getUsersHandles(UserInfo userInfo, IMatchmakingUserHandle matchmakingUserHandle);
}
