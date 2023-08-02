package io.deeplay;

/**
 * Класс Game дает возможность запустить игру.
 */
public class Game {

    private static final String fileForHuman = "fileForHuman";
    private static final String fileForBot = "fileForBot";

    /**
     * Метод startGame запускает игру. По окончанию игры выводится результат.
     *
     * @param board доска.
     * @param black игрок черными.
     * @param white игрок белыми.
     */
    public static void startGame(Board board, final Player black, final Player white) {
        Logging.logStart(fileForHuman);
        int moveNumber = 1;

        while (board.getQuantityOfEmpty() > 0) {
            Board copyBoard = board.getBoardCopy();
            if (!board.getAllAvailableMoves(black.playerCell).isEmpty()) {
                final Move blackMove = black.makeMove(copyBoard);
                board.placePiece(blackMove.row, blackMove.col, black.playerCell);
                Logging.logMove(board, blackMove.row, blackMove.col, black, fileForHuman, fileForBot);
                UI.displayMove(moveNumber, board, black, blackMove);
                moveNumber++;
            }
            if (!board.getAllAvailableMoves(white.playerCell).isEmpty()) {
                final Move whiteMove = white.makeMove(copyBoard);
                board.placePiece(whiteMove.row, whiteMove.col, white.playerCell);
                Logging.logMove(board, whiteMove.row, whiteMove.col, white, fileForHuman, fileForBot);
                UI.displayMove(moveNumber, board, white, whiteMove);
                moveNumber++;
            }
        }
        Logging.logEnd(board, fileForHuman, fileForBot);
        displayResult(board);
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
