package session.receivers;

import java.util.logging.Logger;

import game.session.ISendableConsumer;
import game.utility.ISendable;
import gameclient.rooms.RoomConfig;
import gameclient.rooms.RoomRequest;
import network.messages.configurationstate.CreateRoomRequestResponse;
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

    @Override
    public synchronized void processSendable(final ISendable sendable) {
        Logger.getGlobal().info("Received sendable %s".formatted(sendable));

        switch (sendable) {
            case RoomConfig createRoomRequest -> {
                // synchronized (userRoomHandle) {
                Logger.getGlobal().info("IUHJJII");
                RoomRequest result = null;
                try {
                    result = userRoomHandle.createRoomRequest(createRoomRequest);
                    Logger.getGlobal().info("asasadsad %s".formatted(result));
                } catch (Throwable t) {
                    Logger.getGlobal().info("UUU %s".formatted(t));
                }

                var responsePayload = new CreateRoomRequestResponse.Payload(result, createRoomRequest.name());

                Logger.getGlobal().info("%s response payload".formatted(responsePayload.toString()));

                sendableDispatcher.processSendable(responsePayload);

                // }
            }

            default -> {
                Logger.getGlobal().info("HUUHU");
            }
        }
    }

}
