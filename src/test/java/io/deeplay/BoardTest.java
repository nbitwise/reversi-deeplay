package io.deeplay;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board = new Board();

    @Test
    void checkNewBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row == 3 || row == 4) & (col == 3 || col == 4)) {
                    continue;
                } else {
                    assertEquals(Cell.EMPTY, board.get(row, col));
                }
            }
            assertEquals(Cell.WHITE, board.get(4, 4));
            assertEquals(Cell.WHITE, board.get(3, 3));
            assertEquals(Cell.BLACK, board.get(3, 4));
            assertEquals(Cell.BLACK, board.get(4, 3));

        }
    }

    @Test
    void setOnBoard() {
        board.set(1, 3, Cell.WHITE);
        board.set(2, 3, Cell.BLACK);
        assertEquals(Cell.WHITE, board.get(1, 3));
        assertEquals(Cell.BLACK, board.get(2, 3));
        assertThrows(IllegalArgumentException.class,()->board.set(-2, 3, Cell.WHITE));
    }

    @Test
    void getFromBoard() {
        assertEquals(Cell.WHITE, board.get(3, 3));
        assertEquals(Cell.BLACK, board.get(4, 3));
        assertEquals(Cell.EMPTY, board.get(0, 3));
        assertThrows(IllegalArgumentException.class,()->board.get(-2, 3));
    }

    @Test
    void quantity() {
        assertEquals(2, board.getQuantityOfBlack());
        assertEquals(2, board.getQuantityOfWhite());
        assertEquals(60, board.getQuantityOfEmpty());

        board.set(2, 5, Cell.BLACK);
        assertEquals(3, board.getQuantityOfBlack());
        assertEquals(59, board.getQuantityOfEmpty());

        board.set(5, 5, Cell.BLACK);
        assertEquals(4, board.getQuantityOfBlack());
        assertEquals(58, board.getQuantityOfEmpty());

        board.set(5, 5, Cell.WHITE);
        assertEquals(3, board.getQuantityOfBlack());
        assertEquals(58, board.getQuantityOfEmpty());
    }
}