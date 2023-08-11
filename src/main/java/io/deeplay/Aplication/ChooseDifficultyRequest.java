package io.deeplay.Aplication;

public class ChooseDifficultyRequest implements Request {
    String command = "chooseDifficulty";
    String difficulty;

    ChooseDifficultyRequest(String difficulty) {
        this.difficulty = difficulty;
    }
}
