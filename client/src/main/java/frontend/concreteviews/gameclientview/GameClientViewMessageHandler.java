package frontend.concreteviews.gameclientview;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

import database.IDatabaseManager.UserId;
import game.gamestates.IGameState;
import game.session.ISendableConsumer;
import gameclient.rooms.RoomInfo;
import gameclient.rooms.RoomRequestResult;
import gameclient.rooms.UserMembershipInfo;
import gameclient.user.UserInfo;
import network.client.ClientSideSocketWrapper;
import network.messages.configurationstate.GameStartMessages.GameStartedNotification;
import network.messages.loginstate.PortInfoResponse;
import network.messages.userstate.GameConfirmationRequestMessage;
import utility.ICyclePerformer;
import utils.BoundedQueue;
import utils.ISendable;
import viewmodel.IViewManager;

public class GameClientViewMessageHandler implements ICyclePerformer {
    private final GameClientViewData gameClientViewData;
    private final ClientSideSocketWrapper clientSideSocketWrapper;
    private final IViewManager viewManager;
    private final ISendableConsumer displayHandler;
    private final int userId;

    private Collection<IGameState> initialGameStates = new BoundedQueue<>(1 << 16);

    public GameClientViewMessageHandler(GameClientViewData gameClientViewData,
            ClientSideSocketWrapper clientSideSocketWrapper, IViewManager viewManager,
            ISendableConsumer displayHandler, int userId) {
        this.gameClientViewData = gameClientViewData;
        this.clientSideSocketWrapper = clientSideSocketWrapper;
        this.viewManager = viewManager;
        this.displayHandler = displayHandler;
        this.userId = userId;
    }

    // private long lastLogTimeNs = 0;
    // private long second = 1_000_000_000L;

    @Override
    public void performCycle() {
        try {
            // long now = System.nanoTime();
            //
            // if (now - lastLogTimeNs > second) {
            // double secondsSinceLast = (now - lastLogTimeNs) / second;
            // Logger.getGlobal().info(
            // String.format("performCycle called, last call was %.3f seconds ago",
            // secondsSinceLast));
            // lastLogTimeNs = now;
            // }

            var sendables = clientSideSocketWrapper.getSendables();

            if (!sendables.isEmpty()) {
                Logger.getGlobal().info("Received sendables %s".formatted(sendables));
            }

            for (ISendable sendable : sendables) {
                switch (sendable) {
                    case RoomInfo roomInfo -> {
                        gameClientViewData.clearRoomContents(roomInfo.roomName());
                        gameClientViewData.addRoom(roomInfo);
                    }

                    case PortInfoResponse.Payload portInfoResponse -> {
                    }

                    case UserMembershipInfo userMembershipInfo -> {
                        var roomName = userMembershipInfo.roomName();
                        var userInfo = new UserInfo(new UserId(userMembershipInfo.userID()),
                                userMembershipInfo.username());
                        gameClientViewData.addAnUserToARoom(roomName, userInfo, userMembershipInfo.isAdmin());
                    }

                    case GameConfirmationRequestMessage.Payload gameConfirmationRequest -> {
                        Logger.getGlobal().info("Moving to confirmation screen");
                        displayHandler.processSendable(gameConfirmationRequest);
                    }

                    case RoomRequestResult roomRequestResponse -> {
                        displayHandler.processSendable(roomRequestResponse);
                    }

                    case GameStartedNotification.Payload gameStartedNotificationPayload -> {
                        viewManager.moveToGameplay(clientSideSocketWrapper, userId, initialGameStates);
                    }

                    // case Room

                    case IGameState gameState -> {
                        Logger.getGlobal().info("Adding gamestate");
                        initialGameStates.add(gameState);
                    }

                    default ->
                        throw new IllegalStateException(
                                "We should not have received this message right now %s".formatted(sendable));
                }
            }
        } catch (IOException e) {
            Logger.getGlobal().severe("IOException caught!");
        }
    }
}
