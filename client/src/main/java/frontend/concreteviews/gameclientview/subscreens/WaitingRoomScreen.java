package frontend.concreteviews.gameclientview.subscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import frontend.concreteviews.gameclientview.GameClientViewData;
import frontend.concreteviews.gameclientview.ScreenSwitchingUtils;
import gameclient.rooms.RoomInfo;
import gameclient.user.UserInfo;
import viewmodel.ITextureManager;

public class WaitingRoomScreen extends ScreenAdapter {
    private final ScreenSwitchingUtils screenSwitchingUtils;
    private final GameClientViewData gameClientViewData;
    private final String roomName;
    private final ITextureManager textureManager;

    private final Stage stage;
    private final Table rootTable;
    private final LabelStyle labelStyle = new LabelStyle();

    public WaitingRoomScreen(ScreenSwitchingUtils screenSwitchingUtils, GameClientViewData gameClientViewData,
            String roomName, ITextureManager textureManager) {
        this.screenSwitchingUtils = screenSwitchingUtils;
        this.gameClientViewData = gameClientViewData;
        this.roomName = roomName;
        this.textureManager = textureManager;

        this.stage = new Stage(new ScreenViewport());
        this.rootTable = new Table();
        rootTable.setFillParent(true);

        stage.addActor(rootTable);
        buildUI();
    }

    private void buildUI() {
        rootTable.clear();

        RoomInfo roomInfo = gameClientViewData.getRoomInfo(roomName);

        // Nagłówek
        var label = textureManager.getHeading("Waiting Room: " + roomInfo.roomName());
        rootTable.add(label).colspan(2).pad(10);
        rootTable.row();

        // Lista użytkowników

        var label2 = textureManager.getHeading("Players:");
        rootTable.add(label2).left().pad(5);
        rootTable.row();

        for (UserInfo user : gameClientViewData.getUsersInRoom(roomName)) {
            rootTable.add(new Label(user.username(), labelStyle)).left().pad(3);
            rootTable.row();
        }

        var backButton = textureManager.getTextButton("Back to Browse Rooms");
        backButton.addListener(e -> {
            if (backButton.isPressed()) {
                screenSwitchingUtils.moveToPreviousSubscreen();
                return true;
            }
            return false;
        });

        rootTable.add(backButton).colspan(2).padTop(20);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
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
