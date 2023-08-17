package clientresponse;

public class CreateRoomResponse implements Response {
    private String status;
    private int roomId;

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

    // Геттеры и сеттеры по необходимости
}
