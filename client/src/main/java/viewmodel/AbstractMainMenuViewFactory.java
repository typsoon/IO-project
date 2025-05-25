package viewmodel;

public interface AbstractMainMenuViewFactory {
    void setViewManager(AbstractDefaultViewManager viewManger);
    AbstractView getMainMenuView();
}
