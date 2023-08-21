package serverresponses;

public class ResponseStartGame implements  Response{
    public String status;
    public String message;

    public ResponseStartGame(final String status, final String message) {
        this.status = status;
        this.message = message;
    }
}
