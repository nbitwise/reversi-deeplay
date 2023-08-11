package io.deeplay.Aplication;

public class GameMoveResponse implements Response {
    String status;
    String board;

    GameMoveResponse(String status, String board) {
        this.status = status;
        this.board = board;
    }
}
