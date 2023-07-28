package io.deeplay;

public class Parser {

    private Parser(){}

    /**
     * Возвращает объект типа Board, конвертируя String bouardinstring.
     *
     * @param boardinstring - строка, содержащая поле и разсоположенные на ней фишки, вида      "________\n" +
     *                                                                                          "________\n" +
     *                                                                                          "________\n" +
     *                                                                                          "___-+___\n" +
     *                                                                                          "___+-___\n" +
     *                                                                                          "________\n" +
     *                                                                                          "________\n" +
     *                                                                                          "________\n"
     * @return возвращает доску.
     */
    public static Board parser(String boardinstring){
        Board board = new Board();
        for (int row = 0; row < Board.getBOARD_SIZE(); row++) {
            for (int col = 0; col < Board.getBOARD_SIZE(); col++) {
                char currentCell = boardinstring.charAt((Board.getBOARD_SIZE()+1)*row + col);
                if(currentCell == '+')
                    board.set(row, col,Cell.BLACK);
                else if (currentCell == '-') {
                    board.set(row, col,Cell.WHITE);
                }
            }
        }
        return board;
    }
}

