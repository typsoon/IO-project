package session.receivers;

import java.util.logging.Logger;

import game.session.ISendableConsumer;
import game.utility.ISendable;
import gameclient.rooms.RoomConfig;
import gameclient.rooms.RoomRequest;
import gameclient.rooms.UserMembershipInfo;
import network.messages.configurationstate.CreateRoomRequestResponse;
import room.Room;
import user.IUsersMatchmakingHandle;
import user.IUsersRoomHandle;

public class ConfigurationStateConsumer implements ISendableConsumer {
    private final IUsersRoomHandle userRoomHandle;
    private final IUsersMatchmakingHandle matchmakingHandle;
    private final ISendableConsumer sendableDispatcher;

    public ConfigurationStateConsumer(IUsersRoomHandle userRoomHandle, IUsersMatchmakingHandle matchmakingHandle,
            ISendableConsumer sendableDispatcher) {
        this.userRoomHandle = userRoomHandle;
        this.matchmakingHandle = matchmakingHandle;
        this.sendableDispatcher = sendableDispatcher;
    }

    private void sendDataAboutARoom(Room room) {
        sendableDispatcher.processSendable(room.getRoomInfo());
    }

    @Override
    public synchronized void processSendable(final ISendable sendable) {
        Logger.getGlobal().info("Received sendable %s".formatted(sendable));

        switch (sendable) {
            case RoomConfig createRoomRequest -> {
                synchronized (userRoomHandle) {
                    var result = userRoomHandle.createRoomRequest(createRoomRequest);
                    var createRoomResponsePayload = new CreateRoomRequestResponse.Payload(result,
                            createRoomRequest.name());

                    Logger.getGlobal().info("%s response payload".formatted(createRoomResponsePayload.toString()));

                    if (result == RoomRequest.SUCCESSFUL) {
                        var roomInfoMessage = userRoomHandle.getRoom(createRoomRequest.name());

                        if (roomInfoMessage.isEmpty()) {
                            Logger.getGlobal().info("We should not have encountered this state");
                            break;
                        }

                        var room = roomInfoMessage.get();
                        sendDataAboutARoom(room);
                        var user = userRoomHandle.getRoomMembers().iterator().next().userView();
                        var roomMembershipMsgPayload = new UserMembershipInfo(room.getRoomInfo().roomName(),
                                user.id().id(),
                                user.username());

                        sendableDispatcher.processSendable(roomMembershipMsgPayload);
                    }

                    sendableDispatcher.processSendable(createRoomResponsePayload);
                }
            }

            default -> {
            }
        }
    }

}
