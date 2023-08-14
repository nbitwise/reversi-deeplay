package response;

import java.util.List;

public class ViewStartedGamesResponse implements Response {
    public String status;
    public String message;
    public List<String> roomIds;

    public ViewStartedGamesResponse(String status, String message, List<String> roomIds) {
        this.status = status;
        this.message = message;
        this.roomIds = roomIds;
    }
}
