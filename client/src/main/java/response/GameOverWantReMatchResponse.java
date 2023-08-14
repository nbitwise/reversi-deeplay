package response;

public class GameOverWantReMatchResponse implements Response {
    public String status;
    public String message;

    public GameOverWantReMatchResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
