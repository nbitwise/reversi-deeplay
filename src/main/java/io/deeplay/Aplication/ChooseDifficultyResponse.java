package io.deeplay.Aplication;

public class ChooseDifficultyResponse implements Response {
    String status;
    String message;

    ChooseDifficultyResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
