package viewmodel;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public interface AbstractTextureManager {
    Button getTextButton(String name);
    Table getTable();
    TextField getTextField(String name);
    Label getHeading(String name);
}
