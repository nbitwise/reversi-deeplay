package serverresponses;

public class CreateRoomResponse implements Response {
    public String status;
    public int roomId;
    public String message;

    public CreateRoomResponse(String status, String message, Integer roomId) {
        this.status = status;
        this.roomId = roomId;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public int getRoomId() {
        return roomId;
    }

}

