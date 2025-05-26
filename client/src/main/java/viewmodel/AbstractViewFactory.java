package viewmodel;

public interface AbstractViewFactory {
    AbstractView getMainMenuView();
    AbstractView getSettingsView();
    AbstractView getPlayView();
//    AbstractView getLoginView();
}
