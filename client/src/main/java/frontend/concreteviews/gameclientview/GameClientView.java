package frontend.concreteviews.gameclientview;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.ScreenUtils;

import viewmodel.AbstractView;
import viewmodel.AbstractViewManager;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameClientView extends ScreenAdapter implements AbstractView {
    private final Game game;
    private final AbstractViewManager viewManager;

    private Stage stage;

    public GameClientView(final Game game,
            final AbstractViewManager viewManager) {
        this.game = game;
        this.viewManager = viewManager;
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

        // stage.addListener(this.loginViewEventListener);

        // final var usernameField =
        // viewManager.getTextureManager().getTextField("Username");
        //
        // final var passwordField =
        // viewManager.getTextureManager().getTextField("Password");
        // passwordField.setPasswordMode(true);

        final Button loginButton = viewManager.getTextureManager().getTextButton("Log in");
        loginButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                    final int button) {
                // TODO: remove dependency on network module
                return true;
            }
        });

        final Button buttonBack = viewManager.getTextureManager().getTextButton("Back");
        buttonBack.addListener(new ClickListener() {
            public void clicked(final InputEvent event, final float x, final float y) {
                viewManager.getViewFactory().getPlayView().display();
            }
        });

        final Table table = viewManager.getTextureManager().getTable();
        table.row();
        table.add(loginButton);
        table.getCell(loginButton).spaceBottom(40);
        table.row();
        table.add(buttonBack);

        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
