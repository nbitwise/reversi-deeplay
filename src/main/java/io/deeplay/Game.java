package io.deeplay;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Класс Game дает возможность запустить игру.
 */
public class Game {
    /**
     * Метод startGame запускает игру. По окончанию игры выводится результат.
     *
     * @param board             доска.
     * @param black             игрок черными.
     * @param white             игрок белыми.
     * @param gameId            id игры.
     * @param sessionPlayerFile файл в который будет записан лог игры.
     * @param sessionSystemFile файл в который будут записаны ошибки.
     */
    public static void startGame(Board board, final Player black, final Player white, final int gameId,
                                 final String sessionPlayerFile, final String sessionSystemFile) {
        try (FileWriter writeForHuman = new FileWriter(sessionPlayerFile, true);
             FileWriter writerForBot = new FileWriter(sessionSystemFile, true)) {
            Logging.logStart(gameId, writeForHuman, writerForBot);
            int moveNumber = 1;
            while (!board.getAllAvailableMoves(black.playerCell).isEmpty() || !board.getAllAvailableMoves(white.playerCell).isEmpty()) {
                Board copyBoard = board.getBoardCopy();
                moveNumber = makeMoveOnBoard(board, black, moveNumber, copyBoard, writeForHuman, writerForBot);
                moveNumber = makeMoveOnBoard(board, white, moveNumber, copyBoard, writeForHuman, writerForBot);
            }
            Logging.logEnd(board, writeForHuman, writerForBot);
            displayResult(board);
        } catch (IOException ex) {
            final Logger logger = LogManager.getLogger(Board.class);
            logger.log(Level.ERROR, "Ошибка в работе с файлами в методе startGame.");
        }
    }

    private static int makeMoveOnBoard(final Board board, final Player player,
                                       int moveNumber, final Board copyBoard, final FileWriter writeForHuman, final FileWriter writerForBot) {
        if (!board.getAllAvailableMoves(player.playerCell).isEmpty()) {
            final Move blackMove = player.makeMove(copyBoard);
            board.placePiece(blackMove.row, blackMove.col, player.playerCell);
            Logging.logMove(board, blackMove.row, blackMove.col, player, blackMove.getTimeOnMove(), writeForHuman, writerForBot);
            UI.displayMove(moveNumber, board, player, blackMove);
            moveNumber++;
        }
        return moveNumber;
    }


    /**
     * Метод displayResult отображает результат партии.
     *
     * @param board доска.
     */
    private static void displayResult(final Board board) {
        final int blackCount = board.getQuantityOfBlack();
        final int whiteCount = board.getQuantityOfWhite();

        System.out.println("Number of Black pieces: " + blackCount);
        System.out.println("Number of White pieces: " + whiteCount);

        if (blackCount > whiteCount) {
            System.out.println("Winner: Black");
        } else if (whiteCount > blackCount) {
            System.out.println("Winner: White");
        } else {
            System.out.println("It's a Tie");
        }
    }
}
