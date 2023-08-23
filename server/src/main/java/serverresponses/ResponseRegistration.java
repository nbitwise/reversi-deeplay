package serverresponses;


public class ResponseRegistration implements Response {
    public final String command = "REGISTRATION";
    public String status;
    public String message;

    public ResponseRegistration(final String status, final String message) {
        this.status = status;
        this.message = message;
    }
}
