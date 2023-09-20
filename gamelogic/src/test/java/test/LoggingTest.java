package test;

import gamelogging.GameLogger;
import logic.Board;
import org.junit.jupiter.api.Test;
import parsing.BoardParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class LoggingTest {

    @Test
    void testLogStart() throws IOException {
        try {
            final PrintWriter pwHuman = new PrintWriter("fileForHumanTest");
            pwHuman.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GameLogger.logStart(1, "fileForHumanTest");
        final FileReader humanReader = new FileReader("fileForHumanTest");
        final StringBuilder result = new StringBuilder();
        char[] massiveFromFile = new char[11];
        humanReader.read(massiveFromFile);
        for (char c : massiveFromFile
        ) {
            result.append(c);
        }
        final boolean res = Pattern.matches("Game id 1 \n", result);
        assertTrue(res);
    }

    @Test
    void testLogEnd() throws IOException {
        try {
            final PrintWriter pwHuman = new PrintWriter("fileForHumanTest");
            final PrintWriter pwBot = new PrintWriter("fileForBotTest");
            pwHuman.close();
            pwBot.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Board finalBoard = BoardParser.parse("""
                b b b b b b b b\s
                w b w w w b b b\s
                w w b b w b w b\s
                w b b b b b w b\s
                w b b b b b w b\s
                w b w b b b w b\s
                w w w w w b w b\s
                w w w b b b b b""", 'b', 'w', '_');
        GameLogger.logEnd(finalBoard, "fileForHumanTest");
        final FileReader humanReader = new FileReader("fileForHumanTest");
        char[] massiveFromFile = new char[71];
        humanReader.read(massiveFromFile);
        final StringBuilder resultFromFile = new StringBuilder();
        for (char c : massiveFromFile
        ) {
            resultFromFile.append(c);
        }
        final String expectedResult = """
                Number of Black pieces: 40\r
                Number of white pieces: 24.\r
                Winner: Black
                """;
        assertEquals(resultFromFile.toString(), expectedResult);
    }
}
