package logic;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Player {
    private static final AtomicInteger playerCounter = new AtomicInteger(0);
    public final int playerId;
    public final Cell playerCell;

    /**
     * Конструктор создает объект игрока с указанным типом фишки (клетки) и присваивает
     * ему уникальный идентификатор.
     *
     * @param playerCell тип фишки (клетки) игрока (BLACK или WHITE).
     */
    public Player(Cell playerCell) {
        this.playerId = playerCounter.incrementAndGet();
        this.playerCell = playerCell;
    }

    /**
     * Абстрактный метод makeMove, который должен быть реализован в подклассах.
     * Определяет ход игрока в зависимости от типа игрока (HumanPlayer или BotPlayer).
     *
     * @param board доска, на которой происходит игра.
     * @return возвращает объект Move, представляющий сделанный игроком ход.
     */
    public abstract Move makeMove(Board board) throws IOException;

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
                System.out.println(m.row + 1 + " " + (m.col + 1));
            }

            while (true) {
                Date dateStart = new Date();
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
                            Date dateEnd = new Date();
                            long finalTime = dateEnd.getTime()- dateStart.getTime();
                            move.setTimeOnMove(finalTime);
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
            Date dateStart = new Date();
            Move move = availableMoves.get(random.nextInt(availableMoves.size()));
            Date dateEnd = new Date();
            long time = dateEnd.getTime() - dateStart.getTime();
            move.setTimeOnMove(time);
            board.placePiece(move.row, move.col, playerCell);
            return move;
        }
    }
}