package clientbotfarmresponse;

public class BotFarmRegistrationResponse implements Response {
    public final String command = "REGISTRATION_BOT_FARM";
    public String status;
    public String message;

    public BotFarmRegistrationResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}


