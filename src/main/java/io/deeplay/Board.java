package io.deeplay;

import java.util.Arrays;
import java.util.Objects;

public class Board {
    private final Cell[][] board;
    private final int BOARD_SIZE = 8;

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

        int center = BOARD_SIZE / 2;
        board[center - 1][center - 1] = Cell.WHITE;
        board[center][center] = Cell.WHITE;
        board[center - 1][center] = Cell.BLACK;
        board[center][center - 1] = Cell.BLACK;
    }


    /**
     * Установка диска в указанную ячейку.
     */
    public void setOnPlace(int row, int col, Cell cell) {
        if (row > BOARD_SIZE - 1 || row < 0 || col > BOARD_SIZE - 1 || col < 0) {
            throw new IllegalArgumentException();
        } else {
            board[row][col] = cell;
        }
    }

    /**
     * Возвращает значение cell, которое лежит в клетке.
     */
    public Cell getFromPlace(int row, int col) {
        if (row > BOARD_SIZE - 1 || row < 0 || col > BOARD_SIZE - 1 || col < 0) {
            throw new IllegalArgumentException();
        } else {
            return board[row][col];
        }
    }


    /**
     * Считает диски указанного цвета.
     */
    public int countDisk(Cell cell) {
        int count = 0;
        for (Cell[] cells : board) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (cells[col] == cell) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isValidMove(int row, int col, Cell cell) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE || board[row][col] != null) {
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
}


