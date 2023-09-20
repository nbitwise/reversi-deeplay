package clientrequest;

public class ViewCreatedRoomsRequest implements Request {
    public final String command = "VIEWROOMS";
    public final int roomId;

    public ViewCreatedRoomsRequest(final int roomId) {
        this.roomId = roomId;
    }
}
