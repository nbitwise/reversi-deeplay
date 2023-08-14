package request;

public class GameVsHumanFindRoomRequest implements Request {
    public final String command = "findRoom";
    public String color;

    public GameVsHumanFindRoomRequest(String color) {
        this.color = color;
    }
}
