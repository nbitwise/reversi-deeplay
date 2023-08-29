package gamelogging;

import logic.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/**
 * Класс Logging дает возможность записывть ход игры.
 */
public class GameLogger {

    /**
     * Метод logMove записыват в файлы ходы белых, черных и положение доски после сделанного хода.
     *
     * @param board         доска.
     * @param row           строка.
     * @param col           колонка.
     * @param player        игркок.
     * @param timeOnMove    Время потравченное игроком на ход
     * @param writeForHuman FileWriter файла человекочитаемых записей
     * @param writerForBot  FileWriter фалйа для логов
     */


    public static void logMove(final Board board, final int row, final int col, Player player,
                               long timeOnMove, final FileWriter writeForHuman, final FileWriter writerForBot) {
        try {
            String textForHuman;
            String textForSystem = "";
            textForHuman = constructStringForLogMove(player, player.playerId, row + 1, col + 1, timeOnMove);
            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    textForHuman += board.get(i, j).toString() + " ";
                }
                textForHuman += "\n";
            }
            writerForBot.write(textForSystem);
            writeForHuman.write(textForHuman);
            writerForBot.flush();
            writeForHuman.flush();
        } catch (IOException ex) {
            Logger logger = LogManager.getLogger(GameLogger.class);
            logger.log(Level.ERROR, "Ошибка в логировании хода.");
        }
    }

    public static void logMove(final Board board, final int row, final int col, UUID uuid, String color,
                               final FileWriter writeForHuman, final FileWriter writerForBot) {
        try {
            String textForHuman;
            String textForSystem = "";
            textForHuman = constructStringForLogMove(color, uuid, row + 1, col + 1);
            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    textForHuman += board.get(i, j).toString() + " ";
                }
                textForHuman += "\n";
            }
            writerForBot.write(textForSystem);
            writeForHuman.write(textForHuman);
            writerForBot.flush();
            writeForHuman.flush();
        } catch (IOException ex) {
            Logger logger = LogManager.getLogger(GameLogger.class);
            logger.log(Level.ERROR, "Ошибка в логировании хода.");
        }
    }

    /**
     * Метод constructStringForLogMove создает строку для записи хода игрока
     *
     * @param player     игрок.
     * @param playerId   id игрока.
     * @param row        строка игры.
     * @param col        колонка.
     * @param timeOnMove время на ход.
     */
    public static String constructStringForLogMove(final Player player, final int playerId, final int row, final int col,
                                                   final long timeOnMove) {
        final String line;
        if (player.playerCell.equals(Cell.BLACK)) {
            line = String.format("PlayerId: %d BLACK placed his piece on %d %d and spent on it %d%n", playerId, row, col, timeOnMove);
        } else {
            line = String.format("PlayerId: %d WHITE placed his piece on %d %d and spent on it %d%n", playerId, row, col, timeOnMove);
        }
        return line;
    }

    public static String constructStringForLogMove(String color, final UUID playerId, final int row, final int col) {
        final String line;
        if (color.equals("BLACK")) {
            line = String.format("PlayerId: %s BLACK placed his piece on %d %d%n", playerId.toString(), row, col);
        } else {
            line = String.format("PlayerId: %s WHITE placed his piece on %d %d%n", playerId.toString(), row, col);
        }
        return line;
    }

    /**
     * Метод logStart записыват в файл дату игры.
     *
     * @param gameId id игры.
     */
    public static void logStart(final int gameId, final FileWriter writeForHuman, final FileWriter writeForSystem) {
        try {
            final String text = "Game id " + gameId + " \n";
            writeForHuman.write(text);
            writeForSystem.write(text);
            writeForHuman.flush();
            writeForSystem.flush();
        } catch (IOException ex) {
            Logger logger = LogManager.getLogger(GameLogger.class);
            logger.log(Level.ERROR, "ошибка в начале логирования, gameID: " + gameId);
        }
    }

    /**
     * Метод logEnd записывает результат игры.
     *
     * @param board         доска.
     * @param writeForHuman FileWriter файла человекочитаемых записей
     * @param writeForBot   FileWriter фалйа для логов
     */
    public static void logEnd(final Board board, final FileWriter writeForHuman, final FileWriter writeForBot) {
        try {
            final int blackCount = board.getQuantityOfBlack();
            final int whiteCount = board.getQuantityOfWhite();
            String textForHuman = String.format("Number of Black pieces: %d%nNumber of white pieces: %d.%nWinner: ", blackCount, whiteCount);
            String textForBot;
            if (blackCount > whiteCount) {
                textForHuman += "Black\n";
                textForBot = "B\n";
            } else if (whiteCount > blackCount) {
                textForHuman += "White\n";
                textForBot = "W\n";
            } else {
                textForHuman += "It's tie\n";
                textForBot = "T\n";
            }
            writeForBot.write(textForBot);
            writeForHuman.write(textForHuman);
            writeForBot.flush();
            writeForHuman.flush();
        } catch (IOException ex) {
            Logger logger = LogManager.getLogger(GameLogger.class);
            logger.log(Level.ERROR, "ошибка в завершении логирования.");
        }
    }

}
