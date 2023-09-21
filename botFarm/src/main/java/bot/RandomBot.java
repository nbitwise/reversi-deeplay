package bot;

import logic.Board;
import logic.Cell;
import logic.Move;
import logic.Player;

import java.util.List;
import java.util.Random;

public class RandomBot extends Player {
    private Random random;

    public RandomBot(Cell playerCell) {
        super(playerCell);
        this.random = new Random();
    }

    @Override
    public Move makeMove(Board board) {
        List<Move> availableMoves = board.getAllAvailableMoves(playerCell);
        Move move = availableMoves.get(random.nextInt(availableMoves.size()));
        board.placePiece(move.row, move.col, playerCell);
        return move;
    }

}
