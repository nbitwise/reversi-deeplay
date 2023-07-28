package io.deeplay;

class HumanPlayer extends Player {
    public HumanPlayer(Cell symbol) {
        super(symbol);
    }

    @Override
    public int[] makeMove() { //что это?
        // Human player makes the move through the UI, need update and create console app
        return new int[]{-1, -1};
    }
    public Cell getOpponent() {
        return super.getOpponent();
    }



}