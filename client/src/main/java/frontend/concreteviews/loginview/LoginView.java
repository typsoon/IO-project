package frontend.concreteviews.loginview;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import network.utils.Credentials;

import com.badlogic.gdx.scenes.scene2d.EventListener;

import viewmodel.IViewManager;
import frontend.assetsloading.*;
import viewmodel.IView;

public class LoginView extends ScreenAdapter implements IView {
    private final Game game;
    private final EventListener loginViewEventListener;
    private final ITextureManager textureManager;
    private final IViewManager viewManager;

    private Stage stage;

    // TODO view: refactor to make similar to MainMenuView
    public LoginView(final Game game, final EventListener loginViewEventListener,
            ITextureManager textureManager, IViewManager viewManager) {
        this.game = game;
        this.textureManager = textureManager;
        this.loginViewEventListener = loginViewEventListener;
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

        stage.addListener(this.loginViewEventListener);

        final var usernameField = textureManager.getTextField("Username");

        final var passwordField = textureManager.getTextField("Password");
        passwordField.setPasswordMode(true);

        final Button loginButton = textureManager.getTextButton("Log in");
        loginButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                    final int button) {
                // TODO: remove dependency on network module
                final var credentials = new Credentials(usernameField.getText(), passwordField.getText());
                loginButton.fire(new CredentialsTypedEvent(credentials));
                return true;
            }
        });

        final Button buttonBack = textureManager.getTextButton("Back");
        buttonBack.addListener(new ClickListener() {
            public void clicked(final InputEvent event, final float x, final float y) {
                viewManager.moveToPlayView();
            }
        });

        final Table table = textureManager.getTable();
        table.add(usernameField);
        table.getCell(usernameField).spaceBottom(40).width(300);
        table.row();
        table.add(passwordField);
        table.getCell(passwordField).spaceBottom(40).width(300);
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
