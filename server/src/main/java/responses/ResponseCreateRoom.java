package responses;

public class ResponseCreateRoom extends Response {
    private final int roomId;

    public ResponseCreateRoom(final String status,final String message,final int roomId) {
        super(status, message);
        this.roomId = roomId;

    }
}
