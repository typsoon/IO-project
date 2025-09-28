package session.receivers;

import static gameclient.rooms.RequestResult.FAILED;
import static gameclient.rooms.RequestResult.SUCCESSFUL;
import static gameclient.rooms.RoomRequestType.CREATE;
import static gameclient.rooms.RoomRequestType.JOIN;

import java.util.logging.Logger;

import database.IDatabaseManager.UserId;
import game.session.ISendableConsumer;
import gameclient.rooms.RoomConfig;
import gameclient.rooms.RoomRequestResult;
import matchmaking.MatchmakingParameters;
import network.messages.configurationstate.GameStartMessages.StartGameRequest;
import network.messages.configurationstate.RoomMessages;
import network.messages.configurationstate.RoomMessages.JoinRoomRequest;
import user.IUsersMatchmakingHandle;
import user.IUsersRoomHandle;
import utils.ISendable;
import utils.ObserverWithATwist.Subscribable;

public class ConfigurationStateConsumer implements ISendableConsumer {
    private final IUsersRoomHandle userRoomHandle;
    private final IUsersMatchmakingHandle matchmakingHandle;
    private final ISendableConsumer sendableDispatcher;
    private final UserId id;

    public ConfigurationStateConsumer(IUsersRoomHandle userRoomHandle, IUsersMatchmakingHandle matchmakingHandle,
            ISendableConsumer sendableDispatcher, UserId id, Subscribable subscribable) {
        this.userRoomHandle = userRoomHandle;
        this.matchmakingHandle = matchmakingHandle;
        this.sendableDispatcher = sendableDispatcher;
        this.id = id;

        subscribable.registerSubscriber(_delta -> {
            // sendableDispatcher.processSendable(new PingRoomMember.Payload());
            var myRoom = userRoomHandle.getMyRoom();
            if (myRoom.isEmpty()) {
                return;
            }

            Logger.getGlobal().info("ASDASASD");
            sendOneRoomData(myRoom.get().getRoomInfo().roomName());
        });
    }

    @Override
    public synchronized void processSendable(final ISendable sendable) {
        Logger.getGlobal().info("Received sendable %s".formatted(sendable));

        switch (sendable) {
            case RoomConfig createRoomRequest -> {
                synchronized (userRoomHandle) {
                    var result = userRoomHandle.createRoomRequest(createRoomRequest);
                    var createRoomResponsePayload = new RoomRequestResult(result,
                            CREATE, createRoomRequest.name());

                    Logger.getGlobal().info("%s response payload".formatted(createRoomResponsePayload.toString()));

                    if (result == SUCCESSFUL) {
                        sendOneRoomData(createRoomRequest.name());
                    }

                    sendableDispatcher.processSendable(createRoomResponsePayload);
                }
            }

            case JoinRoomRequest.Payload joinRoomRequest -> {
                var room = userRoomHandle.getRoom(joinRoomRequest.roomName());
                final RoomRequestResult response;
                if (room.isEmpty()) {
                    response = new RoomRequestResult(FAILED, JOIN, joinRoomRequest.roomName());
                    sendableDispatcher.processSendable(response);
                    break;
                }

                var result = userRoomHandle.joinRoomRequest(room.get(), joinRoomRequest.password());

                response = new RoomRequestResult(result, JOIN, joinRoomRequest.roomName());
                if (result == SUCCESSFUL) {
                    sendOneRoomData(joinRoomRequest.roomName());
                }

                sendableDispatcher.processSendable(response);
            }

            case RoomMessages.BrowseRoomsRequest.Payload browseRoomsRequest -> { // TODO: add functionality
                var rooms = userRoomHandle.getPublicRooms();
                for (var room : rooms) {
                    sendableDispatcher.processSendable(room.getRoomInfo());
                }
            }

            case StartGameRequest.Payload startGameReqPayload -> {
                // TODO: unify this between rooms and matchmaking
                var res = userRoomHandle.createGameRequest();
                // if (res == RoomRequest.SUCCESSFUL) {
                // }
            }

            case MatchmakingParameters findGameMessagePars -> {
                var res = matchmakingHandle.findGame(findGameMessagePars);
            }

            default -> {
            }
        }
    }

    private void sendOneRoomData(String roomName) {
        var roomOptional = userRoomHandle.getRoom(roomName);

        if (roomOptional.isEmpty()) {
            Logger.getGlobal().info("We should not have encountered this state");
            return;
        }

        final var room = roomOptional.get();
        sendableDispatcher.processSendable(room.getRoomInfo());

        for (final var member : userRoomHandle.getRoomMembers()) {
            sendableDispatcher.processSendable(member);
        }
    }

}
