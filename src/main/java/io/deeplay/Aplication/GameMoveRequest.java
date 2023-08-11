package io.deeplay.Aplication;

public class GameMoveRequest implements Request {
    String command = "makeMove";
    String playerId;
    String roomId;
    int row;
    int col;

    GameMoveRequest(String playerId, String roomId, int row, int col) {
        this.playerId = playerId;
        this.roomId = roomId;
        this.row = row;
        this.col = col;
    }
}
