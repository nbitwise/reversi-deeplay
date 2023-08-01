package io.deeplay;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    @Test
    public void testBotPlayerMakeMove() {
        Board board = new Board();
        Player player = new Player.BotPlayer(Cell.BLACK);

        List<Move> availableMoves = board.getAllAvailableMoves(Cell.BLACK);
        Move move = player.makeMove(board);
        assertTrue(availableMoves.contains(move));
    }
}