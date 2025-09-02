package frontend.concreteviews.gameclientview;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import gameclient.rooms.RoomInfo;
import gameclient.user.UserInfo;

public final class GameClientViewData {
    private final Map<KeyRecord, RoomContentsRecord> rooms = new ConcurrentHashMap<>();
    private final int uId;

    public GameClientViewData(int uId) {
        this.uId = uId;
    }

    public record UserRecord(UserInfo userInfo, boolean isItMe, boolean isAdmin) {
    }

    private record KeyRecord(String roomName, boolean amIInThatRoom) {
        @Override
        public int hashCode() {
            return roomName.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            KeyRecord other = (KeyRecord) obj;

            if (roomName == null) {
                return other.roomName() == null;
            }

            return roomName.equals(other.roomName);
        }

        KeyRecord(String roomName) {
            this(roomName, false);
        }
    }

    private record RoomContentsRecord(RoomInfo info, Map<Integer, UserRecord> users) {
    }

    void addRoom(RoomInfo roomInfo) {
        var key = new KeyRecord(roomInfo.roomName());

        rooms.computeIfAbsent(
                key,
                rn -> new RoomContentsRecord(roomInfo, new ConcurrentHashMap<>()));
    }

    public RoomInfo getRoomInfo(String roomName) {
        var room = rooms.get(new KeyRecord(roomName));
        return Objects.requireNonNull(room, "No such room").info();
    }

    void addAnUserToARoom(String roomName, UserInfo userInfo, boolean isAdmin) {
        var key = new KeyRecord(roomName, userInfo.id().id() == uId);
        var roomData = rooms.get(key);
        Objects.requireNonNull(roomData, "No such room").users().put(userInfo.id().id(),
                new UserRecord(userInfo, uId == userInfo.id().id(), isAdmin));
        rooms.put(key, roomData);
    }

    void clearRoomContents(String roomName) {
        rooms.remove(new KeyRecord(roomName));
    }

    void clearAllRoomInfo() {
        rooms.clear();
    }

    public Collection<UserRecord> getUsersInRoom(String roomName) {
        var room = rooms.get(new KeyRecord(roomName));
        return Objects.requireNonNull(room, "No such room").users().values();
    }
}
