package io.deeplay.Aplication;

public class GameOverDoYouWantReMatchResponse implements Response {
    String status;
    String message;

    GameOverDoYouWantReMatchResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
