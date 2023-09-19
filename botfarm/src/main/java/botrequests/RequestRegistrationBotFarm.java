package botrequests;

public class RequestRegistrationBotFarm implements Request {

    public String command = "REGISTRATION_BOT_FARM";
    String botName;

    public RequestRegistrationBotFarm(String botName) {
        this.botName = botName;
    }
}
