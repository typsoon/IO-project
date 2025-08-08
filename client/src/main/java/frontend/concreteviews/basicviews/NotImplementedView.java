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

//placeholder for views that don't exist yet, goal is to remove all usages
public class NotImplementedView extends ScreenAdapter implements IView {
    private final Game game;
    private final IViewManager viewManager;

    private Stage stage;

    public NotImplementedView(final Game game, final IViewManager viewManager) {
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
        // TODO hardcoded: remove hardcoded strings, use config instead
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        final Label heading = viewManager.getTextureManager().getHeading("Not Implemented");

        final Button buttonBack = viewManager.getTextureManager().getTextButton("Back To Menu");
        buttonBack.addListener(new ClickListener() {
            public void clicked(final InputEvent event, final float x, final float y) {
                viewManager.start();
            }
        });

        final Table table = viewManager.getTextureManager().getTable();
        table.add(heading);
        table.getCell(heading).spaceBottom(80);
        table.row();
        table.add(buttonBack);

        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
