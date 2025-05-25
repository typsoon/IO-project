package viewmodel;


public class BasicViewManager implements AbstractViewManager {
    private final BasicViewFactory viewFactory;
    private final BasicTextureManager textureManager;

    //should only be called by its injector
    BasicViewManager(
            BasicViewFactory basicViewFactory,
            BasicTextureManager basicTextureManager
    ) {
        this.viewFactory = basicViewFactory;
        basicViewFactory.setViewManager(this);
        this.textureManager = basicTextureManager;
    }

    @Override
    public void start() {
        viewFactory.getMainMenuView().display();
    }

    @Override
    public AbstractViewFactory getViewFactory() {
        return viewFactory;
    }

    @Override
    public AbstractTextureManager getTextureManager() {
        return textureManager;
    }

}
