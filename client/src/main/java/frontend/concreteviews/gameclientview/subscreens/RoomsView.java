// java
package frontend.concreteviews.gameclientview.subscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import frontend.assetsloading.ITextureManager;
import frontend.concreteviews.gameclientview.GameClientViewData;
//import frontend.concreteviews.gameclientview.GameClientViewEvents.BrowseRoomsEvent;
//import frontend.concreteviews.gameclientview.GameClientViewEvents.CreateRoomEvent;
//import frontend.concreteviews.gameclientview.GameClientViewEvents.JoinRoomEvent;
import frontend.concreteviews.gameclientview.ScreenSwitchingUtils;

public class RoomsView extends ScreenAdapter {
    private final ScreenSwitchingUtils screenSwitchingUtils;
    private final GameClientViewData gameClientViewData;
    private final ITextureManager textureManager;
    private final EventListener eventListener;

    private final Stage stage;
    private final Table rootTable;

    public RoomsView(ScreenSwitchingUtils screenSwitchingUtils, GameClientViewData gameClientViewData,
                     ITextureManager textureManager, EventListener eventListener) {
        this.screenSwitchingUtils = screenSwitchingUtils;
        this.gameClientViewData = gameClientViewData;
        this.textureManager = textureManager;
        this.eventListener = eventListener;

        this.stage = new Stage(new ScreenViewport());
        this.rootTable = textureManager.getTable();
        rootTable.setFillParent(true);

        stage.addActor(rootTable);
        buildUI();
    }

    private void buildUI() {
        rootTable.clear();

        rootTable.add(textureManager.getHeading("Rooms")).colspan(2).pad(15);
        rootTable.row();

        final var roomNameField = textureManager.getTextField("Room name");
        final var passwordField = textureManager.getTextField("Password");
        final var maxPlayersField = textureManager.getTextField("Max players (e.g. 4)");

        rootTable.add(roomNameField).width(300).pad(5).colspan(2);
        rootTable.row();
        rootTable.add(passwordField).width(300).pad(5).colspan(2);
        rootTable.row();
        rootTable.add(maxPlayersField).width(300).pad(5).colspan(2);
        rootTable.row();

        final var createBtn = textureManager.getTextButton("Create room");
        final var joinBtn = textureManager.getTextButton("Join room");
        final var browseBtn = textureManager.getTextButton("Browse rooms");
        final var backBtn = textureManager.getTextButton("Back");

        createBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y,
                                     final int pointer, final int button) {
                int maxPlayers = 4;
                try {
                    maxPlayers = Integer.parseInt(maxPlayersField.getText().trim());
                }
                catch (NumberFormatException ignored) { }
                // Public room domyślnie; rozszerz UI jeśli chcesz przełącznik public/private
//                createBtn.fire(new CreateRoomEvent(roomNameField.getText(), passwordField.getText(), maxPlayers, true));
                return true;
            }
        });

        joinBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y,
                                     final int pointer, final int button) {
//                joinBtn.fire(new JoinRoomEvent(roomNameField.getText(), passwordField.getText()));
                return true;
            }
        });

        browseBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y,
                                     final int pointer, final int button) {
//                browseBtn.fire(new BrowseRoomsEvent());
                return true;
            }
        });

        backBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y,
                                     final int pointer, final int button) {
                screenSwitchingUtils.moveToPreviousSubscreen();
                return true;
            }
        });

        rootTable.add(createBtn).pad(10);
        rootTable.add(joinBtn).pad(10);
        rootTable.row();
        rootTable.add(browseBtn).colspan(2).padTop(20);
        rootTable.row();
        rootTable.add(backBtn).colspan(2).padTop(10);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.addListener(eventListener);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
