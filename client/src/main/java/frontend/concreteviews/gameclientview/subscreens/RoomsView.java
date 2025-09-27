package frontend.concreteviews.gameclientview.subscreens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import frontend.concreteviews.gameclientview.GameClientViewData;
import frontend.concreteviews.gameclientview.ScreenSwitchingUtils;
import frontend.assetsloading.*;

class RoomsView extends ScreenAdapter {
    public RoomsView(ScreenSwitchingUtils screenSwitchingUtils, GameClientViewData gameClientViewData,
            ITextureManager textureManager, EventListener eventListener) {
        this.screenSwitchingUtils = screenSwitchingUtils;
        this.gameClientViewData = gameClientViewData;
        this.textureManager = textureManager;
        this.eventListener = eventListener;
    }

    private final ScreenSwitchingUtils screenSwitchingUtils;
    private final GameClientViewData gameClientViewData;
    private final ITextureManager textureManager;
    private final EventListener eventListener;
}
