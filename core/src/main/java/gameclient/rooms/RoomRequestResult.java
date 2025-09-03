package gameclient.rooms;

import utils.ISendable;

public record RoomRequestResult(RequestResult result, RoomRequestType roomRequestType, String roomName)
        implements ISendable {
}
