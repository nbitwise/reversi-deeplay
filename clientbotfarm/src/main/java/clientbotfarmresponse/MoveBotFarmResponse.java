package clientbotfarmresponse;

public class MoveBotFarmResponse implements Response {

    String command = "SEND_MOVE_TO_SERVER_BOT";
    String status;
    String move;

    public MoveBotFarmResponse(String status, String move) {
        this.status = status;
        this.move = move;
    }
}
