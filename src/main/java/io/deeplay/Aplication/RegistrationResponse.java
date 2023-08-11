package io.deeplay.Aplication;

public class RegistrationResponse implements Response {
    String status;
    String message;

    RegistrationResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
