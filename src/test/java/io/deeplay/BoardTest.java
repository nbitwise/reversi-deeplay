package io.deeplay;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board = new Board(8);

    @Test
    void checkNewBoard(){
        assertNull(board.whatIsInside(0, 0));
        assertEquals(Disk.WHITE, board.whatIsInside(3,3)); //на самом деле не 3 должно юыть, размер же доски разный может быть

    }

    @Test
    void setOnPlace() {
        board.setOnPlace(1,3,Disk.WHITE);
        assertEquals(Disk.WHITE, board.whatIsInside(1,3));
    }

    @Test
    void whatIsInside() {
        assertEquals(Disk.WHITE, board.whatIsInside(3,3));
    }

    @Test
    void countDisk() {
        for (int i = 0; i < 5; i++) {
            board.setOnPlace(i,1,Disk.WHITE);
        }
        assertEquals(7,board.countDisk(Disk.WHITE));
    }
}