package responses;

public class ResponseCreateRoom extends Response {
    int roomId;

    public ResponseCreateRoom(String status, String message, int roomId) {
        super(status, message);
        this.roomId = roomId;

    }
}
