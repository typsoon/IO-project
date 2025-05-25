package viewmodel;

            //don't use outside package concerning frontend. use AbstractViewManager instead
public interface AbstractViewManager extends AbstractGeneralViewManager {
    void start();
    AbstractViewFactory getViewFactory();
    AbstractTextureManager getTextureManager();
}
