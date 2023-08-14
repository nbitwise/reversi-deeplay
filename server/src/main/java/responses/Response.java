package responses;


import java.util.ArrayList;
import java.util.Arrays;

public abstract class Response {
    String status;
    String message;

    public Response(final String command, final String message) {
        this.status = command;
        this.message = message;
    }
}

