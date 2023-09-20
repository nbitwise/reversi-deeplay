package clientresponse;


import org.jetbrains.annotations.NotNull;

public class StartGameResponse implements Response {
    public final String command = "STARTGAME";
    public final String status;
    public final String message;

    public StartGameResponse(@NotNull final String status, @NotNull final String message) {
        this.message = message;
        this.status = status;
    }
}