package io.deeplay;

abstract class Player {
    protected Disk colorOfPlayersDisk; //заменил с чара на диск

    public Player(Disk disk) {
        this.colorOfPlayersDisk = disk;
    }

    public abstract int[] makeMove();

    public Disk getOpponent() {
        return (colorOfPlayersDisk == Disk.BLACK) ? Disk.WHITE : Disk.BLACK;
    }
}
