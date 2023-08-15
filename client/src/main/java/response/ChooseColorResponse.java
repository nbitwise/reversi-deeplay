package response;

public class ChooseColorResponse implements Response {
    public String status;
    public String message;

    public ChooseColorResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
