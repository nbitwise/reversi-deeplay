package responses;

public class ResponseGameVsHuman extends Response {
    int roomId;

    public ResponseGameVsHuman(String status, String message, int roomId) {
        super(status, message);
        this.roomId = roomId;
    }
}
