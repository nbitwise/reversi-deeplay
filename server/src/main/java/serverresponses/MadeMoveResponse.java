package serverresponses;

import logic.Board;
import logic.Move;

import java.util.ArrayList;

public class MadeMoveResponse implements Response{

    private String status;

    private String message;

    private Board board;



    public MadeMoveResponse(String status, String message, Board board) {
        this.status = status;
        this.message = message;
        this.board = board;
    }


}
