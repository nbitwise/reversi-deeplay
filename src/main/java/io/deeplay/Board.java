package io.deeplay;

import java.util.Arrays;
import java.util.Objects;

import static io.deeplay.Cell.BLACK;
import static io.deeplay.Cell.WHITE;

public class Board {
    private final Cell[][] board;
    private final int BOARD_SIZE = 8;
    private int quantityOfWhite = 2;
    private int quantityOfBlack = 2;


    /**
     * Создает доску и четыре фишки по центру карты.
     */
    public Board() {

        board = new Cell[BOARD_SIZE][BOARD_SIZE];
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int cow = 0; cow < BOARD_SIZE; cow++) {
                board[row][cow] = Cell.EMPTY;
            }
        }

        board[3][3] = WHITE;
        board[4][4] = WHITE;
        board[3][4] = BLACK;
        board[4][3] = BLACK;
    }


    /**
     * Установка диска в указанное поле.
     *
     * @param row  - строка.
     * @param col  - колонка.
     * @param cell - клетка, который нужно поставить в поле.
     */
    public void set(int row, int col, Cell cell) {
        checkArgument(row, col);

        if (board[row][col] == BLACK) {
            quantityOfWhite++;
            quantityOfBlack--;
        }
        if (board[row][col] == WHITE) {
            quantityOfWhite--;
            quantityOfBlack++;
        }
        if (board[row][col] == Cell.EMPTY) {
            if (cell == WHITE) quantityOfWhite++;
            if (cell == BLACK) quantityOfBlack++;
        }
        board[row][col] = cell;

    }

    /**
     * Возвращает значение cell, которое лежит в поле.
     *
     * @param row - строка.
     * @param col - колонка.
     * @return возвращает клетку.
     */
    public Cell get(int row, int col) {
        checkArgument(row, col);
        return board[row][col];
    }

    private boolean isValidMove(int row, int col, Cell cell) {

        checkArgument(row, col);
        if (board[row][col] != Cell.EMPTY) {
            return false;
        }

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }

                int r = row + dr;
                int c = col + dc;
                boolean isValidDirection = false;

                while (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == cell.reverse()) {
                    r += dr;
                    c += dc;
                    isValidDirection = true;
                }

                if (isValidDirection && r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == cell.reverse()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasAvailableMoves(Cell cell) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (isValidMove(i, j, cell)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Возвращает количество белых клеток.
     */
    public int getQuantityOfWhite() {
        return quantityOfWhite;
    }

    /**
     * Возвращает количество черных клеток.
     */
    public int getQuantityOfBlack() {
        return quantityOfBlack;
    }

    /**
     * Возвращает количество пустых клеток.
     */
    public int getQuantityOfEmpty() {
        return BOARD_SIZE * BOARD_SIZE - quantityOfBlack - quantityOfWhite;
    }

    private void checkArgument(int row, int col) {
        if (row >= BOARD_SIZE || row < 0 || col >= BOARD_SIZE || col < 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board board1)) return false;
        return Arrays.deepEquals(board, board1.board);
    }


    @Override
    public int hashCode() {
        int result = Objects.hash(BOARD_SIZE);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }

    public Board getBoardCopy() {
        Board copy = new Board();
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(this.board[i], 0, copy.board[i], 0, BOARD_SIZE);
        }
        return copy;
    }

    private void placePiece(int row, int col, Cell player) {
        this.set(row, col, player);

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }

                int r = row + dr;
                int c = col + dc;
                boolean isValidDirection = false;
                boolean hasOpponentPiece = false;

                while (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == player.reverse()) {
                    r += dr;
                    c += dc;
                    isValidDirection = true;
                    hasOpponentPiece = true;
                }

                if (isValidDirection && r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == player && hasOpponentPiece) {
                    while (r != row || c != col) {
                        r -= dr;
                        c -= dc;
                        this.set(r, c, player);
                    }
                }
            }
        }
    }

    public Board placePieceAndGetCopy(int row, int col, Cell player) {
        placePiece(row, col, player);
        return getBoardCopy();
    }

//    private Cell getOpponent(Cell player) {
//        return (player == BLACK) ? WHITE : BLACK;
//    }

}


