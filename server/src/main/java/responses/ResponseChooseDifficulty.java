package responses;

public class ResponseChooseDifficulty extends Response {

    public ResponseChooseDifficulty(final String status,final String message) {
        super(status, message);
        this.message = message;
    }
}
