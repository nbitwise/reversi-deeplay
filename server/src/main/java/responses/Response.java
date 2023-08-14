package responses;


import java.util.ArrayList;
import java.util.Arrays;

public abstract class Response {
    String status;
    String message;

    public Response(String command, String message) {
        this.status = command;
        this.message = message;
    }
}

