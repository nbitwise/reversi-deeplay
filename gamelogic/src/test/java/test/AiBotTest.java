package test;

import logic.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AiBotTest {
    @Test
    public void testMakeMove() {
        final Board board = new Board();
        final Player player = new AIBotIlya(Cell.BLACK, 8);

        final List<Move> availableMoves = board.getAllAvailableMoves(Cell.BLACK);
        final Move move = player.makeMove(board);
        assertTrue(availableMoves.contains(move));
    }
}
