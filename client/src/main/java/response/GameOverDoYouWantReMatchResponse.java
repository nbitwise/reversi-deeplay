package response;

public class GameOverDoYouWantReMatchResponse implements Response {
    public String status;
    public String message;

    public GameOverDoYouWantReMatchResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
