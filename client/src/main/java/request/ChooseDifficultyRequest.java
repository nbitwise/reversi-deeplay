package request;

public class ChooseDifficultyRequest implements Request {
    public final String command = "chooseDifficulty";
    public String difficulty;

    public ChooseDifficultyRequest(String difficulty) {
        this.difficulty = difficulty;
    }
}
