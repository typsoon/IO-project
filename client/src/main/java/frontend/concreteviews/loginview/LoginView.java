package frontend.concreteviews.loginview;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.scenes.scene2d.EventListener;

import frontend.concreteviews.mainmenu.MainMenuViewFactory;
import network.Credentials;
import viewmodel.AbstractView;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class LoginView extends ScreenAdapter implements AbstractView {
    private final Game game;
    private final EventListener loginViewEventListener;

    private TextureAtlas atlas;
    private Stage stage;
    private Skin skin;
    private BitmapFont font;

    private FreeTypeFontGenerator generator;

    private OrthographicCamera gameCamera;

    LoginView(final Game game, final EventListener loginViewEventListener) {
        this.game = game;
        this.loginViewEventListener = loginViewEventListener;
    }

    @Override
    public void show() {
        final String loginScreenHeading = "IO Game";
        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, 800, 480);

        stage = new Stage();
        stage.addListener(this.loginViewEventListener);

        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas(Gdx.files.internal("LoginView.atlas"));
        skin = new Skin(atlas);
        final Table table = new Table(skin);

        // TODO: Move strings to config
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Harrington_SHAREWARE.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        // parameter.color.set(sunflower);

        font = generator.generateFont(parameter);

        final Texture texture = font.getRegion().getTexture();
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        final TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("buttonBackground");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;

        final TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.messageFont = font;
        textFieldStyle.font = font;
        textFieldStyle.fontColor = font.getColor();
        textFieldStyle.background = skin.getDrawable("buttonBackground");

        final var usernameField = new TextField("", textFieldStyle);
        usernameField.setAlignment(Align.center);
        usernameField.setMessageText("Username");

        final var passwordField = new TextField("", textFieldStyle);
        passwordField.setAlignment(Align.center);
        passwordField.setMessageText("Password");
        passwordField.setPasswordMode(true);

        final Button loginButton = new TextButton("Log in", textButtonStyle);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(final InputEvent event, final int keycode) {
                if (keycode == Input.Keys.ENTER) {

                }

                return super.keyDown(event, keycode);
            }
        });

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

        final Button buttonBack = new TextButton("Back", textButtonStyle);
        buttonBack.addListener(new ClickListener() {
            public void clicked(final InputEvent event, final float x, final float y) {
                final var mainMenuView = new MainMenuViewFactory(game).getMainMenuView();
                mainMenuView.display();
            }
        });

        final Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.WHITE);
        final Label heading = new Label(loginScreenHeading, headingStyle);
        heading.setFontScale(1.5f);
        heading.setAlignment(10);

        table.add(heading);
        table.getCell(heading).spaceBottom(80);
        table.row();
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

        // table.debug();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.addActor(table);
    }

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        // ScreenUtils.clear(255, 255, 255, 0);

        stage.act(delta);

        stage.draw();
    }

    @Override
    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void dispose() {
        atlas.dispose();
        skin.dispose();
        stage.dispose();
        font.dispose();
        generator.dispose();
    }

    @Override
    public void display() {
        game.setScreen(this);
    }
}
