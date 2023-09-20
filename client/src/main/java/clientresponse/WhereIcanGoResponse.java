package clientresponse;

import org.jetbrains.annotations.NotNull;

public class WhereIcanGoResponse implements Response {

    public final String command = "WHEREICANGORESPONSE";
    public final String availableMoves;
    public final String board;
    public final String color;
    public final String boardStringWON;

    public WhereIcanGoResponse(@NotNull final String availableMoves, @NotNull final String board,
                               @NotNull final String boardStringWON, @NotNull final String color) {
        this.availableMoves = availableMoves;
        this.color = color;
        this.board = board;
        this.boardStringWON = boardStringWON;
    }
}
