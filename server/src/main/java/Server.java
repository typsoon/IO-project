
import java.io.IOException;

import session.SessionManagerInjector;

public class Server {
    public static void main(String[] args) throws IOException {
        // var sessionManager = new SessionManagerInjector().getSessionManager(8080);
        var sessionManager = new SessionManagerInjector().getSessionManager(4567);

        sessionManager.start();
    }
}
