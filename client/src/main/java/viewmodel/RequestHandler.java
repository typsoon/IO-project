package viewmodel;

import frontend.requests.AbstractRequest;

public interface RequestHandler {
  void handleRequest(AbstractRequest<?> request);
}
