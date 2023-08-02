package io.deeplay;

import org.junit.jupiter.api.Test;


final class UITest {

    @Test
    void testDisplayMove() {
        Board board = new Board();
        Player player = new Player.BotPlayer(Cell.BLACK);
        Move move = new Move(3, 2);
        int moveNumber = 1;
        UI.displayMove(moveNumber, board, player, move);
    }
}

