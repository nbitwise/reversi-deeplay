package response;

import java.util.List;

public class ViewAvailableRoomsResponse implements Response {
    public String status;
    public String message;
    List<String> roomIds;

    ViewAvailableRoomsResponse(String status, String message, List<String> roomIds) {
        this.status = status;
        this.message = message;
        this.roomIds = roomIds;
    }
}
