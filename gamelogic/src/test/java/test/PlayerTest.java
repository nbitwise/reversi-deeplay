package test;

import logic.Board;
import logic.Cell;
import logic.Move;
import logic.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    @Test
    public void testBotPlayerMakeMove() {
        final Board board = new Board();
        final Player player = new Player.BotPlayer(Cell.BLACK);

        final List<Move> availableMoves = board.getAllAvailableMoves(Cell.BLACK);
        final Move move = player.makeMove(board);
        assertTrue(availableMoves.contains(move));
    }
    @Test
    void testPlayerId() {
        final Cell blackCell = Cell.BLACK;
        final Cell whiteCell = Cell.WHITE;

        final Player player1 = new Player.HumanPlayer(blackCell);
        final Player player2 = new Player.BotPlayer(whiteCell);
        final Player player3 = new Player.HumanPlayer(blackCell);

        assertNotEquals(player1.playerId, player2.playerId);
        assertNotEquals(player1.playerId, player3.playerId);
        assertNotEquals(player2.playerId, player3.playerId);

        assertEquals(1, player1.playerId);
        assertEquals(2, player2.playerId);
        assertEquals(3, player3.playerId);
    }
}