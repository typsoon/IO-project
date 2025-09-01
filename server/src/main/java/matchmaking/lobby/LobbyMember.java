package matchmaking.lobby;

import gameclient.user.IUserView;
import user.IMatchmakingUserHandle;
import user.UserState;

public record LobbyMember(IUserView userView, IMatchmakingUserHandle matchmakingUserHandle, UserState userState) {
}
