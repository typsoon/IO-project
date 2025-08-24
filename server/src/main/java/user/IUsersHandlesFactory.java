package user;

public interface IUsersHandlesFactory {
    UsersHandles getUsersHandles(IUserView userView, IMatchmakingUserHandle matchmakingUserHandle);
}
