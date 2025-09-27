package user.impl;

import gameclient.user.IUserView;
import matchmaking.IMatchmakingEngine;
import room.IRoomManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.IMatchmakingUserHandle;
import user.UsersHandles;
import utils.ObserverWithATwist;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersHandlesFactoryTest {

    private IRoomManager roomManager;
    private IMatchmakingEngine matchmakingEngine;
    private UsersHandlesFactory factory;
    private final ObserverWithATwist.Notifiable subscriber = mock(ObserverWithATwist.Notifiable.class);

    @BeforeEach
    void setUp() {
        roomManager = mock(IRoomManager.class);
        matchmakingEngine = mock(IMatchmakingEngine.class);
        factory = new UsersHandlesFactory(roomManager, matchmakingEngine);
    }

    @Test
    void getUsersHandles_returnsNonNullHandles() {
        IUserView userView = mock(IUserView.class);
        IMatchmakingUserHandle matchmakingUserHandle = mock(IMatchmakingUserHandle.class);

        UsersHandles handles = factory.getUsersHandles(userView, matchmakingUserHandle, subscriber);

        assertNotNull(handles);
        assertNotNull(handles.roomHandle());
        assertNotNull(handles.matchmakingHandle());
    }
}