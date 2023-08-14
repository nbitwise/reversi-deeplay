package responses;

public class ResponseChooseDifficulty extends Response {

    public ResponseChooseDifficulty(String status, String message) {
        super(status, message);
        this.message = message;
    }
}
