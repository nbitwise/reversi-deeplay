package serverresponses;

public class ConnectToRoomResponse implements Response {
    public String status;
    public String message;

    public ConnectToRoomResponse(String status, String message) {

        this.status = status;
        this.message = message;
    }

    public String getStatus() {

        return status;
    }
    public String getMessage() {

        return message;
    }
}
