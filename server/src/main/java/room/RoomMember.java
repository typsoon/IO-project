package room;

import user.IRoomUserHandle;
import user.IUserHandle;

public record RoomMember(IUserHandle user, IRoomUserHandle userHandle) {
}
