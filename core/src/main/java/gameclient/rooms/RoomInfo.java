package gameclient.rooms;

import game.utility.ISendable;

public record RoomInfo(int roomID, String roomName, boolean isPublic, int numberOfMembers, int maxMembers,
                       boolean hasPassword)
        implements ISendable {
}
