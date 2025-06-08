package learn.mastery.domain;

import java.util.ArrayList;
import java.util.List;

public class Result<T> {
    private T payload;
    private boolean success = true;
    private final List<String> messages = new ArrayList<>();

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public boolean isSuccess() {
        return success;
    }

    public void addErrorMessage(String message) {
        success = false;
        messages.add(message);
    }

    public List<String> getMessages() {
        return messages;
    }
}
