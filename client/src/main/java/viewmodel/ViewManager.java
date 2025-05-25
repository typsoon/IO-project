package viewmodel;


public class ViewManager implements AbstractDefaultViewManager {
    private final AbstractMainMenuViewFactory mainMenuViewFactory;

    //should only be called by its injector
    public ViewManager(AbstractMainMenuViewFactory mainMenuViewFactory) {
        this.mainMenuViewFactory = mainMenuViewFactory;
        mainMenuViewFactory.setViewManager(this);
    }

    public void start() {
        mainMenuViewFactory.getMainMenuView().display();
    }

    //all methods "get*View" should be added to AbstractDefaultViewManager interface
}
