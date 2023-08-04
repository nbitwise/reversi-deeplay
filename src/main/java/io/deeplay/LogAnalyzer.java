package io.deeplay;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс LogAnalyzer дает возможность анализировать файл с записаью игр, показывать статистику по победам/поражениям,
 * количеству выигранных игр.
 */
public class LogAnalyzer {
    /**
     * Метод parseLog записывает статистику по каждой игре в файле с логами игры.
     *
     * @param fileNameReadLog  Название файла в который будет записан ход понятный человеку.
     * @param fileNameWriteLog Название файла с укороченной записью.
     */
    public static void parseLog(final String fileNameReadLog, final String fileNameWriteLog) {
        try (final FileReader file = new FileReader(fileNameReadLog)) {
            final BufferedReader reader = new BufferedReader(file);
            int currentNumberOfLine = 1;
            int idFirstPlayer = 0;
            int idSecondPlayer = 0;
            String line = reader.readLine();
            while (line != null) {
                if (currentNumberOfLine == 2) {
                    idFirstPlayer = Integer.parseInt(line.substring(10, line.indexOf('B') - 1));
                }
                if (currentNumberOfLine == 11) {
                    idSecondPlayer = Integer.parseInt(line.substring(10, line.indexOf('W') - 1));
                }
                if (line.startsWith("Winner")) {
                    final String winner = line.substring(8, 13);
                    if (winner.equals("Black")) {
                        addAnalysis(idFirstPlayer, "Black", idSecondPlayer, fileNameWriteLog);
                    } else if (winner.equals("White")) {
                        addAnalysis(idSecondPlayer, "White", idFirstPlayer, fileNameWriteLog);
                    }
                    currentNumberOfLine = 0;
                }
                currentNumberOfLine++;
                line = reader.readLine();
            }

        } catch (IOException ex) {
            Logger logger = LogManager.getLogger(Logging.class);
            logger.log(Level.ERROR, ex.getMessage());
        }
    }

    /**
     * Метод writeAnalysis записывает статистику по игрокам, которые играют друг с дргуом первый раз.
     *
     * @param idPlayer         id игрока который победил.
     * @param color            цвет победившего игрока.
     * @param idOpponent       id проигравшего.
     * @param fileNameWriteLog Название файла с анализом логов.
     */
    private static void writeAnalysis(final int idPlayer, final String color, final int idOpponent, final String fileNameWriteLog) {
        try (final FileWriter writeForHuman = new FileWriter(fileNameWriteLog, true)) {
            final String textForHuman = "PlayerId " + idPlayer + " win vs PlayerId " + idOpponent + " by " + color + " " + 1 +
                    " times " + "\n";
            writeForHuman.write(textForHuman);
            writeForHuman.flush();
        } catch (IOException ex) {
            Logger logger = LogManager.getLogger(Logging.class);
            logger.log(Level.ERROR, ex.getMessage());
        }
    }

    /**
     * Метод addAnalysis записывает статистику по игрокам, которые уже играли друг с дргуом.
     *
     * @param idPlayer         id игрока который победил.
     * @param color            цвет победившего игрока.
     * @param idOpponent       id проигравшего.
     * @param fileNameWriteLog Название файла с анализом логов.
     */
    private static void addAnalysis(final int idPlayer, final String color, final int idOpponent, final String fileNameWriteLog) {
        final Path path = Paths.get(fileNameWriteLog);

        try (final Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8);
             final FileReader file = new FileReader(fileNameWriteLog)) {
            boolean flag = false;
            final  BufferedReader reader = new BufferedReader(file);
            String lineFromAnalysis = reader.readLine();
            while (lineFromAnalysis != null) {
                if (lineFromAnalysis.startsWith("PlayerId " + idPlayer + " win vs PlayerId " + idOpponent)) {
                    flag = true;
                }
                lineFromAnalysis = reader.readLine();
            }
            if (flag) {
                List<String> list = stream.map(line -> line.startsWith("PlayerId " + idPlayer + " win vs PlayerId " + idOpponent) ?
                                "PlayerId " + idPlayer + " win vs PlayerId " + idOpponent + " by " + color + " " +
                                        (Integer.parseInt(line.substring(38, line.indexOf("times") - 1)) + 1) + " times"
                                : line)
                        .collect(Collectors.toList());

                Files.write(path, list, StandardCharsets.UTF_8);
            } else {
                writeAnalysis(idPlayer, "White", idOpponent, fileNameWriteLog);
            }
        } catch (IOException ex) {
            Logger logger = LogManager.getLogger(Logging.class);
            logger.log(Level.ERROR, ex.getMessage());
        }
    }
}
