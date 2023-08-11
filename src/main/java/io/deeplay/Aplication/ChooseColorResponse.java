package io.deeplay.Aplication;

public class ChooseColorResponse implements Response {
    String status;
    String message;

    ChooseColorResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
