package frontend;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import utility.CyclePerformer;
import viewmodel.AbstractView;

//NOTE: this class has 2 responsiblities but one (providing display()) is really simple so I decided not to split it
public class ViewWithEventLoop implements Screen, AbstractView {
    // public class ViewWithEventLoop extends ScreenAdapter implements AbstractView
    // {
    private final Game game;
    private final CyclePerformer cyclePerformer;
    private final Screen delegate;

    public ViewWithEventLoop(CyclePerformer cyclePerformer, Screen delegate, Game game) {
        this.game = game;
        this.delegate = delegate;
        this.cyclePerformer = cyclePerformer;
    }

    public void show() {
        delegate.show();
    }

    public void resize(int width, int height) {
        delegate.resize(width, height);
    }

    public void pause() {
        delegate.pause();
    }

    public void resume() {
        delegate.resume();
    }

    public void hide() {
        delegate.hide();
    }

    public void dispose() {
        delegate.dispose();
    }

    @Override
    public void render(float delta) {
        cyclePerformer.performCycle();
        delegate.render(delta);
    }

    @Override
    public void display() {
        game.setScreen(this);
    }
}
