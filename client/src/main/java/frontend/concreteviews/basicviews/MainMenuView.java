package frontend.concreteviews.basicviews;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import viewmodel.IView;
import viewmodel.IViewManager;
import frontend.assetsloading.*;

public class MainMenuView extends ScreenAdapter implements IView {
    private final Game game;
    private final IViewManager viewManager;

    private Stage stage;

    public MainMenuView(final Game game, final IViewManager viewManager) {
        this.game = game;
        this.viewManager = viewManager;
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
        // TODO: hardcoded - remove hardcoded strings, use config instead
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        final Label heading = viewManager.getTextureManager().getHeading("IO game");

        final Button buttonPlay = viewManager.getTextureManager().getTextButton("Play");
        buttonPlay.addListener(new ClickListener() {
            public void clicked(final InputEvent event, final float x, final float y) {
                viewManager.moveToPlayView();
            }
        });

        final Button buttonSettings = viewManager.getTextureManager().getTextButton("Settings");
        buttonSettings.addListener(new ClickListener() {
            public void clicked(final InputEvent event, final float x, final float y) {
                viewManager.moveToSettings();
            }
        });

        final Button buttonExit = viewManager.getTextureManager().getTextButton("Exit");
        buttonExit.addListener(new ClickListener() {
            public void clicked(final InputEvent event, final float x, final float y) {
                Gdx.app.exit();
            }
        });

        final Table table = viewManager.getTextureManager().getTable();
        table.add(heading);
        table.getCell(heading).spaceBottom(80);
        table.row();
        table.add(buttonPlay);
        table.getCell(buttonPlay).spaceBottom(40);
        table.row();
        table.add(buttonSettings);
        table.getCell(buttonSettings).spaceBottom(40);
        table.row();
        table.add(buttonExit);

        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
