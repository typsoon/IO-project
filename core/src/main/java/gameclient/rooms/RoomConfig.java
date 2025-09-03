package gameclient.rooms;

import utils.ISendable;

public record RoomConfig(String name, String password, int maxPlayers, boolean isPublic) implements ISendable {
}
