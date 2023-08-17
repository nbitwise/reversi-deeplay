package serverresponses;

public class ResponseDisconnection implements Response {
    public String status;
    public String message;

    public ResponseDisconnection(final String status, final String message) {
        this.status = status;
        this.message = message;
    }
}
