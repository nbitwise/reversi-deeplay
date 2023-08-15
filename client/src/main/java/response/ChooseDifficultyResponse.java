package response;

public class ChooseDifficultyResponse implements Response {
    public String status;
    public String message;

    public ChooseDifficultyResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
