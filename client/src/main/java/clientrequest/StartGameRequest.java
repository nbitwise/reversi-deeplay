package clientrequest;

public class StartGameRequest implements Request {
    public final String command = "STARTGAME";
    public final int roomId;
    public final boolean guiFlag;

    public StartGameRequest(final int roomId, final boolean guiFlag) {
        this.roomId = roomId;
        this.guiFlag = guiFlag;
    }
}
