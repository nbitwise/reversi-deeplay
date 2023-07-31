package io.deeplay;


public class Player {
    private final Cell cell;
    private final Board board;
    /**
     * Добавление игрока.
     */
    public Player(Cell cell, Board board) {
        this.cell = cell;
        this.board = board;
    }
    /**
     * Возвращает цвет клетки.
     */
    public Cell getCell() {
        return cell;
    }
    /**
     * Проверка на возможность хода.
     */
    public boolean makeMove(int row, int col) {
        if (board.isValidMove(row, col, cell)) {
            board.placePiece(row, col, cell);
            return true;
        }
        return false;
    }


}
