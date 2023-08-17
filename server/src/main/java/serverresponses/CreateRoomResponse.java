package serverresponses;

public class CreateRoomResponse implements Response {
    private String status;
    private int roomId;
    private String message;

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

    // Геттеры и сеттеры по необходимости
}

