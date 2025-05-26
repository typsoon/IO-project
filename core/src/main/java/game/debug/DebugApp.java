package game.debug;

import com.badlogic.gdx.Game;

public class DebugApp extends Game {
    @Override
    public void create() {
        setScreen(new DebugScreen());
    }
}