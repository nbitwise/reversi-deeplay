package clientresponse;


import org.jetbrains.annotations.NotNull;

public class ViewCreatedRoomsResponse implements Response {
    public final String command = "VIEWROOMS";

    public final int roomId;

    public final String status;

    public final String message;

    public ViewCreatedRoomsResponse(@NotNull final String status, @NotNull final String message, int roomId) {
        this.message = message;
        this.roomId = roomId;
        this.status = status;
    }
}
