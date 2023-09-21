package botresponses;

//возвращает клиенту ответ о статусе регистрации на ботФерме
public class ResponseBotFarmRegistration implements Response {
    public final String command = "REGISTRATION_BOT_FARM";
    public String status;
    public String message;

    public ResponseBotFarmRegistration(String status, String message) {
        this.status = status;
        this.message = message;
    }
}


