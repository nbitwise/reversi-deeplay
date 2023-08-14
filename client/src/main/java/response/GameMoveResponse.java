package response;

public class GameMoveResponse implements Response {
    public String status;
    public String board;

    public GameMoveResponse(String status, String board) {
        this.status = status;
        this.board = board;
    }
}
