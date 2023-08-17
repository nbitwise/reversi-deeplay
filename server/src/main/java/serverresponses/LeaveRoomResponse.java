package serverresponses;

public class LeaveRoomResponse implements Response {
    private String status;

    public LeaveRoomResponse(String status) {

        this.status = status;
    }

    public String getStatus() {

        return status;
    }

}
