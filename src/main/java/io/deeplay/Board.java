package io.deeplay;

import java.util.Arrays;
import java.util.Objects;

public class Board {
    private final Cell[][] board;
    private final int boardSize;


    public Board(int boardSize) { //создание доски
        if (boardSize <= 3) {
            throw new IllegalArgumentException("неверный размер доски");
        } else {

            this.boardSize = boardSize;
            board = new Cell[boardSize][boardSize];

            for (int row = 0; row < boardSize; row++) {
                for (int cow = 0; cow < boardSize; cow++) {
                    board[row][cow] = Cell.EMPTY;
                }
            }

            int center = boardSize / 2;
            board[center - 1][center - 1] = Cell.WHITE;
            board[center][center] = Cell.WHITE;
            board[center - 1][center] = Cell.BLACK;
            board[center][center - 1] = Cell.BLACK;
        }
    }


    /**
     * установка диска в указанную ячейку
     */
    public void setOnPlace(int row, int col, Cell cell) {
        try {
            board[row][col] = cell;
        } catch (IllegalArgumentException exception) {
            System.out.println("неверные координаты, повторите ввод");
        }

    }

    /**
     * возвращает значение cell, которое лежит в клетке
     */
    public Cell getFromPlace(int row, int col) {
        try {
            return board[row][col];
        } catch (IllegalArgumentException exception) {
            System.out.println("неверные координаты, повторите ввод");
            return null;
        }
    }

    /**
     * считает диски указанного цвета
     */
    public int countDisk(Cell cell) {
        int count = 0;
        for (Cell[] cells : board) {
            for (int col = 0; col < boardSize; col++) {
                if (cells[col] == cell) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isValidMove(int row, int col, Player player) {
        if (row < 0 || row >= boardSize || col < 0 || col >= boardSize || board[row][col] != null) {
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

                while (r >= 0 && r < boardSize && c >= 0 && c < boardSize && board[r][c] == player.getOpponent()) {
                    r += dr;
                    c += dc;
                    isValidDirection = true;
                }

                if (isValidDirection && r >= 0 && r < boardSize && c >= 0 && c < boardSize && board[r][c] == player.getOpponent()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasAvailableMoves(Player player) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (isValidMove(i, j, player)) {
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
        return boardSize == board1.boardSize && Arrays.deepEquals(board, board1.board);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(boardSize);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }
}


