package session.receivers;

import user.IUsersMatchmakingHandle;
import user.IUsersRoomHandle;

public class ConfigurationStateConsumerFactory {

    public ConfigurationStateConsumer getConfigurationStateConsumer(IUsersRoomHandle userRoomHandle,
                                                                    IUsersMatchmakingHandle matchmakingHandle) {
        return new ConfigurationStateConsumer(userRoomHandle, matchmakingHandle);
    }
}
