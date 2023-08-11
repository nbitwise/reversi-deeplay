package io.deeplay.Aplication;

public class ConnectForViewResponse implements Response {
    String status;
    String message;

    ConnectForViewResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
