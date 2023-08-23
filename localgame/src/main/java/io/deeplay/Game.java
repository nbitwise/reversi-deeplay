package io.deeplay;

import gamelogging.*;
import logic.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.UI;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Класс Game дает возможность запустить игру.
 */
public class Game {

    public Cell nextTurnOfPlayerColor = Cell.BLACK;
    private final static Logger logger = LogManager.getLogger(Board.class);
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
    public void startGame(Board board, final Player black, final Player white, final int gameId,
                                 final String sessionPlayerFile, final String sessionSystemFile) {
        try (FileWriter writeForHuman = new FileWriter(sessionPlayerFile, true);
             FileWriter writerForBot = new FileWriter(sessionSystemFile, true)) {
            GameLogger.logStart(gameId, writeForHuman, writerForBot);
            int moveNumber = 1;
            while (!board.getAllAvailableMoves(black.playerCell).isEmpty() || !board.getAllAvailableMoves(white.playerCell).isEmpty()) {
                Board copyBoard = board.getBoardCopy();
                moveNumber = makeMoveOnBoard(board, black, moveNumber, copyBoard, writeForHuman, writerForBot);
                moveNumber = makeMoveOnBoard(board, white, moveNumber, copyBoard, writeForHuman, writerForBot);
            }
            GameLogger.logEnd(board, writeForHuman, writerForBot);
            displayResult(board);
        } catch (IOException ex) {
            logger.log(Level.ERROR, "Ошибка в работе с файлами в методе startGame.");
        }
    }

    public void startGameWithOutLog(Board board, final Player black, final Player white) throws IOException {
            int moveNumber = 1;
            while (!board.getAllAvailableMoves(black.playerCell).isEmpty() || !board.getAllAvailableMoves(white.playerCell).isEmpty()) {
                Board copyBoard = board.getBoardCopy();
                moveNumber = makeMoveOnBoardWithOutLog(board, black, moveNumber, copyBoard);
                moveNumber = makeMoveOnBoardWithOutLog(board, white, moveNumber, copyBoard);
            }
            displayResult(board);
    }

    public static int makeMoveOnBoardWithOutLog(final Board board, final Player player,
                                       int moveNumber, final Board copyBoard) throws IOException {
        if (!board.getAllAvailableMoves(player.playerCell).isEmpty()) {
            final Move blackMove = player.makeMove(copyBoard);
            board.placePiece(blackMove.row, blackMove.col, player.playerCell);
            UI.displayMove(moveNumber, board, player, blackMove);
            moveNumber++;
        }
        return moveNumber;
    }

    private static int makeMoveOnBoard(final Board board, final Player player,
                                       int moveNumber, final Board copyBoard, final FileWriter writeForHuman, final FileWriter writerForBot) throws IOException {
        if (!board.getAllAvailableMoves(player.playerCell).isEmpty()) {
            final Move blackMove = player.makeMove(copyBoard);
            board.placePiece(blackMove.row, blackMove.col, player.playerCell);
            GameLogger.logMove(board, blackMove.row, blackMove.col, player, blackMove.getTimeOnMove(), writeForHuman, writerForBot);
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
