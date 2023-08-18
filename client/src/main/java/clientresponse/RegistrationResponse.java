package clientresponse;

public class RegistrationResponse implements Response {
    public String status;
    public static String message = "HUINY";

    public RegistrationResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
