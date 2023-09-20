package clientresponse;

import org.jetbrains.annotations.NotNull;

public class RegistrationResponse implements Response {
    public final String command = "REGISTRATION";
    public final String status;
    public final String message;

    public RegistrationResponse(@NotNull final String status, @NotNull final String message) {
        this.status = status;
        this.message = message;
    }
}
