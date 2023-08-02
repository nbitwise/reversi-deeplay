package io.deeplay;

import org.junit.jupiter.api.Test;


public class UITest {

    @Test
    void testDisplayMove() {
        final Board board = new Board();
        final Player player = new Player.BotPlayer(Cell.BLACK); // Создаем игрока для теста
        final Move move = new Move(3, 2); // Создаем ход для теста
        final int moveNumber = 1;
        UI.displayMove(moveNumber, board, player, move); // Вывод состояния доски после хода в консоль
    }
}

