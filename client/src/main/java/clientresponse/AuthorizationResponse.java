package clientresponse;

import org.jetbrains.annotations.NotNull;

public class AuthorizationResponse implements Response {
    public final String command = "AUTHORIZATION";
    public final String message;
    public final String status;

    public AuthorizationResponse(@NotNull final String message, @NotNull final String status) {
        this.message = message;
        this.status = status;
    }
}
