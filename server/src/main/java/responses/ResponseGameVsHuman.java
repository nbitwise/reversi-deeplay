package responses;

public class ResponseGameVsHuman extends Response {
    private final int roomId;

    public ResponseGameVsHuman(final String status,final String message,final int roomId) {
        super(status, message);
        this.roomId = roomId;
    }
}
