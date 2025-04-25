package viewmodel;

import viewmodel.requests.AbstractRequest;

public interface RequestHandler {
  void handleRequest(AbstractRequest<?> request);
}
