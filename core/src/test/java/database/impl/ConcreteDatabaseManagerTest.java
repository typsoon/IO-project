package database.impl;

import database.IDatabaseManager;
import database.IDatabaseManager.UserId;
import game.engine.PlayerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConcreteDatabaseManagerTest {

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
        assertEquals(pass, db.getPassword(id), "Hasło powinno się zgadzać");

        PlayerConfig cfg = db.getPlayerConfig(id);
        assertNotNull(cfg, "Konfiguracja gracza nie powinna być `null`");
        assertEquals(new PlayerConfig(), cfg, "Domyślna konfiguracja powinna być zwracana");
    }

    @Test
    void getPasswordWithNullIdReturnsNull() {
        assertNull(db.getPassword(null), "Dla `null` jako `UserId` powinno zwrócić `null`");
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
        assertEquals("p1", db.getPassword(firstId),
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
}
