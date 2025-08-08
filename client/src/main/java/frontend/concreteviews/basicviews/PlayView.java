package frontend.concreteviews.basicviews;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;

import network.client.ClientSideSocketWrapperFactory;
import network.utils.ConnectionData;
import viewmodel.IView;
import viewmodel.IViewManager;

public class PlayView extends ScreenAdapter implements IView {
    private final Game game;
    private final IViewManager viewManager;
    private final ClientSideSocketWrapperFactory ClientSideSocketWrapperFactory;

    private Stage stage;

    public PlayView(final Game game, final IViewManager viewManager,
            ClientSideSocketWrapperFactory clientSideSocketWrapperFactory) {
        this.game = game;
        this.viewManager = viewManager;
        this.ClientSideSocketWrapperFactory = clientSideSocketWrapperFactory;
    }

    @Override
    public void display() {
        game.setScreen(this);
    }

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(0, 0, 0, 1, true);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void show() {
        // TODO hardcoded: remove hardcoded strings, use config instead
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        final Button buttonConnect = viewManager.getTextureManager().getTextButton("Connect");
        buttonConnect.addListener(new ClickListener() {
            public void clicked(final InputEvent event, final float x, final float y) {
                // TODO: change to a proper use of viewManager
                var clientSideSocketWrapper = ClientSideSocketWrapperFactory.getClientSideSocketWrapper();

                var propertiesLoader = new PropertiesLoader();
                var connectionData = new ConnectionData(propertiesLoader.hostname, propertiesLoader.port,
                        propertiesLoader.udp_port);
                clientSideSocketWrapper.establishConnection(connectionData);

                viewManager.moveToLoginView(clientSideSocketWrapper);
            }
        });

        final Button buttonBack = viewManager.getTextureManager().getTextButton("Back");
        buttonBack.addListener(new ClickListener() {
            public void clicked(final InputEvent event, final float x, final float y) {
                viewManager.moveToMainMenu();
            }
        });

        final Table table = viewManager.getTextureManager().getTable();
        table.add(buttonConnect);
        table.getCell(buttonConnect).spaceBottom(40);
        table.row();
        table.add(buttonBack);

        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

class PropertiesLoader {
    String hostname;
    int port;
    int udp_port;

    PropertiesLoader() {
        var fileName = "ServerAddress.properties";
        Properties properties = new Properties();
        try (
                InputStream input = Gdx.files.internal(fileName).read()) {
            properties.load(input);

            hostname = properties.getProperty("hostname");
            port = Integer.parseInt(properties.getProperty("port"));
            udp_port = Integer.parseInt(properties.getProperty("udp_port"));
        } catch (IOException | GdxRuntimeException e) { // remove the need to copy that random file from Sitson
            hostname = "localhost";
            port = 4567;
            udp_port = 4568;
            // throw new RuntimeException(e);
        }
    }
}
