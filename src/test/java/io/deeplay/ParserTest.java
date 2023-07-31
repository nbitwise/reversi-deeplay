package io.deeplay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserTest {
    Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testParsing() {
        Board parsedBoard = Parser.parse(
                "_ _ _ _ _ _ _ _ \n" +
                "_ _ _ _ _ _ _ _ \n" +
                "_ _ _ _ _ _ _ _ \n" +
                "_ _ _ - + _ _ _ \n" +
                "_ _ _ + - _ _ _ \n" +
                "_ _ _ _ _ _ _ _ \n" +
                "_ _ _ _ _ _ _ _ \n" +
                "_ _ _ _ _ _ _ _ \n", '+', '-');
        assertEquals(board, parsedBoard);

        parsedBoard = Parser.parse(
                "_ _ _ _ _ _ _ _ \n" +
                        "_ _ _ _ _ _ _ _ \n" +
                        "_ _ _ _ _ _ _ _ \n" +
                        "_ _ _ - + _ _ _ \n" +
                        "_ _ _ + - _ _ _ \n" +
                        "_ _ _ _ _ _ _ _ \n" +
                        "_ _ _ _ _ _ _ _ \n" +
                        "_ _ _ _ _ _ _ _", '+', '-');
        assertEquals(board, parsedBoard);

        board.set(3, 2, Cell.BLACK);
        board.set(3, 3, Cell.BLACK);
        parsedBoard = Parser.parse(
                "_ _ _ _ _ _ _ _ \n" +
                        "_ _ _ _ _ _ _ _ \n" +
                        "_ _ _ _ _ _ _ _ \n" +
                        "_ _ + + + _ _ _ \n" +
                        "_ _ _ + - _ _ _ \n" +
                        "_ _ _ _ _ _ _ _ \n" +
                        "_ _ _ _ _ _ _ _ \n" +
                        "_ _ _ _ _ _ _ _", '+', '-');
        assertEquals(board, parsedBoard);

        board.set(2, 4, Cell.WHITE);
        board.set(3, 4, Cell.WHITE);
        parsedBoard = Parser.parse(
                "_ _ _ _ _ _ _ _ \n" +
                        "_ _ _ _ _ _ _ _ \n" +
                        "_ _ _ _ - _ _ _ \n" +
                        "_ _ + + - _ _ _ \n" +
                        "_ _ _ + - _ _ _ \n" +
                        "_ _ _ _ _ _ _ _ \n" +
                        "_ _ _ _ _ _ _ _ \n" +
                        "_ _ _ _ _ _ _ _", '+', '-');
        assertEquals(board, parsedBoard);

        assertThrows(IllegalArgumentException.class, () -> Parser.parse(
                "_ _ _ _ _ _ _ _ \n" +
                            "_ _ _ _ _ _ _ _ \n" +
                            "_ _ _ _ _ _ _ _ \n" +
                            "_ _ _ - + _ _ _ \n" +
                            "_ _ _ + - _ _ _ \n" +
                            "_ _ _ _ _ _ _ _ \n" +
                            "_ _ _ _ _ _ _ _ \n" +
                            "_ _ _ _ _ _ _ ", '+', '-'));

    }
}