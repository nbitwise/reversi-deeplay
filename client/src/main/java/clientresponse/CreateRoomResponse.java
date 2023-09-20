package clientresponse;

import org.jetbrains.annotations.NotNull;

public class CreateRoomResponse implements Response {

    public final String command = "CREATEROOM";
    public final String message;
    public final String status;
    public final int roomId;

    public CreateRoomResponse(@NotNull final String message, @NotNull final String status, int roomId) {
        this.message = message;
        this.status = status;
        this.roomId = roomId;
    }


}
