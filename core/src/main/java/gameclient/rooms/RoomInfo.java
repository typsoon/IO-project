package gameclient.rooms;

import game.utility.ISendable;

public record RoomInfo(String roomName, boolean isPublic, int numberOfMembers, int maxMembers, boolean hasPassword)
        implements ISendable {
}
