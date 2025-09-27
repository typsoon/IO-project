package frontend.assetsloading.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import frontend.assetsloading.ITextureManager;

public class BasicTextureManager implements ITextureManager {
    private final TextureAtlas atlas;
    private final Skin skin;
    private final FreeTypeFontGenerator generator;

    private final TextButton.TextButtonStyle textButtonStyle;
    private final TextField.TextFieldStyle textFieldStyle;
    private final Label.LabelStyle labelStyle;
    private final ProgressBar.ProgressBarStyle progressBarStyle;

    public BasicTextureManager() {
        // TODO view: use config instead of hardcoded names
        this.atlas = new TextureAtlas(Gdx.files.internal("BasicView.atlas"));
        this.skin = new Skin(atlas);
        this.generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Harrington_SHAREWARE.ttf"));

        this.textButtonStyle = new TextButton.TextButtonStyle();
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        BitmapFont font = generator.generateFont(parameter);
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("buttonBackground");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;

        this.textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.messageFont = font;
        textFieldStyle.font = font;
        textFieldStyle.fontColor = font.getColor();
        textFieldStyle.background = skin.getDrawable("buttonBackground");

        this.labelStyle = new Label.LabelStyle(font, Color.WHITE);

        Pixmap bgPixmap = new Pixmap(200, 20, Pixmap.Format.RGBA8888);
        bgPixmap.setColor(Color.DARK_GRAY); // kolor tła
        bgPixmap.fill();
        Texture bgTexture = new Texture(bgPixmap);
        bgPixmap.dispose();
        Drawable bgDrawable = new TextureRegionDrawable(new TextureRegion(bgTexture));

        Pixmap fillPixmap = new Pixmap(200, 20, Pixmap.Format.RGBA8888);
        fillPixmap.setColor(Color.RED); // kolor wypełnienia
        fillPixmap.fill();
        Texture fillTexture = new Texture(fillPixmap);
        fillPixmap.dispose();
        Drawable fillDrawable = new TextureRegionDrawable(new TextureRegion(fillTexture));

        this.progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = bgDrawable;
        progressBarStyle.knobBefore = null;
        progressBarStyle.knobBefore = fillDrawable;
    }

    public TextButton getTextButton(String name) {
        return new TextButton(name, textButtonStyle);
    }

    public Table getTable() {
        Table table = new Table(skin);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return table;
    }

    public TextField getTextField(String name) {
        TextField textField = new TextField("", textFieldStyle);
        textField.setAlignment(Align.center);
        textField.setMessageText(name);
        return textField;
    }

    public Label getHeading(String name) {
        Label heading = new Label(name, labelStyle);
        heading.setFontScale(1.5f);
        heading.setAlignment(10);
        return heading;
    }

    @Override
    public ProgressBar getProgressBar(float min, float max, float stepSize, boolean vertical) {
        ProgressBar progressBar = new ProgressBar(min, max, stepSize, vertical, progressBarStyle);
        progressBar.setAnimateDuration(0.25f);
        return progressBar;
    }
}
