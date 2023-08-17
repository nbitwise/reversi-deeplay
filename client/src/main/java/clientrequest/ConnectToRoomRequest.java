package clientrequest;

public class ConnectToRoomRequest implements Request {
    protected final String command = "CONNECTTOROOM";
    private int roomId;

    public ConnectToRoomRequest(int roomId) {

        this.roomId = roomId;

    }

}