package session.receivers;

import game.session.ISendableConsumer;
import user.IUsersMatchmakingHandle;
import user.IUsersRoomHandle;

public interface IConfigurationStateConsumerFactory {
    public ISendableConsumer getConfigurationStateConsumer(final IUsersRoomHandle userRoomHandle,
            final IUsersMatchmakingHandle matchmakingHandle);
}
