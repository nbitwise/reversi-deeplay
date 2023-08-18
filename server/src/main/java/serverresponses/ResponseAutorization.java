package serverresponses;


public class ResponseAutorization implements Response {
    public String status;
    public String message;

    public ResponseAutorization(final String status, final String message) {
        this.status = status;
        this.message = message;
    }
}