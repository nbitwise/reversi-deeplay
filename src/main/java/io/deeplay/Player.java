package io.deeplay;

import java.util.List;
import java.util.Random;
import java.util.Scanner;


/**
 * Абстрактный класс Player представляет игрока в игре. У каждого игрока есть свой тип фишки (клетки) - Cell,
 * который представляет цвет игрока (BLACK или WHITE).
 */
public abstract class Player {
    protected Cell playerCell;

    public Player(Cell playerCell) {
        this.playerCell = playerCell;
    }

    /**
     * Абстрактный метод makeMove, который должен быть реализован в подклассах.
     * Определяет ход игрока в зависимости от типа игрока (HumanPlayer или BotPlayer).
     *
     * @param board доска, на которой происходит игра.
     * @return возвращает объект Move, представляющий сделанный игроком ход.
     */
    public abstract Move makeMove(Board board);

    /**
     * Подкласс HumanPlayer представляет человеческого игрока, который делает ходы с помощью ввода с клавиатуры.
     */
    public static class HumanPlayer extends Player {
        private static final Scanner scanner = new Scanner(System.in);

        public HumanPlayer(Cell playerCell) {
            super(playerCell);
        }

        @Override
        public Move makeMove(Board board) {
            final List<Move> availableMoves = board.getAllAvailableMoves(playerCell);
            System.out.println("Доступные ходы: ");
            for (Move m : availableMoves) {
                System.out.println(m.row + " " + m.col);
            }

            while (true) {
                System.out.print("Введите строку и столбец (например, 2 3): ");
                String input = scanner.nextLine();
                String[] inputArray = input.trim().split("\\s+");
                if (inputArray.length == 2) {
                    try {
                        int row = Integer.parseInt(inputArray[0]) - 1;
                        int col = Integer.parseInt(inputArray[1]) - 1;
                        final Move move = new Move(row, col);

                        if (availableMoves.contains(move)) {
                            board.placePiece(row, col, playerCell); // Размещаем фишку на доске
                            return move;
                        } else {
                            System.out.println("Недопустимый ход! Пожалуйста, выберите из доступных ходов.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Недопустимый ввод! Пожалуйста, введите два числа через пробел.");
                    }
                } else {
                    System.out.println("Недопустимый ввод! Пожалуйста, введите два числа через пробел.");
                }
            }
        }
    }
    /**
     * Подкласс BotPlayer представляет игрока-бота, который делает случайные ходы.
     */
    public static class BotPlayer extends Player {
        private Random random;

        public BotPlayer(Cell playerCell) {
            super(playerCell);
            this.random = new Random();
        }

        @Override
        public Move makeMove(Board board) {
            List<Move> availableMoves = board.getAllAvailableMoves(playerCell);
            Move move = availableMoves.get(random.nextInt(availableMoves.size()));
            board.placePiece(move.row, move.col, playerCell);
            return move;
        }
    }
}