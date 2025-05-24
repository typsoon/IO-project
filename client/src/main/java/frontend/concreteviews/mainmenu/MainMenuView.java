package frontend.concreteviews.mainmenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import viewmodel.AbstractView;
import viewmodel.ViewManagerInjector;

public class MainMenuView extends ScreenAdapter implements AbstractView {
    private final Game game;

    private TextureAtlas atlas;
    private Skin skin;
    private Stage stage;


    MainMenuView(final Game game) {
        this.game = game;
    }


    @Override
    public void display() {
        game.setScreen(this);
    }

    @Override
    public void dispose() {
        atlas.dispose();
        skin.dispose();
        stage.dispose();
    }

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(0, 0, 0, 1, true);
        super.render(delta);
        stage.draw();
    }

    @Override
    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void show() {
        //TODO: understand and refractor (code taken from LoginView)
        var generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Harrington_SHAREWARE.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;

        stage = new Stage();
        atlas = new TextureAtlas(Gdx.files.internal("BasicView.atlas"));
        skin = new Skin(atlas);
        Gdx.input.setInputProcessor(stage);

        final TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = generator.generateFont(parameter);
        textButtonStyle.up = skin.getDrawable("buttonBackground");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;


        final Button buttonConnect = new TextButton("Connect", textButtonStyle);
        buttonConnect.addListener(new ClickListener() {
            public void clicked(final InputEvent event, final float x, final float y) {
                final var viewManager = new ViewManagerInjector(game).getViewManager();
                viewManager.start();
            }
        });

        final Button buttonExit = new TextButton("Exit", textButtonStyle);
        buttonExit.addListener(new ClickListener() {
            public void clicked(final InputEvent event, final float x, final float y) {
                Gdx.app.exit();
            }
        });



        final Table table = new Table(skin);
        table.add(buttonConnect);
        table.getCell(buttonConnect).spaceBottom(40);
        table.row();
        table.add(buttonExit);

        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(table);
    }


}
//        final var viewManager = new ViewManagerInjector(this).getViewManager();
//        viewManager.start();