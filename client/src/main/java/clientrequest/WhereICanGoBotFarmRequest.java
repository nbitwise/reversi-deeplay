package clientrequest;

public class WhereICanGoBotFarmRequest implements Request {
    public String command = "WHERE_I_CAN_GO_REQUEST";

    public String board;

    public WhereICanGoBotFarmRequest(String board) {
        this.board = board;
    }
}
