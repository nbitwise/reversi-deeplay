package response;

public class GameVsHumanConnectToRoomResponse implements Response {
    public String status;
    public String message;

    GameVsHumanConnectToRoomResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
