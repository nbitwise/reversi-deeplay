package clientbotfarmrequest;

public class RegistrationBotFarmRequest implements Request {

    public String command = "REGISTRATION_BOT_FARM";
    String botName;

    public RegistrationBotFarmRequest(String botName) {
        this.botName = botName;
    }
}
