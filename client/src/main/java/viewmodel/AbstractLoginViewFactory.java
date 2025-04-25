package viewmodel;

public interface AbstractLoginViewFactory {
  AbstractView getLoginView(RequestHandler requestHandler);
}
