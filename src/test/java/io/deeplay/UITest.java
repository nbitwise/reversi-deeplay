package io.deeplay;

import org.junit.jupiter.api.Test;


final class UITest {

    @Test
    void testDisplayMove() {
        Board board = new Board();
        Player player = new Player.BotPlayer(Cell.BLACK); // Создаем игрока для теста
        Move move = new Move(3, 2); // Создаем ход для теста
        int moveNumber = 1;
        UI.displayMove(moveNumber, board, player, move); // Вывод состояния доски после хода в консоль
    }
}

