package network.socketwrappers.concretesocketwrappers;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ConcreteServerSocketWrapperTest {
    private ServerSocket serverSocket;
    private ConcreteServerSocketWrapper concreteServerSocketWrapper;

    @BeforeEach
    void init() {
        serverSocket = mock(ServerSocket.class);
        concreteServerSocketWrapper = new ConcreteServerSocketWrapper(serverSocket);
    }

    @Test
    void closeShouldntFailIfServerSocketCloseDoesnt() {
        assertDoesNotThrow(() -> doNothing().when(serverSocket).close(),
                "The close method of the ServerSocket should not throw an exception when it is mocked to do nothing.");

        assertDoesNotThrow(concreteServerSocketWrapper::close,
                "The close method of the ConcreteServerSocketWrapper should not throw an exception.");
    }

    @Test
    void acceptClientShouldThrowAfterClosing() {
        closeShouldntFailIfServerSocketCloseDoesnt();

        assertDoesNotThrow(() -> doThrow(new IOException("Accept was called after closing")).when(serverSocket).accept(),
                "Mocking shouldn't throw");

        assertThrows(Throwable.class, concreteServerSocketWrapper::acceptClient,
                "Exception should be thrown when calling accept after close");
    }
}
