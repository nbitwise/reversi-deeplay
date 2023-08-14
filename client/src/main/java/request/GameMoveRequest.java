package request;

public class GameMoveRequest implements Request {
    public final String command = "makeMove";
    public final String playerId;
    public final String roomId;
    int row;
    int col;

    GameMoveRequest(String playerId, String roomId, int row, int col) {
        this.playerId = playerId;
        this.roomId = roomId;
        this.row = row;
        this.col = col;
    }
}
