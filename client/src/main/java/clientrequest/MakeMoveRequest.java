package clientrequest;

public class MakeMoveRequest implements Request {
    public final String command = "MAKEMOVE";
    public final int row;
    public final int col;

    public MakeMoveRequest(final int row, final int col) {
        this.row = row;
        this.col = col;
    }
}
