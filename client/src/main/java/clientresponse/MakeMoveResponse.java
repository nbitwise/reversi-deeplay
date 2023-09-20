package clientresponse;

import org.jetbrains.annotations.NotNull;

public class MakeMoveResponse implements Response {
    public final String command = "MAKEMOVE";
    public final String status;
    public final String message;

    public MakeMoveResponse(@NotNull final String status, @NotNull final String message) {
        this.message = message;
        this.status = status;
    }
}
