package frontend.assetsloading;

import com.badlogic.gdx.scenes.scene2d.ui.*;

public interface ITextureManager {
    TextButton getTextButton(String name);

    Table getTable();

    TextField getTextField(String name);

    Label getHeading(String name);

    ProgressBar getProgressBar(float min, float max, float stepSize, boolean vertical);
}
