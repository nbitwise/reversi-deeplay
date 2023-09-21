package botrequests;

//запрос на регистарцию на бот ферме
public class RequestRegistrationBotFarm implements Request {

    public final String command = "REGISTRATION_BOT_FARM";
    public final String botName;

    public RequestRegistrationBotFarm(String botName) {
        this.botName = botName;
    }
}
