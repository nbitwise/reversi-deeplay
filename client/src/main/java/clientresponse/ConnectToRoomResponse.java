package clientresponse;

import org.jetbrains.annotations.NotNull;

public class ConnectToRoomResponse implements Response {
    public final String command = "CONNECTTOROOM";
    public final String message;
    public final String status;

    public ConnectToRoomResponse(@NotNull final String status, @NotNull final String message) {
        this.status = status;
        this.message = message;
    }
}
