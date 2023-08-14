package request;

public class GameVsHumanFindRoomRequest implements Request {
    public final String command = "findRoom";
    public String color;

    GameVsHumanFindRoomRequest(String color) {
        this.color = color;
    }
}
