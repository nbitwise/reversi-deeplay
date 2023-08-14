package responses;

public class ResponseChooseColor extends Response {
    public ResponseChooseColor(final String status,final String message) {
        super(status, message);
        this.message = message;
    }
}
