package frontend.gamestate;

public interface IDisplayableGameState extends IReadOnlyDisplayableGameState {
    void AddDrawable(DrawableInfo drawable);
    void RemoveDrawable(DrawableInfo drawableInfo);
}
