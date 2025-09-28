package database.impl;

import database.IDatabaseManager.UserId;
import game.engine.PlayerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConcreteDatabaseManagerTest {
    private ConcreteDatabaseManager db;

    @BeforeEach
    void setUp() {
        db = new ConcreteDatabaseManager();
    }

    @Test
    void shouldReturnUserIdForKnownLogin() {
        UserId u1 = db.getUserId("u1");
        assertNotNull(u1, "Powinno zwrócić `UserId` dla istniejącego loginu");
    }

    @Test
    void shouldReturnNullForUnknownLogin() {
        assertNull(db.getUserId("no_such_user"),
                "Dla nieistniejącego loginu powinno zwrócić `null`");
    }

    @Test
    void distinctUsersHaveDistinctIds() {
        UserId u1 = db.getUserId("u1");
        UserId u2 = db.getUserId("u2");
        UserId u3 = db.getUserId("u3");
        UserId u = db.getUserId("u");

        assertNotNull(u1);
        assertNotNull(u2);
        assertNotNull(u3);
        assertNotNull(u);

        assertNotEquals(u1, u2);
        assertNotEquals(u1, u3);
        assertNotEquals(u2, u3);
        assertNotEquals(u, u1);
        assertNotEquals(u, u2);
        assertNotEquals(u, u3);
    }

    @Test
    void addUserShouldMakeUserRetrievableByAllAPIs() {
        String login = "alice";
        String pass = "secret";

        db.addUser(login, pass);

        UserId id = db.getUserId(login);
        assertNotNull(id, "Po dodaniu użytkownika `UserId` nie powinien być `null`");

        assertEquals(login, db.getPlayerUsername(id), "Nazwa użytkownika powinna się zgadzać");
        assertTrue(db.checkPassword(id, pass), "Hasło powinno się zgadzać");

        PlayerConfig cfg = db.getPlayerConfig(id);
        assertNotNull(cfg, "Konfiguracja gracza nie powinna być `null`");
        assertEquals(new PlayerConfig(), cfg, "Domyślna konfiguracja powinna być zwracana");
    }

    @Test
    void getPlayerConfigWithNullIdReturnsNull() {
        assertNull(db.getPlayerConfig(null), "Dla `null` jako `UserId` powinno zwrócić `null`");
    }

    @Test
    void getPlayerUsernameWithNullIdReturnsNull() {
        assertNull(db.getPlayerUsername(null), "Dla `null` jako `UserId` powinno zwrócić `null`");
    }

    @Test
    void getUserIdWithNullLoginReturnsNull() {
        assertNull(db.getUserId(null), "Dla `null` loginu powinno zwrócić `null`");
    }

    @Test
    void addingDuplicateLoginDoesNotOverwriteFirstUser() {
        // Stan początkowy zawiera `u1` o haśle `p1`
        UserId firstId = db.getUserId("u1");
        assertNotNull(firstId);

        // Dodaj ponownie ten sam login z innym hasłem
        db.addUser("u1", "new-pass");

        // `getUserId` zwraca pierwsze trafienie, więc hasło powinno nadal być `p1`
        assertTrue(db.checkPassword(firstId, "p1"),
                "Pierwszy wpis nie powinien zostać nadpisany przez duplikat loginu");
    }

    @Test
    void exhaustingIdsPreventsFurtherAdds() {
        // Dodawaj unikalnych użytkowników, aż dodawanie przestanie działać (pula 100 id, 4 już użyte)
        int overflowAt = -1;
        for (int i = 0; i < 200; i++) {
            String login = "fill_user_" + i;
            db.addUser(login, "pw_" + i);
            UserId id = db.getUserId(login);
            if (id == null) {
                overflowAt = i;
                break;
            }
        }
        assertTrue(overflowAt >= 0, "Powinno dojść do wyczerpania puli identyfikatorów");

        // Po wyczerpaniu puli, kolejne dodanie nie powinno zarejestrować użytkownika
        String extraLogin = "overflow_user";
        db.addUser(extraLogin, "pw");
        assertNull(db.getUserId(extraLogin),
                "Po wyczerpaniu puli id nowe konto nie powinno zostać dodane");
    }

    @Test
    void defaultUsersExposeDefaultPlayerConfig() {
        UserId id = db.getUserId("u2");
        assertNotNull(id);
        assertEquals(new PlayerConfig(), db.getPlayerConfig(id),
                "Domyślna konfiguracja gracza powinna być zwracana dla użytkowników startowych");
    }

    @Test
    public void getUserIdKnownTest() {
        String login = "u1";

        assertNotNull(db.getUserId(login));
    }

    @Test
    public void getUserIdUnknownTest() {
        String unknownLogin = "lkjhgfdsa";

        assertNull(db.getUserId(unknownLogin));
    }

    @Test
    public void getUserIdNullTest() {
        assertNull(db.getUserId(null));
    }

    @Test
    public void getPlayerConfigTest() {
        String login = "u1";
        var id = db.getUserId(login);
        assertNotNull(db.getPlayerConfig(id));
    }

    @Test
    public void getPlayerConfigNullTest() {
        assertNull(db.getPlayerConfig(null));
    }

    @Test
    public void getPlayerUsernameTest() {
        String login = "u1";
        var id = db.getUserId(login);

        assertEquals(login, db.getPlayerUsername(id));
    }

    @Test
    public void getPlayerUsernameNullTest() {
        assertNull(db.getPlayerUsername(null));
    }

    @Test
    public void addUserTest() {
        String newLogin = "newUserLogin";
        String newPassword = "newUserPassword";

        assertTrue(db.addUser(newLogin, newPassword));
    }

    @Test
    public void addUserNullUserTest() {
        String password = "newPassword";

        assertFalse(db.addUser(null, password));
    }

    @Test
    public void addUserNullPasswordTest() {
        String login = "newLogin";

        assertFalse(db.addUser(login, null));
    }

    @Test
    public void addUserBothNullTest() {
        assertFalse(db.addUser(null, null));
    }

    @Test
    public void addUserConflictTest() {
        String existingLogin = "u1";

        String truePassword = "p1";
        assertFalse(db.addUser(existingLogin, truePassword));

        String wrongPassword = "lkjhgfdsa";
        assertFalse(db.addUser(existingLogin, wrongPassword));

        assertFalse(db.addUser(existingLogin, null));
    }

    @Test
    public void checkPasswordSuccessTest() {
        String login = "u1";
        var id = db.getUserId(login);
        String password = "p1";

        assertTrue(db.checkPassword(id, password));
    }

    @Test
    public void checkPasswordFailureTest() {
        String login = "u1";
        var id = db.getUserId(login);
        String wrongPassword = "lkjhgfdsa";

        assertFalse(db.checkPassword(id, wrongPassword));
    }

    @Test
    public void checkPasswordNullIdTest() {
        String password = "lkjhgfdsa";

        assertFalse(db.checkPassword(null, password));
    }

    @Test
    public void checkPasswordNullPasswordTest() {
        String login = "u1";
        var id = db.getUserId(login);

        assertFalse(db.checkPassword(id, null));
    }

    @Test
    public void checkPasswordBothNullTest() {
        assertFalse(db.checkPassword(null, null));
    }
}
