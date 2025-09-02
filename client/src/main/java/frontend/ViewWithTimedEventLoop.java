package frontend;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import utility.ICycleTimedPerformer;
import viewmodel.IView;

// NOTE: this class has 2 responsiblities but one (providing display()) is
// really simple so I decided not to split it

/**
 * This class delegates all method calls to contained {@link Screen}, the only
 * difference is {@code render} method - it performs
 * a cycle of a contained cycle performer before calling render on a contained
 * {@link Screen}
 */
public class ViewWithTimedEventLoop implements Screen, IView {
    // public class ViewWithEventLoop extends ScreenAdapter implements AbstractView
    // {
    private final Game game;
    private final ICycleTimedPerformer cyclePerformer;
    private final Screen delegate;

    public ViewWithTimedEventLoop(ICycleTimedPerformer cyclePerformer, Screen delegate, Game game) {
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
        // game.getScreen().pause();
        delegate.hide();
    }

    public void dispose() {
        delegate.dispose();
    }

    @Override
    public void render(float delta) {
        cyclePerformer.performCycle(delta);
        delegate.render(delta);
    }

    @Override
    public void display() {
        Gdx.app.postRunnable(() -> game.setScreen(this));
    }
}
