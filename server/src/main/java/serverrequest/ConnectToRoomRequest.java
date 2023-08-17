package serverrequest;

public class ConnectToRoomRequest implements Request {
    protected final String command = "CONNECTTOROOM";
    public int roomId;

    public ConnectToRoomRequest(int roomId) {

        this.roomId = roomId;
    }

}