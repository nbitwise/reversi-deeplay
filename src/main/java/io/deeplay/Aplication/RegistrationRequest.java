package io.deeplay.Aplication;

public class RegistrationRequest implements Request {
    String command = "registration";
    String nickname;

    RegistrationRequest(String nickname) {
        this.nickname = nickname;
    }
}
