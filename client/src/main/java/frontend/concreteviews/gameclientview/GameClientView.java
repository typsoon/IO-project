// java
package frontend.concreteviews.gameclientview;

import java.util.Stack;
import java.util.logging.Logger;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import frontend.assetsloading.ITextureManager;
import frontend.concreteviews.gameclientview.GameClientViewEvents.FindGameEvent;
import frontend.concreteviews.gameclientview.subscreens.ConfirmationPromptScreen;
import frontend.concreteviews.gameclientview.subscreens.GameClientSubviewsFactory;
import frontend.concreteviews.gameclientview.subscreens.WaitingRoomScreen;
import game.session.ISendableConsumer;
import gameclient.rooms.RequestResult;
import gameclient.rooms.RoomRequestResult;
import gameclient.rooms.RoomRequestType;
import matchmaking.MatchmakingParameters;
import network.messages.userstate.GameConfirmationRequestMessage;
import utils.ISendable;

public class GameClientView extends ScreenAdapter implements ISendableConsumer, ScreenSwitchingUtils {
    private static final float ACTION_BUTTON_WIDTH = 320f;
    private static final float ACTION_BUTTON_HEIGHT = 56f;

    @SuppressWarnings("unused")
    private final Game game;
    private final ITextureManager textureManager;
    private final EventListener gameClientViewEventListener;
    private final GameClientViewData gameClientViewData;
    private final GameClientSubviewsFactory gameClientSubscreensFactory;

    private final Stack<Screen> activeSubscreens = new Stack<>();

    private Stage stage;

    public GameClientView(final Game game, ITextureManager textureManager,
            GameClientViewEventListener gameClientViewEventListener, GameClientViewData gameClientViewData,
            GameClientSubviewsFactory gameClientSubscreensFactory) {
        this.game = game;
        this.textureManager = textureManager;
        this.gameClientViewEventListener = gameClientViewEventListener;
        this.gameClientViewData = gameClientViewData;
        this.gameClientSubscreensFactory = gameClientSubscreensFactory;
    }

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void show() {
        activeSubscreens.clear();
        activeSubscreens.add(this);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        stage.addListener(this.gameClientViewEventListener);

        final Button exitButton = textureManager.getTextButton("Exit");
        exitButton.addListener(new ClickListener() {
            public void clicked(final InputEvent event, final float x, final float y) {
                Gdx.app.exit();
            }
        });

        var roomActionsTable = getRoomActionsTable();
        var matchmakingActionsTable = getMatchmakingActionsTable();

        // Główny layout: górna przestrzeń na grafikę + panele niżej
        var mainTable = textureManager.getTable();
        mainTable.setFillParent(true);
        mainTable.defaults().pad(10).uniformX().fillX().center();

        // Górny obszar na grafikę (placeholder) — zajmuje wolną wysokość
        var graphicArea = textureManager.getTable();
        var placeholder = textureManager.getHeading("nice photo here");
        graphicArea.add(placeholder).expand().center();
        mainTable.add(graphicArea).colspan(2).expandY().fillX();
        mainTable.row();

        // Dwie kolumny z przyciskami — symetryczne i niżej na ekranie
        mainTable.add(matchmakingActionsTable).expandX().fillX().left().padBottom(20);
        mainTable.add(roomActionsTable).expandX().fillX().right().padBottom(20);
        mainTable.row();

        mainTable.add(exitButton).colspan(2).padTop(10);

        stage.addActor(mainTable);
    }

    private final Actor getRoomActionsTable() {
        final Button roomsScreenButton = textureManager.getTextButton("Rooms...");
        roomsScreenButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                    final int button) {
                changeSubscreen(new frontend.concreteviews.gameclientview.subscreens.RoomsView(
                        GameClientView.this, gameClientViewData, textureManager, gameClientViewEventListener));
                return true;
            }
        });

        final Table panel = textureManager.getTable();
        panel.add(roomsScreenButton)
                .width(ACTION_BUTTON_WIDTH)
                .height(ACTION_BUTTON_HEIGHT)
                .padBottom(40)
                .center();
        return panel;
    }

    private final Actor getMatchmakingActionsTable() {
        final Button findGameButton = textureManager.getTextButton("Find game");
        findGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                    final int button) {
                findGameButton.fire(new FindGameEvent(new MatchmakingParameters(2)));
                return true;
            }
        });

        final Table panel = textureManager.getTable();
        panel.add(findGameButton)
                .width(ACTION_BUTTON_WIDTH)
                .height(ACTION_BUTTON_HEIGHT)
                .padBottom(40)
                .center();
        return panel;
    }

    @Override
    public void changeSubscreen(final Screen newScreen) {
        Gdx.app.postRunnable(() -> {
            activeSubscreens.add(newScreen);
            final var view = gameClientSubscreensFactory.wrapScreen(this, newScreen, game);
            view.display();
        });
    }

    @Override
    public void moveToPreviousSubscreen() {
        Gdx.app.postRunnable(() -> {
            activeSubscreens.pop();
            final var view = gameClientSubscreensFactory.wrapScreen(this, activeSubscreens.peek(), game);
            view.display();
        });
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void processSendable(ISendable sendable) {
        switch (sendable) {
            case RoomRequestResult requestResponse -> {
                switch (requestResponse.roomRequestType()) {
                    case RoomRequestType.CREATE, RoomRequestType.JOIN -> {
                        if (requestResponse.result() == RequestResult.SUCCESSFUL) {
                            var waitingRoomScreen = new WaitingRoomScreen(this, gameClientViewData,
                                    requestResponse.roomName(),
                                    textureManager, gameClientViewEventListener);
                            changeSubscreen(waitingRoomScreen);
                        }
                    }
                }
            }
            case GameConfirmationRequestMessage.Payload gameConfirmationRequest -> {
                Logger.getGlobal().info("Moving to confirmation screen");
                final var screen = new ConfirmationPromptScreen(this, gameClientViewEventListener, textureManager);
                changeSubscreen(screen);
            }
            default -> {
                throw new IllegalStateException("Unexpected sendable");
            }
        }
    }
}
