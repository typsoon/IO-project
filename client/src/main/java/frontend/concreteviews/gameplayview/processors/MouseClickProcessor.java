package frontend.concreteviews.gameplayview.processors;

import com.badlogic.gdx.InputAdapter;

import frontend.concreteviews.gameplayview.IGameplayInfoProvider;

public class MouseClickProcessor extends InputAdapter {
    private final IGameplayInfoProvider gameplayInfoProvider;

    public MouseClickProcessor(IGameplayInfoProvider gameplayInfoProvider) {
        this.gameplayInfoProvider = gameplayInfoProvider;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
}
