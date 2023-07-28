package io.deeplay;

public enum Cell {
    BLACK, WHITE, EMPTY;

    public Cell reverse() {
        if (this == BLACK) {
            return Cell.WHITE;
        } else return Cell.BLACK;
    }
}