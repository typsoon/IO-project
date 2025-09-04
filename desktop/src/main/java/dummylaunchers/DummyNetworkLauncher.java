package dummylaunchers;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.GdxRuntimeException;

import gameclient.rooms.RequestResult;
import gameclient.rooms.RoomConfig;
import gameclient.rooms.RoomRequestResult;
import network.client.ClientSideSocketWrapper;
import network.client.ClientSideSocketWrapper.EstablishConnectionResult;
import network.client.ClientSideSocketWrapperFactory;
import network.client.DuplexSocketWrapper;
import network.client.DuplexSocketWrapper.ConnectionEndedException;
import network.messages.configurationstate.GameStartMessages.StartGameRequest;
import network.messages.configurationstate.RoomMessages.JoinRoomRequest;
import network.messages.defaultmessage.ConcreteObjectDecoder;
import network.messages.defaultmessage.ObjectToMessageDecoder;
import network.messages.loginstate.LogInQuery;
import network.messages.loginstate.LogInResponse;
import network.messages.loginstate.PortInfoResponse;
import network.messages.userstate.GameConfirmation;
import network.messages.userstate.GameConfirmation.Confirmation;
import network.messages.userstate.GameConfirmationRequestMessage;
import network.utils.ConnectionData;
import utils.ISendable;
import viewmodel.impl.BasicViewManagerInjector;

class DummyNetworkGameLauncher extends Game {
    private static final LogInQuery[] logInQueries = { new LogInQuery("u1", "p1"), new LogInQuery("u2", "p2"),
            new LogInQuery("u3", "p3") };

    private final ClientSideSocketWrapper socketWrapper = new ClientSideSocketWrapperFactory()
            .getClientSideSocketWrapper();
    private final ObjectToMessageDecoder objectDecoder = new ConcreteObjectDecoder();
    private final RoomConfig roomConfig;

    private final LogInQuery myLogInQuery;
    private final int maxPlayersInRoom;

    public DummyNetworkGameLauncher(int whichPlayer, int maxPlayersInRoom) {
        myLogInQuery = logInQueries[whichPlayer];
        roomConfig = new RoomConfig("asd", "", maxPlayersInRoom, false);
        this.maxPlayersInRoom = maxPlayersInRoom;
    }

    private void mockingLoop() throws IOException, Exception {
        var propertiesLoager = new PropertiesLoader();
        var res = socketWrapper.establishConnection(
                new ConnectionData(propertiesLoager.hostname, propertiesLoager.port,
                        propertiesLoager.udpPort));

        if (res == EstablishConnectionResult.FAILED) {
            Logger.getGlobal().info("Failed to establish connection");
            return;
        }

        var viewManager = new BasicViewManagerInjector(this).getViewManager();
        int userId = -1;

        socketWrapper.dispatchMessage(myLogInQuery);
        while (true) {
            var sendables = socketWrapper.getSendables();

            for (ISendable sendable : sendables) {
                switch (sendable) {
                    case LogInResponse.Payload logInResponse -> {
                        userId = logInResponse.userId();
                    }

                    case PortInfoResponse.Payload portInfoResponsePayload -> {
                        socketWrapper.dispatchMessage(
                                objectDecoder.decodeFromRecord(roomConfig));

                    }

                    case RoomRequestResult roomRequestResult -> {
                        handleRequestResult(roomRequestResult);
                    }

                    case GameConfirmationRequestMessage.Payload confirmationRequest -> {
                        viewManager.moveToGameClient(socketWrapper, userId);
                        socketWrapper.dispatchMessage(
                                objectDecoder.decodeFromRecord(new GameConfirmation(Confirmation.CONFIRMED)));
                        return;
                    }

                    default -> {
                        Logger.getGlobal().severe("Illegal state %s".formatted(sendable));
                    }
                }
            }
        }

    }

    @Override
    public void create() {
        try {
            mockingLoop();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    private void handleRequestResult(RoomRequestResult roomRequestResult) throws IOException, ConnectionEndedException {
        switch (roomRequestResult.roomRequestType()) {
            case CREATE -> {
                switch (roomRequestResult.result()) {
                    case RequestResult.SUCCESSFUL -> {
                        if (maxPlayersInRoom > 1) {
                            try {
                                Thread.sleep(Duration.ofSeconds(3));
                            } catch (Exception e) {
                            }
                        }

                        socketWrapper.dispatchMessage(
                                objectDecoder.decodeFromRecord(new StartGameRequest.Payload()));
                    }

                    case RequestResult.FAILED -> {
                        var joinRoomRequest = new JoinRoomRequest.Payload(roomConfig.name(), roomConfig.password());
                        socketWrapper.dispatchMessage(objectDecoder.decodeFromRecord(joinRoomRequest));
                    }

                    default -> {

                    }
                }
            }
            case JOIN -> {
                if (roomRequestResult.result() == RequestResult.FAILED) {
                    throw new IllegalStateException("Failed to join room");
                }
            }
        }
    }
}

class DummyNetworkLauncher {
    public static void main(String[] args) throws DuplexSocketWrapper.ConnectionEndedException, IOException {
        int whichPlayer = args.length > 0 ? Integer.parseInt(args[0]) : 0;

        int maxPlayersInRoom = args.length > 1 ? Integer.parseInt(args[1]) : 1;
        // Logger.getGlobal().info(Arrays.asList(args).toString());
        Logger.getGlobal().info(String.valueOf(whichPlayer));

        var applog = Logger.getGlobal();
        Handler systemOut = new ConsoleHandler();

        var logPath = "logs/player_u%d.log".formatted(whichPlayer + 1);
        var logFile = new java.io.File(logPath);
        logFile.getParentFile().mkdirs(); // Create parent directories if needed
        logFile.createNewFile(); // Create the file if it doesn't exist
        var fileHandler = new FileHandler(logPath, false);

        // var level = Level.FINEST;
        var level = Level.INFO;
        systemOut.setLevel(level);
        if (maxPlayersInRoom == 0) {
            applog.addHandler(systemOut);
        }
        applog.addHandler(fileHandler);
        applog.setLevel(level);

        applog.setUseParentHandlers(false);

        final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("Dummy IO Game");
        // config.setWindowedMode(800, 720);
        // TODO: remove magic numbers and strings
        config.setWindowedMode(720, 720);
        new Lwjgl3Application(new DummyNetworkGameLauncher(whichPlayer, maxPlayersInRoom), config);
    }

}

class PropertiesLoader {
    String hostname;
    int port;
    int udpPort;

    PropertiesLoader() {
        var fileName = "ServerAddress.properties";
        Properties properties = new Properties();
        try (
                InputStream input = Gdx.files.internal(fileName).read()) {
            properties.load(input);

            hostname = properties.getProperty("hostname");
            port = Integer.parseInt(properties.getProperty("port"));
            udpPort = Integer.parseInt(properties.getProperty("udp_port"));
        } catch (IOException | GdxRuntimeException e) { // remove the need to copy that random file from Sitson
            hostname = "localhost";
            port = 4567;
            udpPort = 4568;
            // throw new RuntimeException(e);
        }
    }
}
