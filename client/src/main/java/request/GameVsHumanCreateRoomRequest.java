package request;

public class GameVsHumanCreateRoomRequest implements Request {
    public final String command = "createRoom";
    public String color;
    public String timeControl;

    GameVsHumanCreateRoomRequest(String color, String timeControl) {
        this.color = color;
        this.timeControl = timeControl;
    }
}
