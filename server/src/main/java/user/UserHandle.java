package user;

import game.engine.PlayerConfig;

public interface UserHandle {
    UserRoomHandler getUserRoomHandler();

    PlayerConfig getPlayerConfig();
}
