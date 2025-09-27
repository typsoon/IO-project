package frontend.concreteviews.gameclientview.subscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import frontend.concreteviews.gameclientview.GameClientViewEvents.*;

import frontend.concreteviews.gameclientview.ScreenSwitchingUtils;
import frontend.assetsloading.*;

import static network.messages.userstate.GameConfirmation.Confirmation.*;

public class ConfirmationPromptScreen extends ScreenAdapter {
    private final ScreenSwitchingUtils screenSwitchingUtils;
    private final EventListener eventListener;
    private final ITextureManager textureManager;

    private final Stage stage;
    private final Table rootTable;

    public ConfirmationPromptScreen(ScreenSwitchingUtils screenSwitchingUtils,
            EventListener eventListener,
            ITextureManager textureManager) {
        this.screenSwitchingUtils = screenSwitchingUtils;
        this.eventListener = eventListener;
        this.textureManager = textureManager;

        this.stage = new Stage(new ScreenViewport());
        this.rootTable = new Table();
        rootTable.setFillParent(true);

        stage.addActor(rootTable);
        buildUI();
    }

    private void buildUI() {
        rootTable.clear();

        // Pytanie
        Label questionLabel = textureManager.getHeading("Are you sure?");
        questionLabel.setColor(Color.WHITE);
        rootTable.add(questionLabel).colspan(2).pad(15);
        rootTable.row();

        // Przycisk Yes
        var yesButton = textureManager.getTextButton("Yes");
        yesButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                yesButton.fire(new ConfirmGameEvent(CONFIRMED));
                screenSwitchingUtils.moveToPreviousSubscreen();
                return true;
            }
        });

        // Przycisk No
        var noButton = textureManager.getTextButton("No");
        noButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                noButton.fire(new ConfirmGameEvent(CANCELLED));
                screenSwitchingUtils.moveToPreviousSubscreen();
                return true;
            }
        });

        // Układ obok siebie
        rootTable.add(yesButton).pad(10);
        rootTable.add(noButton).pad(10);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.addListener(eventListener);
    }

    @Override
    public void render(float delta) {
        // półprzezroczyste tło, żeby wyglądało jak popup
        Gdx.gl.glClearColor(0, 0, 0, 0.7f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
