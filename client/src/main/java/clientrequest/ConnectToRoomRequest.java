package clientrequest;

import org.jetbrains.annotations.NotNull;

public class ConnectToRoomRequest implements Request {
    public final String command = "CONNECTTOROOM";

    public final int roomId;
    public ConnectToRoomRequest(final int roomId) {
        this.roomId = roomId;
    }
}