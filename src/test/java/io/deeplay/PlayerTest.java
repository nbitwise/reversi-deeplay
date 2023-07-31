package io.deeplay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testMakeValidMove() {
        Player player = new Player(Cell.BLACK, board);
        assertTrue(player.makeMove(2, 3)); // Допустимый ход для начальной доски
        assertEquals(Cell.BLACK, board.get(2, 3));
    }

    @Test
    public void testMakeInvalidMove() {
        Player player = new Player(Cell.WHITE, board);
        assertFalse(player.makeMove(0, 0)); // Invalid move, since the cell is not empty
        assertEquals(Cell.EMPTY, board.get(0, 0));
    }

    @Test
    public void testGetCell() {
        Player player = new Player(Cell.WHITE, board);
        assertEquals(Cell.WHITE, player.getCell());
    }
}
