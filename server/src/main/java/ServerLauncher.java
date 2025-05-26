
import java.io.IOException;

import session.ServerInjector;

public class ServerLauncher {
    public static void main(String[] args) throws IOException {
        // var sessionManager = new SessionManagerInjector().getSessionManager(8080);
        var server = new ServerInjector().getServer(4567, 4568, 4569);

        server.start();
    }
}
