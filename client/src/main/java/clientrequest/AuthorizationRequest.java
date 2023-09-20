package clientrequest;

import org.jetbrains.annotations.NotNull;

public class AuthorizationRequest implements Request {
    public final String command = "AUTHORIZATION";

    public final String nickname;
    public AuthorizationRequest( @NotNull final String nickname) {
        this.nickname = nickname;
    }
}
