package frontend.concreteviews.gameclientview;

import java.io.IOException;
import java.util.logging.Logger;

import database.IDatabaseManager.UserId;
import game.session.ISendableConsumer;
import game.utility.ISendable;
import gameclient.rooms.RoomInfo;
import gameclient.rooms.UserMembershipInfo;
import gameclient.user.UserInfo;
import network.client.ClientSideSocketWrapper;
import network.messages.userstate.GameConfirmationRequestMessage;
import utility.ICyclePerformer;
import viewmodel.IViewManager;

public class GameClientViewMessageHandler implements ICyclePerformer {
    private final GameClientViewData gameClientViewData;
    private final ClientSideSocketWrapper clientSideSocketWrapper;
    private final IViewManager viewManager;
    private final ISendableConsumer displayHandler;

    public GameClientViewMessageHandler(GameClientViewData gameClientViewData,
            ClientSideSocketWrapper clientSideSocketWrapper, IViewManager viewManager,
            ISendableConsumer displayHandler) {
        this.gameClientViewData = gameClientViewData;
        this.clientSideSocketWrapper = clientSideSocketWrapper;
        this.viewManager = viewManager;
        this.displayHandler = displayHandler;
    }

    @Override
    public void performCycle() {
        try {
            var sendables = clientSideSocketWrapper.getSendables();

            for (ISendable sendable : sendables) {
                switch (sendable) {
                    case RoomInfo roomInfo -> {
                        gameClientViewData.addRoom(roomInfo);
                    }

                    case UserMembershipInfo userMembershipInfo -> {
                        var roomName = userMembershipInfo.roomName();
                        var userInfo = new UserInfo(new UserId(userMembershipInfo.userID()),
                                userMembershipInfo.username());
                        gameClientViewData.addAnUserToARoom(roomName, userInfo);
                    }

                    case GameConfirmationRequestMessage.GameConfirmationRequest gameConfirmationRequest -> {

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
