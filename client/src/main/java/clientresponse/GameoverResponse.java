package clientresponse;

import org.jetbrains.annotations.NotNull;

public class GameOverResponse implements Response {
    public final String command = "GAMEOVER";
    public final String status;
    public final String message;

    public GameOverResponse(@NotNull final String status, @NotNull final String message) {
        this.status = status;
        this.message = message;
    }
}
