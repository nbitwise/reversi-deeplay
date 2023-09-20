package test;

import logic.*;
import logic.ai.MovesCornersEvaluate;
import logic.ai.QuantityEvaluate;
import logic.ai.StageEvaluator;
import org.junit.jupiter.api.Test;
import parsing.BoardParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeuristicFunctionsTest {
    @Test
    public void testWeightEvaluate() {
        final Board board = BoardParser.parse(
                "b b b b b b w b \n" +
                        "  w w w w w w b \n" +
                        "  w w b w b w b \n" +
                        "    w w b b w b \n" +
                        "b b b w w w w   \n" +
                        "b b b b w w w b \n" +
                        "b b b     w w   \n" +
                        "b b b       w b ", 'b', 'w', ' ');
        final double value = new StageEvaluator().countValue(board, Cell.BLACK);
        assertEquals(81548.0, (int)value);
    }

    @Test
    public void testMovesCornersEvaluate() {
        final Board board = BoardParser.parse(
                "b b b b b b     \n" +
                        "    w w w w w w \n" +
                        "  w b b b b   w \n" +
                        "    b b b b w b \n" +
                        "  w b b b w w   \n" +
                        "w b b b b b w b \n" +
                        "b b b       w   \n" +
                        "b b b       w b ", 'b', 'w', ' ');
        final double value = new MovesCornersEvaluate().countValue(board, Cell.BLACK);
        assertEquals(75074.0,(int) value);
    }

    @Test
    public void testQuantityEvaluate() {
        final Board board = BoardParser.parse(
                "b b b w   b     \n" +
                        "    w b b b b   \n" +
                        "  w b b b b   w \n" +
                        "    b b b b w b \n" +
                        "  w b b b w     \n" +
                        "w w b b b b b b \n" +
                        "  b w       w   \n" +
                        "b w         w b ", 'b', '2', ' ');
        final double value = new QuantityEvaluate().countValue(board, Cell.BLACK);
        assertEquals(2147483647, (int) value);
    }
}
