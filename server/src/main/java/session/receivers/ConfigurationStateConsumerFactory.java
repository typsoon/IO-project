package session.receivers;

import user.IMatchmakingHandle;
import user.IUserRoomHandle;

public class ConfigurationStateConsumerFactory {

    public ConfigurationStateConsumer getConfigurationStateConsumer(IUserRoomHandle userRoomHandle,
            IMatchmakingHandle matchmakingHandle) {
        return new ConfigurationStateConsumer(userRoomHandle, matchmakingHandle);
    }
}
