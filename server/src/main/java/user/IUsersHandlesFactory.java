package user;

import gameclient.user.IUserView;

public interface IUsersHandlesFactory {
    UsersHandles getUsersHandles(IUserView userView, IMatchmakingUserHandle matchmakingUserHandle);
}
