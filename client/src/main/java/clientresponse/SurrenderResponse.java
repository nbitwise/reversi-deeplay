package clientresponse;

import org.jetbrains.annotations.NotNull;

public class SurrenderResponse implements Response {
    public final String command = "SURRENDER";
    public final String status;
    public final String message;

    public SurrenderResponse(@NotNull final String status, @NotNull final String message) {
        this.status = status;
        this.message = message;
    }
}
