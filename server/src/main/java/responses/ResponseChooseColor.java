package responses;

public class ResponseChooseColor extends Response {
    public ResponseChooseColor(String status, String message) {
        super(status, message);
        this.message = message;
    }
}
