package clientresponse;

public class MakeMoveResponse implements Response {
    public String command = "MAKEMOVE";

    private String status;
    private String message;

    public MakeMoveResponse(String status, String message) {
        this.message = message;
        this.status = status;
    }

    public String getStatus() {

        return status;
    }
    public String getMessage() {

        return message;
    }
}
