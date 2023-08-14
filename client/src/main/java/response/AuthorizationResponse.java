package response;

public class AuthorizationResponse implements Response {
    public String status;

    public AuthorizationResponse(String status) {
        this.status = status;
    }
}
