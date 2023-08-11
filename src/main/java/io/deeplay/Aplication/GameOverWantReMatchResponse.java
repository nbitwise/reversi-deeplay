package io.deeplay.Aplication;

public class GameOverWantReMatchResponse implements Response {
    String status;
    String message;

    GameOverWantReMatchResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
