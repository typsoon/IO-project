package room;

import user.IRoomsUserHandle;
import user.IMatchmakingUserHandle;
import user.UserInfo;

public record RoomMember(UserInfo userInfo, IMatchmakingUserHandle matchmakingUserHandle, IRoomsUserHandle roomsUserHandle) {
}
