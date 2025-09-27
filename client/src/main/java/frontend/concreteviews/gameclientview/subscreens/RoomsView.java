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
import frontend.concreteviews.gameclientview.GameClientViewEvents;
import frontend.concreteviews.gameclientview.GameClientViewEvents.CreateRoomEvent;
import frontend.concreteviews.gameclientview.ScreenSwitchingUtils;

public class RoomsView extends ScreenAdapter {
    private enum Mode {MAIN, JOIN, CREATE, BROWSE}

    private final ScreenSwitchingUtils screenSwitchingUtils;
    private final GameClientViewData gameClientViewData;
    private final ITextureManager textureManager;
    private final EventListener eventListener;

    private final Stage stage;
    private final Table rootTable;

    private Mode mode = Mode.MAIN;

    public RoomsView(ScreenSwitchingUtils screenSwitchingUtils,
                     GameClientViewData gameClientViewData,
                     ITextureManager textureManager,
                     EventListener eventListener) {
        this.screenSwitchingUtils = screenSwitchingUtils;
        this.gameClientViewData = gameClientViewData;
        this.textureManager = textureManager;
        this.eventListener = eventListener;

        this.stage = new Stage(new ScreenViewport());
        this.rootTable = textureManager.getTable();
        rootTable.setFillParent(true);
        rootTable.defaults().pad(6);

        stage.addActor(rootTable);
        rebuild();
    }

    private void rebuild() {
        rootTable.clear();
        switch (mode) {
            case MAIN -> buildMainMenu();
            case JOIN -> buildJoinView();
            case CREATE -> buildCreateView();
            case BROWSE -> buildBrowseView();
        }
    }

    private void buildMainMenu() {
        rootTable.add(textureManager.getHeading("Rooms")).pad(15);
        rootTable.row();

        var browseBtn = textureManager.getTextButton("Browse rooms");
        var joinBtn = textureManager.getTextButton("Join room");
        var createBtn = textureManager.getTextButton("Create room");
        var backBtn = textureManager.getTextButton("Back");

        browseBtn.addListener(simpleClick(() -> {
            mode = Mode.BROWSE;
            rebuild();
        }));
        joinBtn.addListener(simpleClick(() -> {
            mode = Mode.JOIN;
            rebuild();
        }));
        createBtn.addListener(simpleClick(() -> {
            mode = Mode.CREATE;
            rebuild();
        }));
        backBtn.addListener(simpleClick(screenSwitchingUtils::moveToPreviousSubscreen));

        rootTable.add(joinBtn).width(360);
        rootTable.row();
        rootTable.add(createBtn).width(360);
        rootTable.row();
        rootTable.add(browseBtn).width(360);
        rootTable.row();
        rootTable.add(backBtn).width(360).padTop(10);
    }

    private InputListener simpleClick(Runnable action) {
        return new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y,
                                     final int pointer, final int button) {
                action.run();
                return true;
            }
        };
    }

    private void buildCreateView() {
        rootTable.add(textureManager.getHeading("Create room")).pad(10);
        rootTable.row();

        final var nameField = textureManager.getTextField("Room name");
        final var passwordField = textureManager.getTextField("Password");
        passwordField.setPasswordCharacter('*');
        final var maxField = textureManager.getTextField("Max players");

        rootTable.add(nameField).width(320);
        rootTable.row();
        rootTable.add(passwordField).width(320);
        rootTable.row();
        rootTable.add(maxField).width(320);
        rootTable.row();

        final boolean[] isPublic = new boolean[]{true};
        final var publicToggle = textureManager.getTextButton("Public: ON");
        publicToggle.addListener(simpleClick(() -> {
            isPublic[0] = !isPublic[0];
            publicToggle.setText(isPublic[0] ? "Public: ON" : "Public: OFF");
        }));

        var createBtn = textureManager.getTextButton("Create");
        createBtn.addListener(simpleClick(() -> {
            int maxPlayers = 4;
            try {
                maxPlayers = Integer.parseInt(maxField.getText().trim());
            }
            catch (NumberFormatException ignored) { }
            createBtn.fire(new CreateRoomEvent(nameField.getText(), passwordField.getText(), maxPlayers, isPublic[0]));
        }));

        var backBtn = textureManager.getTextButton("Back");
        backBtn.addListener(simpleClick(() -> {
            mode = Mode.MAIN;
            rebuild();
        }));

        rootTable.add(publicToggle).padTop(8);
        rootTable.row();
        rootTable.add(createBtn).padTop(16);
        rootTable.row();
        rootTable.add(backBtn).padTop(8);
    }

    private void buildJoinView() {
        rootTable.add(textureManager.getHeading("Join room")).pad(10);
        rootTable.row();

        final var nameField = textureManager.getTextField("Room name");
        final var passwordField = textureManager.getTextField("Password");

        var joinBtn = textureManager.getTextButton("Join");
        joinBtn.addListener(simpleClick(() -> {
            joinBtn.fire(new GameClientViewEvents.JoinRoomEvent(nameField.getText(), passwordField.getText()));
        }));

        var backBtn = textureManager.getTextButton("Back");
        backBtn.addListener(simpleClick(() -> {
            mode = Mode.MAIN;
            rebuild();
        }));

        rootTable.add(nameField).width(320);
        rootTable.row();
        rootTable.add(passwordField).width(320);
        rootTable.row();
        rootTable.add(joinBtn).padTop(16);
        rootTable.row();
        rootTable.add(backBtn).padTop(8);
    }

    private void buildBrowseView() {
        rootTable.add(textureManager.getHeading("Browse rooms")).pad(10);
        rootTable.row();

        // Placeholder: w przyszłości wstaw listę z gameClientViewData
        var info = textureManager.getHeading("Feature Coming Soon");
        rootTable.add(info);
        rootTable.row();

        var refreshBtn = textureManager.getTextButton("Refresh");
        refreshBtn.addListener(simpleClick(() -> {
            refreshBtn.fire(new GameClientViewEvents.BrowseRoomsEvent());
        }));

        var backBtn = textureManager.getTextButton("Back");
        backBtn.addListener(simpleClick(() -> {
            mode = Mode.MAIN;
            rebuild();
        }));

        rootTable.add(refreshBtn).padTop(12);
        rootTable.row();
        rootTable.add(backBtn).padTop(8);
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
