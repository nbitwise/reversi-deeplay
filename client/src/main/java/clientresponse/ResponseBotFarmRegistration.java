package clientresponse;

public class ResponseBotFarmRegistration implements Response {
    public final String command = "REGISTRATION_BOT_FARM";
    public String status;
    public String message;

    public ResponseBotFarmRegistration(String status, String message) {
        this.status = status;
        this.message = message;
    }
}


