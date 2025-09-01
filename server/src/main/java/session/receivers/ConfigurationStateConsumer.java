package session.receivers;

import java.util.logging.Logger;

import game.session.ISendableConsumer;
import game.utility.ISendable;
import gameclient.rooms.RoomConfig;
import network.messages.configurationstate.CreateRoomRequestResponse;
import user.IUsersMatchmakingHandle;
import user.IUsersRoomHandle;
import gameclient.rooms.*;

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

                        sendableDispatcher.processSendable(roomInfoMessage.get().getRoomInfo());
                    }

                    sendableDispatcher.processSendable(createRoomResponsePayload);
                }
            }

            default -> {
            }
        }
    }

}
