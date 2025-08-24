package matchmaking.lobby;

import user.IMatchmakingUserHandle;
import user.IUserView;
import user.UserState;

public record LobbyMember(IUserView userView, IMatchmakingUserHandle matchmakingUserHandle, UserState userState) {
}