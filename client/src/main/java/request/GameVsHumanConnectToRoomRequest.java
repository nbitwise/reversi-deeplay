package request;

public class GameVsHumanConnectToRoomRequest implements Request {
    public final String command = "connectToRoom";
    public String roomId;

    public GameVsHumanConnectToRoomRequest(String roomId) {
        this.roomId = roomId;
    }
}
