package io.deeplay;

import java.util.Objects;

public final class Move {
    public final int row;
    public final int col;

    public Move(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return row == move.row && col == move.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
