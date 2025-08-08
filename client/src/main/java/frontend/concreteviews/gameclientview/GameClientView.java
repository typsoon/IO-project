package frontend.concreteviews.gameclientview;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import viewmodel.TextureManager;

public class GameClientView extends ScreenAdapter {
    private final Game game;
    private final TextureManager textureManager;
    private final GameClientViewEventListener gameClientViewEventListener;

    private Stage stage;

    public GameClientView(final Game game, TextureManager textureManager,
            GameClientViewEventListener gameClientViewEventListener2) {
        this.game = game;
        this.textureManager = textureManager;
        this.gameClientViewEventListener = gameClientViewEventListener2;
    }

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
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

        stage.addListener(this.gameClientViewEventListener);

        final Button exitButton = textureManager.getTextButton("Exit");
        exitButton.addListener(new ClickListener() {
            public void clicked(final InputEvent event, final float x, final float y) {
                Gdx.app.exit();
            }
        });

        var roomActionsTable = getRoomActionsTable();
        var matchmakingActionsTable = getMatchmakingActionsTable();

        var mainTable = textureManager.getTable();
        mainTable.add(matchmakingActionsTable).expandX();
        mainTable.add(roomActionsTable).spaceBottom(40).expandX();
        mainTable.row();
        mainTable.add(exitButton).colspan(2);

        stage.addActor(mainTable);
    }

    private final Actor getRoomActionsTable() {
        final var roomNameField = textureManager.getTextField("Room name");

        final Button createRoomButton = textureManager.getTextButton("Create room");
        createRoomButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                    final int button) {
                createRoomButton.fire(new GameClientViewEvents.CreateRoomEvent(roomNameField.getText()));
                return true;
            }
        });

        final Button joinRoomButton = textureManager.getTextButton("Browse rooms");
        joinRoomButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                    final int button) {
                joinRoomButton.fire(new GameClientViewEvents.CreateRoomEvent(roomNameField.getText()));
                return true;
            }
        });

        // final Table roomActionsTable = textureManager.getTable();
        final Table answer = new Table();
        answer.add(roomNameField);
        answer.getCell(roomNameField).spaceBottom(40).width(300);
        answer.row();
        answer.add(createRoomButton);
        answer.getCell(createRoomButton).spaceBottom(40);
        answer.row();
        answer.add(joinRoomButton);
        answer.getCell(joinRoomButton).spaceBottom(40);

        return answer;
    }

    private final Actor getMatchmakingActionsTable() {
        final Button findGameButton = textureManager.getTextButton("Find game");
        findGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                    final int button) {
                // playButton.fire(new
                // GameClientViewEvents.CreateRoomEvent(roomNameField.getText()));
                return true;
            }
        });

        // final Table roomActionsTable = textureManager.getTable();
        final Table answer = new Table();
        answer.add(findGameButton).expandY();
        answer.getCell(findGameButton).spaceBottom(40);

        return answer;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
