package response;

public class AuthorizationResponse implements Response {
    public String status;

    AuthorizationResponse(String status) {
        this.status = status;
    }
}
