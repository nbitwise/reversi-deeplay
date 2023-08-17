package clientresponse;

public class ConnectToRoomResponse implements Response {
    private String status;
    private String message;

    public ConnectToRoomResponse(String status, String message) {

        this.status = status;
        this.message = message;
    }

    public String getStatus() {

        return status;
    }
}
