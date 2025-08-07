package frontend.concreteviews.gameclientview;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.ScreenUtils;

import viewmodel.AbstractTextureManager;
import viewmodel.AbstractView;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameClientView extends ScreenAdapter implements AbstractView {
    private final Game game;
    private final AbstractTextureManager textureManager;
    private final GameClientViewEventListener gameClientViewEventListener;

    private Stage stage;

    public GameClientView(final Game game, AbstractTextureManager textureManager,
            GameClientViewEventListener gameClientViewEventListener2) {
        this.game = game;
        this.textureManager = textureManager;
        this.gameClientViewEventListener = gameClientViewEventListener2;
    }

    @Override
    public void display() {
        game.setScreen(this);
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

        final Button playButton = textureManager.getTextButton("Play");
        playButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                    final int button) {
                playButton.fire(new GameClientViewEvents.CreateRoomEvent(""));
                return true;
            }
        });

        final Button exitButton = textureManager.getTextButton("Exit");
        exitButton.addListener(new ClickListener() {
            public void clicked(final InputEvent event, final float x, final float y) {
                Gdx.app.exit();
            }
        });

        final Table table = textureManager.getTable();
        table.row();
        table.add(playButton);
        table.getCell(playButton).spaceBottom(40);
        table.row();
        table.add(exitButton);

        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
