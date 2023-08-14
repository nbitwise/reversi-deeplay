package response;

public class ConnectForViewResponse implements Response {
    public String status;
    public String message;

    ConnectForViewResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
