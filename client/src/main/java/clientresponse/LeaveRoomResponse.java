package clientresponse;

public class LeaveRoomResponse implements Response {
    public String message = "You get out";
    public String status;

    public LeaveRoomResponse(String status) {

        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
