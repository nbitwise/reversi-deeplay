package response;

public class GameOverDoYouWantReMatchResponse implements Response {
    public String status;
    public String message;

    GameOverDoYouWantReMatchResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
