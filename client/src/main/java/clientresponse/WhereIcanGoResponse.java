package clientresponse;

import logic.Board;
import logic.Cell;
import logic.Move;

import java.util.ArrayList;
import java.util.List;

public class WhereIcanGoResponse implements Response {
    public List<Move> availableMoves;

    public Board boardAfterMove;

    public Cell cell;


    public WhereIcanGoResponse(List<Move> availableMoves, Board boardAfterMove, Cell cell) {
        this.availableMoves = availableMoves;
        this.boardAfterMove = boardAfterMove;
        this.cell = cell;
    }

    public List<Move> getAvailableMoves() {
        return availableMoves;
    }

    public void setAvailableMoves(List<Move> availableMoves) {
        this.availableMoves = availableMoves;
    }

    public void setBoard(Board boardAfterMove) {
        this.boardAfterMove = boardAfterMove;
    }

    public Board getBoard() {
        return boardAfterMove;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }
}
