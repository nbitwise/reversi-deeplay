package io.deeplay;

import org.junit.jupiter.api.Test;


class UITest {

    @Test
    void testDisplayMove() {
        Board board = new Board();
        Player player = new Player.BotPlayer(Cell.BLACK); // Создаем игрока для теста
        Move move = new Move(3, 2); // Создаем ход для теста
        UI.displayMove(board, player, move); // Вывод состояния доски после хода в консоль
    }
}
