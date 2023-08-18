package clientresponse;

public class CreateRoomResponse implements Response {
    public String message;
    public String status;
    public int roomId;

    public CreateRoomResponse(String status, int roomId) {
        this.status = status;
        this.roomId = roomId;
    }

    public String getStatus() {
        return status;
    }

    public int getRoomId() {
        return roomId;
    }

}
