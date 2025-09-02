
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import session.ServerInjector;

public class ServerLauncher {
    public static void main(String[] args) throws IOException {
        var applog = Logger.getGlobal();
        Handler systemOut = new ConsoleHandler();
        var level = Level.INFO;
        // var level = Level.FINER;
        // var level = Level.FINEST;
        systemOut.setLevel(level);
        applog.addHandler(systemOut);
        applog.setLevel(level);

        applog.setUseParentHandlers(false);

        var server = new ServerInjector().getServer(4567, 4568, 4569);

        server.start();
    }
}
