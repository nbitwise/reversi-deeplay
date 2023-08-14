package responses;

public class ResponseGameVsHuman extends Response {
    int roomId;

    public ResponseGameVsHuman(final String status,final String message,final int roomId) {
        super(status, message);
        this.roomId = roomId;
    }
}
