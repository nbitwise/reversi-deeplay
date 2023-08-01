package io.deeplay;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Класс Logging дает возможность записывть ход игры.
 */
public class Logging {
    /**
     * Метод logMove записыват в файлы ходы белых, черных и положение доски после сделанного хода.
     *
     * @param board        доска.
     * @param row          строка.
     * @param col          колонка.
     * @param player       игркок.
     * @param fileForHuman Название файла в который будет записан ход понятный человеку.
     * @param fileForBot   Название файла с укороченной записью.
     */
    public static void logMove(final Board board, final int row, final int col, Player player, final String fileForHuman, final String fileForBot) {
        try (FileWriter writeForHuman = new FileWriter(fileForHuman, true);
             FileWriter writerForBot = new FileWriter(fileForBot, true)) {
            String textForHuman;
            String textForBot;
            if (player.playerCell.equals(Cell.BLACK)) {
                textForHuman = "Player BLACK placed his piece on " + (row + 1) + " " + (col + 1);
                textForBot = "b" + row + "" + col;
            } else {
                textForHuman = "Player WHITE placed his piece on " + (row + 1) + " " + (col + 1);
                textForBot = "w" + row + "" + col;
            }
            textForHuman += "\nboard\n";
            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    textForHuman += board.get(i, j).toString() + " ";
                }
                textForHuman += "\n";
            }
            textForHuman += "\n";
            writerForBot.write(textForBot);
            writeForHuman.write(textForHuman);
            writerForBot.flush();
            writeForHuman.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Метод logStart записыват в файл дату игры.
     *
     * @param fileForHuman Название файла в который будет записан ход понятный человеку.
     */
    public static void logStart(final String fileForHuman) {
        try (FileWriter writeForHuman = new FileWriter(fileForHuman, true)) {
            final String textForHuman = "Game " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + "\n";
            writeForHuman.write(textForHuman);
            writeForHuman.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
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
            System.out.println(ex.getMessage());
        }
    }

}
