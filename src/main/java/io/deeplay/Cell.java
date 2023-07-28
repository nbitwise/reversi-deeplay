package io.deeplay;

public enum Cell {
    BLACK("B"), WHITE("W"), EMPTY("E");
    final String abbreviation;


    Cell(String color) {
        this.abbreviation = color;
    }

    public Cell reverse() {
        if (abbreviation.equals("B")) {
            return Cell.WHITE;
        } else return Cell.BLACK;
    }
}