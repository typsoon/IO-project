package room;

import gameclient.user.IUserView;
import matchmaking.lobby.LobbyMember;
import user.IMatchmakingUserHandle;
import user.IRoomsUserHandle;
import user.UserState;

public record RoomMember(IUserView userView,
        IMatchmakingUserHandle matchmakingUserHandle,
        IRoomsUserHandle roomsUserHandle,
        UserState userState) {

    public LobbyMember getLobbyMember() {
        return new LobbyMember(userView, matchmakingUserHandle, userState);
    }
}
