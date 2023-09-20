package botrequests;

//запрос бот ферме на то, чтобы предоставила ход по отправленной доске
public class RequestWhereICanGoBotFarm implements Request {

    public final String command = "WHERE_I_CAN_GO_REQUEST";
    public final String board;
    public RequestWhereICanGoBotFarm(String board) {
        this.board = board;
    }
}
