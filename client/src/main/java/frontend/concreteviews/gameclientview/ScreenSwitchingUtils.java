package frontend.concreteviews.gameclientview;

import com.badlogic.gdx.Screen;

public interface ScreenSwitchingUtils {
    void changeSubscreen(Screen newScreen);

    void moveToPreviousSubscreen();
}
