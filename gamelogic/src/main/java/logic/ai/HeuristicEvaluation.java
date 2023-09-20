package logic.ai;

import logic.Board;
import logic.Cell;
import org.jetbrains.annotations.NotNull;

/**
 *  interface HeuristicEvaluation - интерфейс для создания оценочных фукнций доски
 */
public interface HeuristicEvaluation {
    double countValue(Board board, Cell cell);
}
