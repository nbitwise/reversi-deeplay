package serverrequest;

import clientrequest.Request;
import logic.Cell;
import logic.Move;

public class MakeMoveRequest implements Request {

    public String command = "MAKEMOVE";
    Move move;
    Cell cell;

    public MakeMoveRequest(Move move, Cell cell) {
        this.move = move;
        this.cell = cell;
    }

    public Move getMove() {
        return move;
    }
}