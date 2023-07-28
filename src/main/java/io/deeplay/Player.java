package io.deeplay;

abstract class Player {
    protected Cell colorOfPlayersCell; //заменил с чара на диск

    public Player(Cell cell) {
        this.colorOfPlayersCell = cell;
    }

    public abstract int[] makeMove();

    public Cell getOpponent() {
        return (colorOfPlayersCell == Cell.BLACK) ? Cell.WHITE : Cell.BLACK;
    }
}
