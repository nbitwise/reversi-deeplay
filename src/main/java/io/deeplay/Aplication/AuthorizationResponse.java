package io.deeplay.Aplication;

public class AuthorizationResponse implements Response {
    String status;

    AuthorizationResponse(String status) {
        this.status = status;
    }
}
