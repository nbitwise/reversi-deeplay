package clientresponse;

public class AuthorizationResponse implements Response {
    public static String message = "success";
    public String status;
    public AuthorizationResponse(String status) {
        this.status = status;
    }
}
