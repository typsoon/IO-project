package network.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import database.IDatabaseManager;
import database.IDatabaseManager.UserId;
import network.server.AuthenticationService.Token;

class ConcreteAuthenticationServiceTest {

    private IDatabaseManager databaseManager;
    private ConcreteAuthenticationService authService;
    private UserId userId;

    @BeforeEach
    void setUp() {
        databaseManager = Mockito.mock(IDatabaseManager.class);
        authService = new ConcreteAuthenticationService(databaseManager);
        userId = new UserId(1); // assuming a constructor like this exists
    }

    @Test
    void renewToken_AssignsUniqueTokenAndMapsCorrectly() {
        int tokenVal = authService.renewToken(userId);

        // the token should map back to the same user
        assertEquals(userId, authService.getUser(tokenVal));
    }

    @Test
    void tryAuth_ReturnsNull_OnWrongPassword() {
        when(databaseManager.getPassword(userId)).thenReturn("correct");

        Token token = authService.tryAuth(userId, "wrong");

        assertNull(token, "Authentication with wrong password should return null");
    }

    @Test
    void tryAuth_ReturnsToken_OnCorrectPassword() {
        when(databaseManager.getPassword(userId)).thenReturn("secret");

        Token token = authService.tryAuth(userId, "secret");

        assertNotNull(token, "Authentication with correct password should return a token");
        assertEquals(userId, authService.getUser(token.val()),
                "Token should map back to the authenticated user");
    }

    @Test
    void tryAuth_ReusesExistingToken_OnRepeatedCalls() {
        when(databaseManager.getPassword(userId)).thenReturn("pw");

        Token first = authService.tryAuth(userId, "pw");
        Token second = authService.tryAuth(userId, "pw");

        assertNotNull(first);
        assertNotNull(second);
        assertEquals(first, second, "Repeated tryAuth for same user should return the same token");
    }

    @Test
    void getUser_ReturnsNull_ForUnknownToken() {
        assertNull(authService.getUser(999999), "Unknown token should return null user");
    }
}
