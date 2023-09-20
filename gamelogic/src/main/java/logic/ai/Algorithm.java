package logic.ai;

import logic.Board;
import logic.Cell;
import logic.Move;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * interface Algorithm - интерфейс алгоритмов подобных минимаксу
 */
public interface Algorithm {
    @Contract(pure = true)
    static @Nullable Move getBestMove(Board board, final Cell cell, int depth, final HeuristicEvaluation he) {
        return null;
    }
}
