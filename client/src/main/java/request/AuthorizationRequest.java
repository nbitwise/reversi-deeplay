package request;

public class AuthorizationRequest implements Request {
    public final String command = "authorization";
    public String nickname;

    public AuthorizationRequest(String nickname) {
        this.nickname = nickname;
    }
}
