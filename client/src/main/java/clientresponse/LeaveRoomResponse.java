package clientresponse;

import org.jetbrains.annotations.NotNull;

public class LeaveRoomResponse implements Response {
    public final String command = "LEAVEROOM";
    public final String message;
    public final String status;

    public LeaveRoomResponse(@NotNull final String message, @NotNull final String status) {
        this.message = message;
        this.status = status;
    }
}
