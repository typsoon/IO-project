package frontend.concreteviews.basicviews;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import viewmodel.AbstractView;
import viewmodel.AbstractViewManager;
import viewmodel.OldViewManagerInjector;

public class PlayView extends ScreenAdapter implements AbstractView {
    private final Game game;
    private final AbstractViewManager viewManager;

    private Stage stage;


    public PlayView(final Game game, final AbstractViewManager viewManager) {
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
        //TODO hardcoded: remove hardcoded strings, use config instead
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        final Button buttonConnect = viewManager.getTextureManager().getTextButton("Connect");
        buttonConnect.addListener(new ClickListener() {
            public void clicked(final InputEvent event, final float x, final float y) {
                //TODO: change to a proper use of viewManager
                final var oldViewManager = new OldViewManagerInjector(game).getViewManager();
                oldViewManager.start(viewManager);
            }
        });

        final Button buttonBack = viewManager.getTextureManager().getTextButton("Back");
        buttonBack.addListener(new ClickListener() {
            public void clicked(final InputEvent event, final float x, final float y) {
                viewManager.getViewFactory().getMainMenuView().display();
            }
        });

        final Table table = viewManager.getTextureManager().getTable();
        table.add(buttonConnect);
        table.getCell(buttonConnect).spaceBottom(40);
        table.row();
        table.add(buttonBack);

        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
