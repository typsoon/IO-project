package user;

public interface IUsersHandlesFactory {
    UsersHandles getUsersHandles(UserInfo userInfo, IMatchmakingUserHandle matchmakingUserHandle);
}
