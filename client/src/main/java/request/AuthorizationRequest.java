package request;

public class AuthorizationRequest implements Request {
    public final String command = "authorization";
    public String nickname;

    AuthorizationRequest(String nickname) {
        this.nickname = nickname;
    }
}
