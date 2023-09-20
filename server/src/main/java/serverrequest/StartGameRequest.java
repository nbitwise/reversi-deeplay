package serverrequest;

public class StartGameRequest implements Request {
    public final String command = "STARTGAME";
    public int roomId;
    public final boolean guiFlag;

    public StartGameRequest(int roomId, boolean guiFlag) {
        this.roomId = roomId;
        this.guiFlag = guiFlag;
    }
}
