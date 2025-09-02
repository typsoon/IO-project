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

import frontend.concreteviews.gameclientview.subscreens.ConfirmationPromptScreen;
import frontend.concreteviews.gameclientview.subscreens.GameClientSubviewsFactory;
import frontend.concreteviews.gameclientview.subscreens.WaitingRoomScreen;
import game.session.ISendableConsumer;
import game.utility.ISendable;
import gameclient.rooms.RoomRequest;
import network.messages.configurationstate.CreateRoomRequestResponse;
import network.messages.userstate.GameConfirmationRequestMessage;
import viewmodel.ITextureManager;

public class GameClientView extends ScreenAdapter implements ISendableConsumer, ScreenSwitchingUtils {
    @SuppressWarnings("unused")
    private final Game game;
    private final ITextureManager textureManager;
    private final EventListener gameClientViewEventListener;
    private final GameClientViewData gameClientViewData;
    private final GameClientSubviewsFactory gameClientSubscreensFactory;

    private Stack<Screen> activeSubscreens = new Stack<>();

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

        var mainTable = textureManager.getTable();
        mainTable.add(matchmakingActionsTable).expandX();
        mainTable.add(roomActionsTable).spaceBottom(40).expandX();
        mainTable.row();
        mainTable.add(exitButton).colspan(2);

        stage.addActor(mainTable);
    }

    private final Actor getRoomActionsTable() {
        final var roomNameField = textureManager.getTextField("Room name");

        final Button createRoomButton = textureManager.getTextButton("Create room");
        createRoomButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                    final int button) {
                createRoomButton.fire(new GameClientViewEvents.CreateRoomEvent(roomNameField.getText()));
                return true;
            }
        });

        final Button joinRoomButton = textureManager.getTextButton("Browse rooms");
        joinRoomButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                    final int button) {
                joinRoomButton.fire(new GameClientViewEvents.CreateRoomEvent(roomNameField.getText()));
                return true;
            }
        });

        // final Table roomActionsTable = textureManager.getTable();
        final Table answer = new Table();
        answer.add(roomNameField);
        answer.getCell(roomNameField).spaceBottom(40).width(300);
        answer.row();
        answer.add(createRoomButton);
        answer.getCell(createRoomButton).spaceBottom(40);
        answer.row();
        answer.add(joinRoomButton);
        answer.getCell(joinRoomButton).spaceBottom(40);

        return answer;
    }

    private final Actor getMatchmakingActionsTable() {
        final Button findGameButton = textureManager.getTextButton("Find game");
        findGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer,
                    final int button) {
                // playButton.fire(new
                // GameClientViewEvents.CreateRoomEvent(roomNameField.getText()));
                return true;
            }
        });

        // final Table roomActionsTable = textureManager.getTable();
        final Table answer = new Table();
        answer.add(findGameButton).expandY();
        answer.getCell(findGameButton).spaceBottom(40);

        return answer;
    }

    @Override
    public void changeSubscreen(final Screen newScreen) {
        // Logger.getGlobal().info(activeSubscreens.toString());
        // activeSubscreens.add(newScreen);
        // game.setScreen(newScreen);
        Gdx.app.postRunnable(() -> {
            Logger.getGlobal().info(activeSubscreens.toString());
            activeSubscreens.add(newScreen);
            var view = gameClientSubscreensFactory.wrapScreen(this, newScreen, game);
            view.display();
        });
    }

    @Override
    public void moveToPreviousSubscreen() {
        // Logger.getGlobal().info(activeSubscreens.toString());
        // activeSubscreens.pop();
        // game.setScreen(activeSubscreens.peek());
        Gdx.app.postRunnable(() -> {
            Logger.getGlobal().info(activeSubscreens.toString());
            activeSubscreens.pop();

            var view = gameClientSubscreensFactory.wrapScreen(this, activeSubscreens.peek(), game);
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
            case CreateRoomRequestResponse.Payload requestResponse -> {
                if (requestResponse.request() == RoomRequest.SUCCESSFUL) {
                    var waitingRoomScreen = new WaitingRoomScreen(this, gameClientViewData, requestResponse.roomName(),
                            textureManager, gameClientViewEventListener);
                    changeSubscreen(waitingRoomScreen);
                }
            }

            case GameConfirmationRequestMessage.Payload gameConfirmationRequest -> {
                Logger.getGlobal().info("Moving to confirmation screen");

                var screen = new ConfirmationPromptScreen(this, gameClientViewEventListener, textureManager);
                changeSubscreen(screen);
            }

            default -> {
                throw new IllegalStateException("Unexpected sendable");
            }
        }
    }
}
