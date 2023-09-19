package botrequests;

public class RequestWhereICanGoBotFarm implements Request {
    public String command = "WHERE_I_CAN_GO_REQUEST";

    public String board;

    public RequestWhereICanGoBotFarm(String board) {
        this.board = board;
    }
}
