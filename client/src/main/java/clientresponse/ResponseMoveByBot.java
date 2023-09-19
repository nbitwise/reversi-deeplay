package clientresponse;

public class ResponseMoveByBot implements Response {

    String command = "SEND_MOVE_TO_SERVER_BOT";
    String status;
    String move;

    public ResponseMoveByBot(String status, String move) {
        this.status = status;
        this.move = move;
    }
}
