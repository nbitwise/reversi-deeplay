package clientbotfarmrequest;

import org.jetbrains.annotations.NotNull;
/**
 * Класс запроса на авторизацию
 */
public class AuthorizationRequest implements Request {
    /**
     * Название запроса
     */
    public final String command = "AUTHORIZATION";

    /**
     * Имя игрока
     */
    public final String nickname;

    public AuthorizationRequest(@NotNull final String nickname) {
        this.nickname = nickname;
    }
}
