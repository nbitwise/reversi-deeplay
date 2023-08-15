package response;

public class GameVsHumanCreateRoomResponse implements Response {
    public String status;
    public String roomId;

    public GameVsHumanCreateRoomResponse(String status, String roomId) {
        this.status = status;
        this.roomId = roomId;
    }
}
