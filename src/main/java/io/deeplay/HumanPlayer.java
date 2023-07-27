package io.deeplay;

class HumanPlayer extends Player {
    public HumanPlayer(Disk symbol) {
        super(symbol);
    }

    @Override
    public int[] makeMove() { //что это?
        // Human player makes the move through the UI, need update and create console app
        return new int[]{-1, -1};
    }
    public Disk getOpponent() {
        return super.getOpponent();
    }



}