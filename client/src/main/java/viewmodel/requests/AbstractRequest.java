package viewmodel.requests;

public abstract class AbstractRequest<T> {
  private final T payload;

  protected AbstractRequest(T payload) {
    this.payload = payload;
  }

  public final T getPayload() {
    return payload;
  }
}
