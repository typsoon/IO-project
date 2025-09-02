package dummylaunchers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.GdxRuntimeException;

import game.utility.ISendable;
import gameclient.rooms.RoomConfig;
import gameclient.rooms.RoomRequest;
import network.client.ClientSideSocketWrapper;
import network.client.ClientSideSocketWrapper.EstablishConnectionResult;
import network.client.ClientSideSocketWrapperFactory;
import network.client.DuplexSocketWrapper;
import network.messages.configurationstate.CreateRoomRequestResponse;
import network.messages.configurationstate.GameStartMessages.StartGameRequest;
import network.messages.defaultmessage.ConcreteObjectDecoder;
import network.messages.loginstate.LogInQuery;
import network.messages.loginstate.LogInResponse;
import network.messages.userstate.GameConfirmation;
import network.messages.userstate.GameConfirmation.Confirmation;
import network.messages.userstate.GameConfirmationRequestMessage;
import network.utils.ConnectionData;
import viewmodel.impl.BasicViewManagerInjector;

class DummyNetworkGameLauncher extends Game {

    @Override
    public void create() {
        ClientSideSocketWrapper socketWrapper = new ClientSideSocketWrapperFactory().getClientSideSocketWrapper();
        var objectDecoder = new ConcreteObjectDecoder();

        try {
            socketWrapper.dispatchMessage(new LogInQuery("u1", "p1"));

            var propertiesLoager = new PropertiesLoader();
            var res = socketWrapper
                    .establishConnection(
                            new ConnectionData(propertiesLoager.hostname, propertiesLoager.port,
                                    propertiesLoager.udpPort));

            if (res == EstablishConnectionResult.FAILED) {
                Logger.getGlobal().info("Failed to establish connection");
                return;
            }

            var viewManager = new BasicViewManagerInjector(this).getViewManager();
            int userId = -1;

            while (true) {
                var sendables = socketWrapper.getSendables();

                for (ISendable sendable : sendables) {
                    switch (sendable) {
                        case LogInResponse.Payload logInResponse -> {
                            socketWrapper.dispatchMessage(
                                    objectDecoder.decodeFromRecord(new RoomConfig("asd", "", 1, false)));
                            userId = logInResponse.userId();
                        }

                        case CreateRoomRequestResponse.Payload createRoomRequestResponse -> {
                            assert createRoomRequestResponse.request().equals(RoomRequest.SUCCESSFUL);

                            socketWrapper
                                    .dispatchMessage(objectDecoder.decodeFromRecord(new StartGameRequest.Payload()));
                        }

                        case GameConfirmationRequestMessage.Payload confirmationRequest -> {
                            viewManager.moveToGameClient(socketWrapper, userId);
                            socketWrapper.dispatchMessage(
                                    objectDecoder.decodeFromRecord(new GameConfirmation(Confirmation.CONFIRMED)));
                        }

                        default -> {
                            Logger.getGlobal().severe("Illegal state");
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}

class DummyNetworkLauncher {
    public static void main(String[] args) throws DuplexSocketWrapper.ConnectionEndedException, IOException {

        var applog = Logger.getGlobal();
        Handler systemOut = new ConsoleHandler();
        var level = Level.INFO;
        systemOut.setLevel(level);
        applog.addHandler(systemOut);
        applog.setLevel(level);

        applog.setUseParentHandlers(false);

        final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("Dummy IO Game");
        // config.setWindowedMode(800, 720);
        // TODO: remove magic numbers and strings
        config.setWindowedMode(720, 720);
        new Lwjgl3Application(new GameLauncher(), config);
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
