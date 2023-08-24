package clientresponse;

import logic.Board;
import logic.Cell;
import logic.Move;

import java.util.ArrayList;
import java.util.List;

public class WhereIcanGoResponse implements Response {

    public String command = "WHEREICANGORESPONSE";
    public String availableMoves;
    public String board;

    public WhereIcanGoResponse(String availableMoves, String board) {
        this.availableMoves = availableMoves;
        this.board = board;
    }
}
