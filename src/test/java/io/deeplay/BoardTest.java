package io.deeplay;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board = new Board(8);

    @Test
    void checkNewBoard(){
        assertEquals(Cell.EMPTY, board.getFromPlace(0, 0));
        assertEquals(Cell.WHITE, board.getFromPlace(3,3));
        assertEquals(Cell.BLACK, board.getFromPlace(3,4));

    }

    @Test
    void setOnPlace() {
        board.setOnPlace(1,3, Cell.WHITE);
        board.setOnPlace(2,3, Cell.BLACK);
        assertEquals(Cell.WHITE, board.getFromPlace(1,3));
        assertEquals(Cell.BLACK, board.getFromPlace(2,3));
    }

    @Test
    void whatIsInside() {
        assertEquals(Cell.WHITE, board.getFromPlace(3,3));
    }

    @Test
    void countDisk() {
        for (int i = 0; i < 5; i++) {
            board.setOnPlace(i,1, Cell.WHITE);
        }
        assertEquals(7,board.countDisk(Cell.WHITE));
        assertEquals(2,board.countDisk(Cell.BLACK));
    }
}