package clientrequest;
/**
 * Класс запроса на регистрацию
 */
public class RegistrationRequest implements Request {
    /**
     * Название запроса
     */
    protected final String command = "REGISTRATION";
    /**
     * Имя игрока
     */
    protected String nickname;

    public RegistrationRequest(String nickname) {
        this.nickname = nickname;
    }
}
