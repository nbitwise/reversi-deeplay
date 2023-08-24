package serverresponses;

public class SurrenderResponse implements Response {
    public String command = "SURRENDER";

    private final String status = "success";
    private String message;

    public SurrenderResponse(String message) {
        this.message = message;
    }
}
