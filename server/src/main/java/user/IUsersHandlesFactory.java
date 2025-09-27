package user;

import gameclient.user.IUserView;
import utils.ObserverWithATwist;

public interface IUsersHandlesFactory {
    UsersHandles getUsersHandles(IUserView userView, IMatchmakingUserHandle matchmakingUserHandle, ObserverWithATwist.Notifiable subscriber);
}
