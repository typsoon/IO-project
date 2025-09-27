package session.receivers;

import database.IDatabaseManager.UserId;
import game.session.ISendableConsumer;
import user.IUsersMatchmakingHandle;
import user.IUsersRoomHandle;
import utils.ObserverWithATwist.Subscribable;

public interface IConfigurationStateConsumerFactory {
    public ISendableConsumer getConfigurationStateConsumer(final IUsersRoomHandle userRoomHandle,
            final IUsersMatchmakingHandle matchmakingHandle, final UserId userId, Subscribable subscribable);
}
