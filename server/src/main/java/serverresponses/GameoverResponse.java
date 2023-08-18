package serverresponses;

public class GameoverResponse implements Response{
    private String status;
    private String message;
    public GameoverResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
    public String getStatus() {

        return status;
    }
}
