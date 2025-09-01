package frontend.concreteviews.gameclientview;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import gameclient.rooms.RoomInfo;
import gameclient.user.UserInfo;

public final class GameClientViewData {
    private final Map<String, RoomInfo> roomsNameToInfos = new ConcurrentHashMap<>();
    private final Map<String, Collection<UserInfo>> rooms = new ConcurrentHashMap<>();

    synchronized void addRoom(RoomInfo roomInfo) {
        roomsNameToInfos.put(roomInfo.roomName(), roomInfo);
        rooms.computeIfAbsent(roomInfo.roomName(), info -> new ConcurrentLinkedDeque<>());
    }

    public synchronized RoomInfo getRoomInfo(String roomName) {
        return Objects.requireNonNull(roomsNameToInfos.get(roomName));
    }

    void addAnUserToARoom(String roomName, UserInfo userInfo) {
        var userList = rooms.get(roomName);

        assert userList != null;
        userList.add(userInfo);
    }

    synchronized void clearRoomContents(String roomName) {
        rooms.remove(roomName);
        roomsNameToInfos.remove(roomName);
    }

    synchronized void clearAllRoomInfo() {
        roomsNameToInfos.clear();
        rooms.clear();
    }

    public Collection<UserInfo> getUsersInRoom(String roomName) {
        return Objects.requireNonNull(rooms.get(roomName));
    }
}
