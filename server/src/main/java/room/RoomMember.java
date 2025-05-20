package room;

import user.RoomUserHandle;
import user.UserHandle;

public record RoomMember(UserHandle user, RoomUserHandle userHandle) {
}
