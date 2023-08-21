package clientresponse;

import logic.Board;

public class MadeMoveResponse implements Response{

    public String status;

    public String message;

    public Board board;



    public MadeMoveResponse(String status, String message, Board board) {
        this.status = status;
        this.message = message;
        this.board = board;
    }
}
