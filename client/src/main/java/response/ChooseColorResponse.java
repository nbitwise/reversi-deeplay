package response;

public class ChooseColorResponse implements Response {
    public String status;
    public String message;

    ChooseColorResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
