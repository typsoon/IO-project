package utils;

import java.util.Objects;

public class SingleWriteContainer<U> {
    private U contents = null;

    public void setContents(U socketSender) {
        if (this.contents != null) {
            throw new IllegalStateException("Element already set to %s".formatted(this.contents));
        }
        this.contents = socketSender;
    }

    public U getContents() {
        return Objects.requireNonNull(contents);
    }
}
