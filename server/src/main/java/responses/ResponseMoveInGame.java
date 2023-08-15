package responses;


import logic.Board;


public class ResponseMoveInGame extends Response {
    Board board;


    public ResponseMoveInGame(final String status, final String message, Board board) {
        super(status, message);
        this.board = board;

    }
}
