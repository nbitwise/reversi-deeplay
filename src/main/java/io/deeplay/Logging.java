package io.deeplay;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Класс Logging дает возможность записывть ход игры.
 */
public class Logging {
    /**
     * Метод logMove записыват в файлы ходы белых, черных и положение доски после сделанного хода.
     *
     * @param board      доска.
     * @param row        строка.
     * @param col        колонка.
     * @param player     игркок.
     * @param playerFile Название файла в который будет записан ход понятный человеку.
     * @param systemFile Название файла с укороченной записью.
     * @param timeOnMove Время потравченное игроком на ход
     */
    public static void logMove(final Board board, final int row, final int col, Player player, final String playerFile, final String systemFile, long timeOnMove) {
        try (FileWriter writeForHuman = new FileWriter(playerFile, true);
             FileWriter writerForBot = new FileWriter(systemFile, true)) {
            String textForHuman;
            String textForSystem = "";
            if (player.playerCell.equals(Cell.BLACK)) {
                textForHuman = "PlayerId: " + player.playerId + " BLACK placed his piece on " + (row + 1) + " "
                        + (col + 1) + "and spent on it " + timeOnMove + " ms\n";
            } else {
                textForHuman = "PlayerId: " + player.playerId + " WHITE placed his piece on " + (row + 1) + " "
                        + (col + 1) + "and spent on it " + timeOnMove + " ms\n";
            }
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
            Logger logger = LogManager.getLogger(Logging.class);
            logger.log(Level.ERROR, ex.getMessage());
        }
    }

    /**
     * Метод logStart записыват в файл дату игры.
     *
     * @param playerFile Название файла в который будет записан ход понятный человеку.
     * @param systemFile Название файла в который будет записаны ошибки.
     * @param gameId     id игры.
     */
    public static void logStart(final String playerFile, final String systemFile, final int gameId) {
        try (FileWriter writeForHuman = new FileWriter(playerFile, true);
             FileWriter writeForSystem = new FileWriter(systemFile, true)) {
            final String text = "Game id " + gameId + " \n";
            writeForHuman.write(text);
            writeForSystem.write(text);
            writeForHuman.flush();
            writeForSystem.flush();
        } catch (IOException ex) {
            Logger logger = LogManager.getLogger(Logging.class);
            logger.log(Level.ERROR, ex.getMessage());
        }
    }

    /**
     * Метод logEnd записывает результат игры.
     *
     * @param board        доска.
     * @param fileForHuman Название файла в который будет записан результат понятный человеку.
     * @param fileForBot   Название файла для коротой записи.
     */
    public static void logEnd(final Board board, final String fileForHuman, final String fileForBot) {
        try (FileWriter writeForHuman = new FileWriter(fileForHuman, true);
             FileWriter writeForBot = new FileWriter(fileForBot, true)) {

            final int blackCount = board.getQuantityOfBlack();
            final int whiteCount = board.getQuantityOfWhite();

            String textForHuman = "Number of Black pieces: " + blackCount + "\n" + "Number of white pieces: " + whiteCount + ".\n" + "Winner: ";
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
            Logger logger = LogManager.getLogger(Logging.class);
            logger.log(Level.ERROR, ex.getMessage());
        }
    }

}
