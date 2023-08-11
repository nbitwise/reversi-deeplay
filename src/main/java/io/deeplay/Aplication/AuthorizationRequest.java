package io.deeplay.Aplication;

public class AuthorizationRequest implements Request {
    String command = "authorization";
    String nickname;

    AuthorizationRequest(String nickname) {
        this.nickname = nickname;
    }
}
