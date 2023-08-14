package response;

public class GameVsHumanConnectToRoomResponse implements Response {
    public String status;
    public String message;

    public GameVsHumanConnectToRoomResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
