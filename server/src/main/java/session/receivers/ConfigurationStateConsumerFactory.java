package session.receivers;

import user.IMatchmakingHandle;
import user.IUsersRoomHandle;

public class ConfigurationStateConsumerFactory {

    public ConfigurationStateConsumer getConfigurationStateConsumer(IUsersRoomHandle userRoomHandle,
                                                                    IMatchmakingHandle matchmakingHandle) {
        return new ConfigurationStateConsumer(userRoomHandle, matchmakingHandle);
    }
}
