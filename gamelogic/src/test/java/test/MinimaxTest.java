package test;

import logic.*;
import logic.ai.StageEvaluator;
import org.junit.jupiter.api.Test;
import parsing.BoardParser;

import static logic.ai.Minimax.minimax;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinimaxTest {
    @Test
    public void testMinimax() {
        final Board boardTest1 = BoardParser.parse(
                "_ _ _ _ _ _ _ _ \n" +
                        "_ _ _ _ _ _ _ _ \n" +
                        "_ _ _ _ - _ _ _ \n" +
                        "_ _ + + - _ _ _ \n" +
                        "_ _ _ + - _ _ _ \n" +
                        "_ _ _ _ _ _ _ _ \n" +
                        "_ _ _ _ _ _ _ _ \n" +
                        "_ _ _ _ _ _ _ _ ", '+', '-', '_');

        final double valueDepth2 = minimax(2, Integer.MIN_VALUE, Integer.MAX_VALUE, boardTest1, Cell.BLACK, false, new StageEvaluator());
        final double valueDepth3 = minimax(3, Integer.MIN_VALUE, Integer.MAX_VALUE, boardTest1, Cell.BLACK, false, new StageEvaluator());

        assertEquals(500.0, valueDepth2);
        assertEquals(500.0, valueDepth3);

        final Board boardTest2 = BoardParser.parse(
                "w w   w     w w \n" +
                        "b b w w w b w b \n" +
                        "b b w w w w w w \n" +
                        "b b b w b b w w \n" +
                        "b b w w w b w w \n" +
                        "b b b w b w w w \n" +
                        "b b b b b b w   \n" +
                        "b b b b b b b b ", 'b', 'w', ' ');

        final Player ai = new AIBotIlya(Cell.BLACK, 7);

        final Move move = ai.makeMove(boardTest2);
        assertEquals(new Move(0,2), move);

    }
}
